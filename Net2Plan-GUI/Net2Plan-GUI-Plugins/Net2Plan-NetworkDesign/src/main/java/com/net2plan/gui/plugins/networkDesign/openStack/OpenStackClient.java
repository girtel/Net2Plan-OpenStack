package com.net2plan.gui.plugins.networkDesign.openStack;

import com.google.common.collect.Lists;
import com.net2plan.gui.plugins.networkDesign.api.Gnocchi;
import com.net2plan.gui.plugins.networkDesign.api.Keystone;
import com.net2plan.gui.plugins.networkDesign.openStack.compute.*;
import com.net2plan.gui.plugins.networkDesign.openStack.identity.*;
import com.net2plan.gui.plugins.networkDesign.openStack.image.OpenStackImageV2;
import com.net2plan.gui.plugins.networkDesign.openStack.network.OpenStackNetwork;
import com.net2plan.gui.plugins.networkDesign.openStack.network.OpenStackPort;
import com.net2plan.gui.plugins.networkDesign.openStack.network.OpenStackRouter;
import com.net2plan.gui.plugins.networkDesign.openStack.network.OpenStackSubnet;
import com.net2plan.gui.plugins.networkDesign.openStack.telemetry.OpenStackGnocchiMeasure;
import com.net2plan.gui.plugins.networkDesign.openStack.telemetry.OpenStackMeter;
import com.net2plan.gui.plugins.networkDesign.openStack.telemetry.OpenStackResource;
import com.net2plan.gui.plugins.utils.MyRunnable;

import com.net2plan.gui.plugins.utils.OpenStackUtils;
import com.net2plan.interfaces.networkDesign.NetPlan;
import org.json.JSONArray;
import org.json.JSONObject;
import org.openstack4j.api.Builders;
import org.openstack4j.api.OSClient;
import org.openstack4j.api.telemetry.TelemetryService;
import org.openstack4j.api.types.Facing;
import org.openstack4j.model.common.Identifier;
import org.openstack4j.model.compute.*;
import org.openstack4j.model.identity.v3.*;
import org.openstack4j.model.identity.v3.builder.EndpointBuilder;
import org.openstack4j.model.network.Network;
import org.openstack4j.model.network.Port;
import org.openstack4j.model.network.Router;
import org.openstack4j.model.network.Subnet;
import org.openstack4j.model.telemetry.Meter;
import org.openstack4j.model.telemetry.Resource;
import org.openstack4j.openstack.OSFactory;

import java.awt.geom.Point2D;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import static edu.emory.mathcs.utils.ConcurrencyUtils.submit;

public class OpenStackClient {

    public String os_auth_url;
    private String os_username;
    private String os_password;
    private String os_user_domain_name;
    private String os_project_id;

    private OSClient.OSClientV3 os;
    private OpenStackNetCreate openStackNetCreate;
    private OpenStackNetDelete openStackNetDelete;

    private String name;
    private Boolean connect=false;
    /*List of OpenStackNetworkElements of KEYSTONE*/
    public final List<OpenStackUser> openStackUsers = new ArrayList<> ();
    public final List<OpenStackProject> openStackProjects = new ArrayList<> ();
    public final List<OpenStackDomain> openStackDomains = new ArrayList<> ();
    public final List<OpenStackEndpoint> openStackEndpoints = new ArrayList<> ();
    public final List<OpenStackService> openStackServices = new ArrayList<> ();
    public final List<OpenStackRegion> openStackRegions = new ArrayList<> ();
    public final List<OpenStackCredential> openStackCredentials = new ArrayList<> ();
    public final List<OpenStackGroup> openStackGroups = new ArrayList<> ();
    public final List<OpenStackPolicy> openStackPolicies = new ArrayList<> ();
    public final List<OpenStackRole> openStackRoles = new ArrayList<> ();

    /*List of OpenStackNetworkElements of NEUTRON*/
    public final List<OpenStackNetwork> openStackNetworks = new ArrayList<>();
    public final List<OpenStackSubnet> openStackSubnets = new ArrayList<> ();
    public final List<OpenStackRouter> openStackRouters = new ArrayList<> ();
    public final List<OpenStackPort> openStackPorts = new ArrayList<> ();

    /*List of OpenStackNetworkElements of NOVA*/
    public final List<OpenStackServer> openStackServers = new ArrayList<> ();
    public final List<OpenStackFlavor> openStackFlavors = new ArrayList<> ();
    public final List<OpenStackFloatingIp> openStackFloatingIps = new ArrayList<> ();
    public final List<OpenStackKeypair> openStackKeypairs = new ArrayList<> ();
    public final List<OpenStackSecurityGroup> openStackSecurityGroups = new ArrayList<> ();
    public final List<OpenStackHostResource> openStackHostResources = new ArrayList<> ();

    /*List of OpenStackNetworkElements of GLANCE*/
    public final List<OpenStackImageV2> openStackImages = new ArrayList<> ();

    /*List of OpenStackNetworkElements of CEILOMETER*/
    public final List<OpenStackMeter> openStackMeters = new ArrayList<> ();
    public final List<OpenStackResource> openStackResources = new ArrayList<> ();
    public List<OpenStackMeter> openStackMetersAvailable = new ArrayList<>();
    public final List<OpenStackGnocchiMeasure> openStackMeasures = new ArrayList<>();

    private Gnocchi gnocchi;
    private Keystone keystone;
    private OpenStackNet osn;
    private Token token;
    private NetPlan netPlan;

    public OpenStackClient (){
        this.osn= new OpenStackNet();
        this.netPlan = new NetPlan();
    }
    public OpenStackClient create(OpenStackNet osn,JSONObject jsonObject,String name){

        try {
            this.os_auth_url = jsonObject.getString("os_auth_url");
            this.os_username = jsonObject.getString("os_username");
            this.os_password = jsonObject.getString("os_password");
            this.os_user_domain_name = jsonObject.getString("os_user_domain_name");
            this.os_project_id = jsonObject.getString("os_project_id");

            OSFactory.enableHttpLoggingFilter(false);

            OSClient.OSClientV3 os = OSFactory.builderV3()
                    .endpoint(os_auth_url)
                    .credentials(os_username,os_password, Identifier.byName(os_user_domain_name))
                    .scopeToProject(Identifier.byId(os_project_id))
                    .authenticate();

            this.os=os;
            this.name = name;
            this.osn = osn;
            this.openStackNetCreate = new OpenStackNetCreate(this);
            this.openStackNetDelete = new OpenStackNetDelete(this);
            this.token = os.getToken();
            this.os = OSFactory.clientFromToken(token);
            this.netPlan = new NetPlan();
            prepareApis();

            connect=true;

        }catch (Exception ex){
            ex.printStackTrace();
        }
        return this;
    }

    public void prepareApis(){
        Token token = os.getToken();
        MyRunnable newR = new MyRunnable(token,Facing.PUBLIC);
        submit(newR);
        this.os=newR.getOs();
        List<Service> services = this.os.identity().serviceEndpoints().list().stream().filter(n -> ((Service) n).getName().equals("gnocchi")).collect(Collectors.toList());

        if(services.size() >= 1) {

            Service service = services.get(0);
            Endpoint endpoint = this.os.identity().serviceEndpoints().listEndpoints().stream().filter(n -> ((Endpoint) n).getServiceId().equals(service.getId())).collect(Collectors.toList()).get(0);
            this.gnocchi = new Gnocchi(endpoint.getUrl().toString() + "/v1/", this.os);
        }
        this.keystone = new Keystone(os_auth_url,this.os);


    }

    public OpenStackClient clearList(){

        /*Clear Keystone list*/
        openStackUsers.clear();
        openStackProjects.clear();
        openStackDomains.clear();
        openStackEndpoints.clear();
        openStackServices.clear();
        openStackRegions.clear();
        openStackGroups.clear();
        openStackPolicies.clear();
        openStackRoles.clear();

        /*Clear Neutron list*/
        openStackNetworks.clear();
        openStackRouters.clear();
        openStackPorts.clear();
        openStackSubnets.clear();

        /*Clear Nova list*/
        openStackServers.clear();
        openStackFlavors.clear();
        openStackFloatingIps.clear();
        openStackKeypairs.clear();
        openStackSecurityGroups.clear();
        openStackHostResources.clear();

        /*Clear Glance list*/
        openStackImages.clear();

        /*Clear Ceilometer list*/
        openStackResources.clear();

        netPlan.removeAllNodes();
        return this;
    }
    public OpenStackClient fillList(){

        try {
            this.os = OSFactory.clientFromToken(token);
            /* Get elements of Identity(Keystone)*/
            this.os.identity().users().list().forEach(n->addOpenStackUser(n));
            this.os.identity().domains().list().forEach(n->addOpenStackDomain(n));
            this.os.identity().serviceEndpoints().listEndpoints().forEach(n->addOpenStackEndpoint(n));
            this.os.identity().serviceEndpoints().list().forEach(n-> addOpenStackService(n));
            this.os.identity().regions().list().forEach(n->addOpenStackRegion(n));
            this.os.identity().credentials().list().forEach(n->addOpenStackCredential(n));
            this.os.identity().groups().list().forEach(n->addOpenStackGroup(n));
            this.os.identity().policies().list().forEach(n->addOpenStackPolicy(n));
            this.os.identity().roles().list().forEach(n->addOpenStackRole(n));
            this.keystone.projectList().stream().forEach(n->addOpenStackProject(n));
            /* Get elements of Network(NEUTRON)*/
            this.os.networking().network().list().forEach(n -> addOpenStackNetwork(n));
            this.os.networking().subnet().list().forEach(n -> addOpenStackSubnet(n));
            this.os.networking().router().list().stream().forEach(n -> addOpenStackRouter(n));
            this.os.networking().port().list().stream().forEach(n -> addOpenStackPort(n));

            /*Get elements of Compute(NOVA)*/
           // System.out.println("SERVERS "+this.os_auth_url+ " "+OSFactory.clientFromToken(token).compute().servers().list().size());
            this.os.compute().servers().list().stream().forEach(n -> addOpenStackServer(n));
            this.os.compute().flavors().list().stream().forEach(n -> addOpenStackFlavor(n));
            this.os.compute().floatingIps().list().forEach(n -> addOpenStackFloatingIP(n));
            this.os.compute().keypairs().list().stream().forEach(n -> addOpenStackKeypair(n));
            this.os.compute().securityGroups().list().stream().forEach(n -> addOpenStackSecurityGroup(n));

            final List<? extends HostResource> hosts = os.compute().host().list();
            hosts.stream().forEach(n -> os.compute().host().hostDescribe(((HostResource) n).getHostName()).stream().forEach(p -> addOpenStackHostResource(p)));

            /*Get elements of Image(GLANCE)*/
            this.os.imagesV2().list().stream().forEach(n -> addOpenStackImage(n));

            /*Get elements of Telmetry(Ceilometer)*/
            gnocchi.resourcesList().forEach(n->addOpenStackResource(n));

            doTopology();
        }catch(Exception ex){
            ex.printStackTrace();
        }

        return this;
    }

    public void updateMeterList(String resource_id){

        //System.out.println ("Clearing meter for " + resource_id);
        openStackMeters.clear();
        gnocchi.metersList().forEach(n->addOpenStackMeter(n));
        //System.out.println("Meters "+ openStackMeters);
        openStackMeters.forEach(n->openStackMetersAvailable.add(n));
        openStackMeters.clear();
       // System.out.println("Meters available"+ openStackMetersAvailable);
        openStackMetersAvailable.forEach(n->{if(n.getMeter_resource_id().equals(resource_id)){
        openStackMeters.add(n);
        }
        });
      //  System.out.println("Meters available"+ openStackMeters);
        osn.getCallback().getViewEditTopTables().updateView();

    }
    public void updateMeasuresList(String metric_id){
       // System.out.println("Meteasures"+ gnocchi.measuresList(metric_id));
        gnocchi.measuresList(metric_id).forEach(n -> {openStackMeasures.add( OpenStackGnocchiMeasure.createFromAddMeasure(this.osn,((JSONArray)n).get(0).toString(),((JSONArray)n).get(1).toString(),((JSONArray)n).get(2).toString(),this));});
        //System.out.println("Meteasures"+ openStackMeasures);
        osn.getCallback().getViewEditTopTables().updateView();
    }

    public String getName(){return this.name;}
    public String getProjectId(){return this.os_project_id;}
    public Boolean isConnected(){return this.connect;}

    public OSClient.OSClientV3 getClient(){
        return this.os;
    }
    public OpenStackNetCreate getOpenStackNetCreate(){ return this.openStackNetCreate; }
    public OpenStackNetDelete getOpenStackNetDelete(){ return this.openStackNetDelete; }
    public OpenStackNet getOsn (){return  this.osn;}
    public Token getToken (){return this.token;}
    public NetPlan getNetPlanDesign(){return this.netPlan;}
    public void setNetPlanDesign(NetPlan netPlan){this.netPlan = netPlan;}

    /* Add OpenStackNetworkElements of Keystone*/
    public OpenStackUser addOpenStackUser (User user){
       final OpenStackUser res = OpenStackUser.createFromAddUser(this.osn ,user,this);
        if(openStackUsers.contains(res)) return res;
        openStackUsers.add(res);
        return res;
    }
    public OpenStackProject addOpenStackProject(Project project){
        final OpenStackProject res = OpenStackProject.createFromAddProject(this.osn,project,this);
        if(openStackProjects.contains(res)) return res;
        openStackProjects.add(res);
        return res;
    }
    public OpenStackDomain addOpenStackDomain(Domain domain) {
        final OpenStackDomain res = OpenStackDomain.createFromAddDomain(this.osn,domain,this);
        if(openStackDomains.contains(res)) return res;
        openStackDomains.add(res);
        return res;
    }
    public OpenStackEndpoint addOpenStackEndpoint(Endpoint endpoint) {
        final OpenStackEndpoint res = OpenStackEndpoint.createFromAddEndpoint(this.osn,endpoint ,this);
        if(openStackEndpoints.contains(res)) return res;
        openStackEndpoints.add(res);
        return res;
    }
    public OpenStackService addOpenStackService(Service service) {
        final OpenStackService res = OpenStackService.createFromAddService(this.osn,service,this);
        if(openStackServices.contains(res)) return res;
        openStackServices.add(res);
        return res;
    }
    public OpenStackRegion addOpenStackRegion(Region region) {
        final OpenStackRegion res = OpenStackRegion.createFromAddRegion(this.osn,region,this);
        if(openStackRegions.contains(res)) return res;
        openStackRegions.add(res);
        return res;
    }
    public OpenStackCredential addOpenStackCredential(Credential credential) {
        final OpenStackCredential res = OpenStackCredential.createFromAddCredential(this.osn,credential,this);
        if(openStackCredentials.contains(res)) return res;
        openStackCredentials.add(res);
        return res;
    }
    public OpenStackGroup addOpenStackGroup(Group group) {
        final OpenStackGroup res = OpenStackGroup.createFromAddGroup(this.osn,group,this);
        if(openStackGroups.contains(res)) return res;
        openStackGroups.add(res);
        return res;
    }
    public OpenStackPolicy addOpenStackPolicy(Policy policy) {
        final OpenStackPolicy res = OpenStackPolicy.createFromAddPolicy(this.osn,policy,this);
        if(openStackPolicies.contains(res)) return res;
        openStackPolicies.add(res);
        return res;
    }
    public OpenStackRole addOpenStackRole(Role role) {
        final OpenStackRole res = OpenStackRole.createFromAddRole(this.osn,role,this);
        if(openStackRoles.contains(res)) return res;
        openStackRoles.add(res);
        return res;
    }

    /* Add OpenStackNetworkElements of Neutron*/
    public OpenStackNetwork addOpenStackNetwork(Network network) {
        final OpenStackNetwork res = OpenStackNetwork.createFromAddNetwork(osn ,network,this);
        if(openStackNetworks.contains(res)) return res;
        openStackNetworks.add(res);
        return res;
    }
    public OpenStackSubnet addOpenStackSubnet (Subnet subnet) {
        final OpenStackSubnet res = OpenStackSubnet.createFromAddSubnet(osn,subnet,this);
        if(openStackSubnets.contains(res)) return res;
        openStackSubnets.add(res);
        return res;
    }
    public OpenStackRouter addOpenStackRouter(Router router) {
        final OpenStackRouter res = OpenStackRouter.createFromAddRouter(osn,router,this);
        if(openStackRouters.contains(res)) return res;
        openStackRouters.add(res);
        return res;
    }
    public OpenStackPort addOpenStackPort(Port port) {
        final OpenStackPort res = OpenStackPort.createFromAddPort(osn,port,this);
        if(openStackPorts.contains(res)) return res;
        openStackPorts.add(res);
        return res;
    }

    /* Add OpenStackNetworkElements of Nova*/
    public OpenStackServer addOpenStackServer(Server server) {
        final OpenStackServer res = OpenStackServer.createFromAddServer(osn,server,this);
        if(openStackServers.contains(res)) return res;
        openStackServers.add(res);
        return res;
    }
    public OpenStackFlavor addOpenStackFlavor(Flavor flavor) {
        final OpenStackFlavor res = OpenStackFlavor.createFromAddFlavor(osn,flavor,this);
        if(openStackFlavors.contains(res)) return res;
        openStackFlavors.add(res);
        return res;
    }
    public OpenStackFloatingIp addOpenStackFloatingIP(FloatingIP floatingIP) {
        final OpenStackFloatingIp res = OpenStackFloatingIp.createFromAddFloatingIp(osn,floatingIP,this);
        if(openStackFloatingIps.contains(res)) return res;
        openStackFloatingIps.add(res);
        return res;
    }
    public OpenStackKeypair addOpenStackKeypair(Keypair keypair){

        final OpenStackKeypair res = OpenStackKeypair.createFromAddKeypair(osn,keypair,this);
        if(openStackKeypairs.contains(res)) return res;
        openStackKeypairs.add(res);
        return res;
    }
    public OpenStackSecurityGroup addOpenStackSecurityGroup(SecGroupExtension securityGroup) {
        final OpenStackSecurityGroup res = OpenStackSecurityGroup.createFromAddSegurityGroup(osn,securityGroup,this);
        if(openStackSecurityGroups.contains(res)) return res;
        openStackSecurityGroups.add(res);
        return res;
    }
    public OpenStackHostResource addOpenStackHostResource(HostResource hostResource) {
        final OpenStackHostResource res = OpenStackHostResource.createFromAddHostResource(osn,hostResource,this);
        if(openStackHostResources.contains(res)) return res;
        openStackHostResources.add(res);

        return res;
    }

    /* Add OpenStackNetworkElements of Glance*/
    public OpenStackImageV2 addOpenStackImage(org.openstack4j.model.image.v2.Image image) {
        final OpenStackImageV2 res = OpenStackImageV2.createFromAddImageV2(osn,image,this);
        if(openStackImages.contains(res)) return res;
        openStackImages.add(res);
        return res;
    }

    /* Add OpenStackNetworkElements of Ceilometer*/
    public OpenStackMeter addOpenStackMeter(Meter meter) {
        final OpenStackMeter res = OpenStackMeter.createFromAddMeter(osn ,meter,this);
        if(openStackMeters.contains(res)) return res;
        openStackMeters.add(res);
        return res;
    }
    public OpenStackResource addOpenStackResource(Resource resource) {
        final OpenStackResource res = OpenStackResource.createFromAddResource(osn ,resource,this);
        if(openStackResources.contains(res)) return res;
        openStackResources.add(res);
        return res;
    }


    /*Get list from OpenStackNetworkElements of KEYSTONE*/
    public List<OpenStackUser> getOpenStackUsers () { return Collections.unmodifiableList(openStackUsers); }
    public List<OpenStackProject> getOpenStackProjects () { return Collections.unmodifiableList(openStackProjects); }
    public List<OpenStackDomain> getOpenStackDomains () { return Collections.unmodifiableList(openStackDomains); }
    public List<OpenStackEndpoint> getOpenStackEndpoints () { return Collections.unmodifiableList(openStackEndpoints); }
    public List<OpenStackService> getOpenStackServices () { return Collections.unmodifiableList(openStackServices); }
    public List<OpenStackRegion> getOpenStackRegions () { return Collections.unmodifiableList(openStackRegions); }
    public List<OpenStackCredential> getOpenStackCredentials () { return Collections.unmodifiableList(openStackCredentials); }
    public List<OpenStackGroup> getOpenStackGroups () { return Collections.unmodifiableList(openStackGroups); }
    public List<OpenStackPolicy> getOpenStackPolicies () { return Collections.unmodifiableList(openStackPolicies); }
    public List<OpenStackRole> getOpenStackRoles () { return Collections.unmodifiableList(openStackRoles); }

    /*Get list from OpenStackNetworkElements of NEUTRON*/
    public List<OpenStackNetwork> getOpenStackNetworks () { return Collections.unmodifiableList(openStackNetworks); }
    public List<OpenStackSubnet> getOpenStackSubnets () { return Collections.unmodifiableList(openStackSubnets); }
    public List<OpenStackRouter> getOpenStackRouters () { return Collections.unmodifiableList(openStackRouters); }
    public List<OpenStackPort> getOpenStackPorts () { return Collections.unmodifiableList(openStackPorts); }

    /*Get list from OpenStackNetworkElements of NOVA*/
    public List<OpenStackServer> getOpenStackServers () { return Collections.unmodifiableList(openStackServers); }
    public List<OpenStackFlavor> getOpenStackFlavor () { return Collections.unmodifiableList(openStackFlavors); }
    public List<OpenStackFloatingIp> getOpenStackFloatingIpDns () { return Collections.unmodifiableList(openStackFloatingIps); }
    public List<OpenStackKeypair> getOpenStackKeypairs () { return Collections.unmodifiableList(openStackKeypairs); }
    public List<OpenStackSecurityGroup> getOpenStackSecurityGroups () { return Collections.unmodifiableList(openStackSecurityGroups); }
    public List<OpenStackHostResource> getOpenStackHostResource () { return Collections.unmodifiableList(openStackHostResources); }

    /*Get list from OpenStackNetworkElements of GLANCE*/
    public List<OpenStackImageV2> getOpenStackImages () { return Collections.unmodifiableList(openStackImages); }

    /*Get list from OpenStackNetworkElements of CEILOMETER*/
    public List<OpenStackMeter> getOpenStackMeters () { return Collections.unmodifiableList(openStackMeters); }
    public List<OpenStackResource> getOpenStackResources () { return Collections.unmodifiableList(openStackResources); }
    public List<OpenStackGnocchiMeasure> getOpenStackMeasures () { return Collections.unmodifiableList(openStackMeasures); }

    public void doTopology(){

        // Node list
        final List<OpenStackRouter> routerList = getOpenStackRouters();
        final List<OpenStackNetwork> networkList = getOpenStackNetworks();
        final List<OpenStackSubnet> subnetList = getOpenStackSubnets();
        final List<OpenStackServer> serverList = getOpenStackServers();

        double index = 0.0;

        index = -routerList.size()*10/2;
        for(OpenStackRouter openStackRouter: routerList){
            openStackRouter.getNpNode().setXYPositionMap(new Point2D.Double(index,-20.0));
            index = index + 15;
        }
        index = -networkList.size()*10/2;
        for(OpenStackNetwork openStackNetwork: networkList){

            if(openStackNetwork.getName().equals("public")){
                openStackNetwork.getNpNode().setXYPositionMap(new Point2D.Double(0.0,0.0));

                    //openStackNetwork.getNpNode().setUrlNodeIcon(this.getNetPlanDesign().getNetworkLayerDefault(), new URL(getClass().getResource("/resources/gui/figs/nube.png").toURI().toURL().toString()));

            }else{
                openStackNetwork.getNpNode().setXYPositionMap(new Point2D.Double(index,-40.0));
                index = index + 15;
            }
            try {
                openStackNetwork.getNpNode().setUrlNodeIcon(this.getNetPlanDesign().getNetworkLayerDefault(), new URL("http://images.clipartpanda.com/cloud-icon-png-nTBXpg5Gc.png"));
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }


        }

        index = -subnetList.size()*10/2;
        for(OpenStackSubnet openStackSubnet: subnetList){

            if(openStackSubnet.getName().equals("public_subnet")){
                openStackSubnet.getNpNode().setXYPositionMap(new Point2D.Double(0.0,20.0));
                try {
                    openStackSubnet.getNpNode().setUrlNodeIcon(this.getNetPlanDesign().getNetworkLayerDefault(), new URL("https://cdn4.iconfinder.com/data/icons/Browsers_tatice/512/Globe.png"));
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            }else{
                openStackSubnet.getNpNode().setXYPositionMap(new Point2D.Double(index,-60.0));
                index = index + 15;
                try {
                    openStackSubnet.getNpNode().setUrlNodeIcon(this.getNetPlanDesign().getNetworkLayerDefault(), new URL("http://icons.iconarchive.com/icons/icons8/windows-8/256/Network-Switch-icon.png"));
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            }


        }

        index = -serverList.size()*10/2;
        for(OpenStackServer openStackServer: serverList){
            openStackServer.getNpNode().setXYPositionMap(new Point2D.Double(index,-80.0));
            index = index + 15;
            try {
                openStackServer.getNpNode().setUrlNodeIcon(this.getNetPlanDesign().getNetworkLayerDefault(), new URL("https://cdn2.iconfinder.com/data/icons/circle-icons-1/64/computer-128.png"));
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }

        for(OpenStackNetwork openStackNetwork: networkList){
            for(OpenStackSubnet openStackSubnet:subnetList){
                if(openStackNetwork.getId().equals(openStackSubnet.getSubnetNetworkId())){
                    this.getNetPlanDesign().addLink(openStackNetwork.getNpNode(),openStackSubnet.getNpNode(),20000,200000,20000,null);
                }
            }

        }

        for(OpenStackPort openStackPort: this.getOpenStackPorts()){
            for(OpenStackRouter openStackRouter : routerList){
                for(OpenStackNetwork openStackNetwork: networkList){
                    if(openStackNetwork.getId().equals(openStackPort.getPortNetworkId()) && openStackRouter.getId().equals(openStackPort.getPortDeviceId())){
                        this.getNetPlanDesign().addLink(openStackNetwork.getNpNode(),openStackRouter.getNpNode(),20000,200000,20000,null);
                    }
                }
            }
        }

        for(OpenStackServer openStackServer: this.getOpenStackServers()){
            for(OpenStackSubnet openStackSubnet: subnetList) {
                if(OpenStackUtils.belongsToThisNetwork(openStackServer.getServer(), openStackSubnet.getSubnet())){
                    this.getNetPlanDesign().addLink(openStackServer.getNpNode(),openStackSubnet.getNpNode(),20000,200000,20000,null);
                }
            }
        }


    }
}
