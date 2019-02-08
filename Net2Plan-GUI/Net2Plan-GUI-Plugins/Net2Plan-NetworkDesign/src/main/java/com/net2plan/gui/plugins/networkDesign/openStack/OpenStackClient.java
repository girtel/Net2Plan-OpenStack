package com.net2plan.gui.plugins.networkDesign.openStack;

import com.net2plan.gui.plugins.networkDesign.api.Cinder;
import com.net2plan.gui.plugins.networkDesign.api.Gnocchi;
import com.net2plan.gui.plugins.networkDesign.api.Keystone;
import com.net2plan.gui.plugins.networkDesign.openStack.blockstorage.OpenStackVolume;
import com.net2plan.gui.plugins.networkDesign.openStack.compute.*;
import com.net2plan.gui.plugins.networkDesign.openStack.extra.OpenStackSummary;
import com.net2plan.gui.plugins.networkDesign.openStack.identity.*;
import com.net2plan.gui.plugins.networkDesign.openStack.image.OpenStackImageV2;
import com.net2plan.gui.plugins.networkDesign.openStack.network.OpenStackNetwork;
import com.net2plan.gui.plugins.networkDesign.openStack.network.OpenStackPort;
import com.net2plan.gui.plugins.networkDesign.openStack.network.OpenStackRouter;
import com.net2plan.gui.plugins.networkDesign.openStack.network.OpenStackSubnet;
import com.net2plan.gui.plugins.networkDesign.openStack.telemetry.OpenStackGnocchiMeasure;
import com.net2plan.gui.plugins.networkDesign.openStack.telemetry.OpenStackMeter;
import com.net2plan.gui.plugins.networkDesign.openStack.telemetry.OpenStackResource;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.ViewEditTopologyTablesPane;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.AdvancedJTable_networkElement;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.blockstorage.AdvandecJTable_volume;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.compute.*;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.identity.*;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.image.AdvancedJTable_imagesV2;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.network.AdvancedJTable_networks;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.network.AdvancedJTable_ports;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.network.AdvancedJTable_routers;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.network.AdvancedJTable_subnets;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.telemetry.AdvancedJTable_resources;
import com.net2plan.gui.plugins.networkDesign.visualizationControl.VisualizationState;
import com.net2plan.gui.plugins.utils.OpenStackGraphCreator;

import com.net2plan.gui.plugins.utils.OpenStackUtils;
import com.net2plan.interfaces.networkDesign.NetPlan;
import com.net2plan.interfaces.networkDesign.NetworkLayer;
import com.net2plan.utils.Pair;
import org.apache.commons.collections15.BidiMap;
import org.jfree.date.AnnualDateRule;
import org.json.JSONArray;
import org.json.JSONObject;
import org.openstack4j.api.OSClient;
import org.openstack4j.model.common.Identifier;
import org.openstack4j.model.compute.*;
import org.openstack4j.model.identity.v3.*;
import org.openstack4j.model.network.Network;
import org.openstack4j.model.network.Port;
import org.openstack4j.model.network.Router;
import org.openstack4j.model.network.Subnet;
import org.openstack4j.model.storage.block.Volume;
import org.openstack4j.model.telemetry.Meter;
import org.openstack4j.model.telemetry.Resource;
import org.openstack4j.openstack.OSFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

import static edu.emory.mathcs.utils.ConcurrencyUtils.submit;

public class OpenStackClient {

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
    public final List<OpenStackRule> openStackRules = new ArrayList<> ();

    /*List of OpenStackNetworkElements of GLANCE*/
    public final List<OpenStackImageV2> openStackImages = new ArrayList<> ();

    public final List<OpenStackVolume> openStackVolumes = new ArrayList<> ();

    /*List of OpenStackNetworkElements of CEILOMETER*/
    public final List<OpenStackMeter> openStackMeters = new ArrayList<> ();
    public final List<OpenStackResource> openStackResources = new ArrayList<> ();
    public List<OpenStackMeter> openStackMetersAvailable = new ArrayList<>();
    public final List<OpenStackGnocchiMeasure> openStackMeasures = new ArrayList<>();

    /*Extra*/
    public final List<OpenStackSummary> openStackSummaries= new ArrayList<>();

    private Gnocchi gnocchi;
    private Cinder cinder;
    public Keystone keystone;
    private OpenStackNet openStackNet;
    private Token token;
    private NetPlan netPlan;

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
    private Boolean isAdmin=false;

    public OpenStackClient (){
        this.openStackNet= new OpenStackNet();
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
                    .credentials(os_username,os_password,Identifier.byName(os_user_domain_name))
                    .scopeToProject(Identifier.byId(os_project_id))
                    .authenticate();

            //System.out.println(os_project_id);
            this.os=os;
            this.token = os.getToken();
            this.name = name;
            //System.out.println(token);
            this.openStackNet = osn;
            this.openStackNetCreate = new OpenStackNetCreate(this);
            this.openStackNetDelete = new OpenStackNetDelete(this);

            this.netPlan = new NetPlan();

            prepareOwnApisForClient();

            this.isAdmin = checkIfThisOpenStackClientIsAdmin();
            this.connect = true;

        }catch (Exception ex){
            ex.printStackTrace();
        }
        return this;
    }

    private void prepareOwnApisForClient(){

        if(checkIfThisOpenStackClientIsAdmin()) {
            List<Service> services = this.os.identity().serviceEndpoints().list().stream().filter(n -> ((Service) n).getName().equals("gnocchi")).collect(Collectors.toList());

            if (services.size() >= 1) {

                Service service = services.get(0);
                Endpoint endpoint = this.os.identity().serviceEndpoints().listEndpoints().stream().filter(n -> ((Endpoint) n).getServiceId().equals(service.getId())).collect(Collectors.toList()).get(0);
                this.gnocchi = new Gnocchi(endpoint.getUrl().toString() + "/v1/", this.os);

            }

            List<Service> services2 = this.os.identity().serviceEndpoints().list().stream().filter(n -> ((Service) n).getName().equals("cinderv3")).collect(Collectors.toList());

            if (services2.size() >= 1) {

                Service service = services2.get(0);
                Endpoint endpoint = this.os.identity().serviceEndpoints().listEndpoints().stream().filter(n -> ((Endpoint) n).getServiceId().equals(service.getId())).collect(Collectors.toList()).get(0);
                this.cinder = new Cinder(endpoint.getUrl().toString(), this.os);

            }
            this.keystone = new Keystone(os_auth_url, this.os);

        }
    }

    public OpenStackClient clearClientListsAndTopology(){

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
        openStackRules.clear();

        /*Clear Glance list*/
        openStackImages.clear();

        openStackVolumes.clear();

        /*Clear Ceilometer list*/
        openStackResources.clear();



        netPlan.removeAllNodes();
        return this;
    }
    public OpenStackClient fillClientListsAndTopology(){

        updateClient();

       if(isAdmin) {

           setAdminClientConfiguration();

       }else{

           setNonAdminClientConfiguration();
       }
        return this;
    }

    private void setAdminClientConfiguration(){
        try {
            /* Get elements of Identity(Keystone)*/
            this.os.identity().users().list().forEach(n -> addOpenStackUser(n));
            this.os.identity().domains().list().forEach(n -> addOpenStackDomain(n));
            this.os.identity().serviceEndpoints().listEndpoints().forEach(n -> addOpenStackEndpoint(n));
            this.os.identity().serviceEndpoints().list().forEach(n -> addOpenStackService(n));
            this.os.identity().regions().list().forEach(n -> addOpenStackRegion(n));
            this.os.identity().credentials().list().forEach(n -> addOpenStackCredential(n));
            this.os.identity().groups().list().forEach(n -> addOpenStackGroup(n));
            this.os.identity().policies().list().forEach(n -> addOpenStackPolicy(n));
            this.os.identity().roles().list().forEach(n -> addOpenStackRole(n));
            this.keystone.projectList().stream().forEach(n -> addOpenStackProject(n));

            /* Get elements of Network(NEUTRON)*/
            this.os.networking().network().list().forEach(n -> addOpenStackNetwork(n));
            this.os.networking().subnet().list().forEach(n -> addOpenStackSubnet(n));
            this.os.networking().router().list().stream().forEach(n -> addOpenStackRouter(n));
            this.os.networking().port().list().stream().forEach(n -> addOpenStackPort(n));

            /*Get elements of Compute(NOVA)*/
            this.os.compute().servers().listAll(true).stream().forEach(n -> addOpenStackServer(n));
            this.os.compute().flavors().list().stream().forEach(n -> addOpenStackFlavor(n));
            this.os.compute().floatingIps().list().forEach(n -> addOpenStackFloatingIP(n));
            this.os.compute().keypairs().list().stream().forEach(n -> addOpenStackKeypair(n));
            this.os.compute().securityGroups().list().stream().forEach(n -> addOpenStackSecurityGroup(n));

            addRules();

            final List<? extends HostResource> hosts = os.compute().host().list();

            for (HostResource hostResource : hosts) {
                if (hostResource.getService().equals("compute"))
                    this.os.compute().host().hostDescribe(hostResource.getHostName()).stream().filter(x -> ((HostResource) x).getProject().equals("(total)")).forEach(p -> addOpenStackHostResource(p));
            }

            /*Get elements of Image(GLANCE)*/
            this.os.imagesV2().list().stream().forEach(n -> addOpenStackImage(n));


            if (cinder != null) {
                this.cinder.volumeList().forEach(n-> addOpenStackVolume(n));
            }
            /*Get elements of Telmetry(Ceilometer)*/
            if (gnocchi != null)
                gnocchi.resourcesList().forEach(n -> addOpenStackResource(n));

            doTopology();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    private void setNonAdminClientConfiguration(){

        try {

            addOpenStackProject(this.os.getToken().getProject());
            addOpenStackUser(this.os.getToken().getUser());
            addOpenStackRole(this.os.getToken().getRoles().get(0));

            /* Get elements of Network(NEUTRON)*/
            this.os.networking().network().list().forEach(n -> addOpenStackNetwork(n));
            this.os.networking().subnet().list().forEach(n -> addOpenStackSubnet(n));
            this.os.networking().router().list().stream().forEach(n -> addOpenStackRouter(n));
            this.os.networking().port().list().stream().forEach(n -> addOpenStackPort(n));

            /*Get elements of Compute(NOVA)*/
            this.os.compute().servers().list().stream().forEach(n -> addOpenStackServer(n));
            this.os.compute().flavors().list().stream().forEach(n -> addOpenStackFlavor(n));
            this.os.compute().floatingIps().list().forEach(n -> addOpenStackFloatingIP(n));
            this.os.compute().keypairs().list().stream().forEach(n -> addOpenStackKeypair(n));
            this.os.compute().securityGroups().list().stream().forEach(n -> addOpenStackSecurityGroup(n));

            this.os.imagesV2().list().stream().forEach(n -> addOpenStackImage(n));
            doTopology();
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private boolean checkIfThisOpenStackClientIsAdmin(){

        boolean isAdmin = false;

        List<? extends Role> roles = this.os.getToken().getRoles();

        List<Role> role = roles.stream().filter(n->((Role) n).getName().equals("admin")).collect(Collectors.toList());

        if(role.size()>0) isAdmin=true;

        return isAdmin;

    }
    public boolean isThisClientAdmin(){return this.isAdmin;}


    public void updateThisList (AdvancedJTable_networkElement advancedJTable_networkElement){

        updateClient();

        if(isAdmin) {
            updateThisListAdmin(advancedJTable_networkElement);
        }else {
            updateThisListNonAdmin(advancedJTable_networkElement);
            }

    }
    public void updateThisListAdmin(AdvancedJTable_networkElement advancedJTable_networkElement){
        if (advancedJTable_networkElement instanceof AdvancedJTable_users) {

            openStackUsers.clear();
            this.os.identity().users().list().forEach(n -> addOpenStackUser(n));

        } else if (advancedJTable_networkElement instanceof AdvancedJTable_projects) {

            openStackProjects.clear();
            this.keystone.projectList().stream().forEach(n -> addOpenStackProject(n));
            doTopology();
            this.openStackNet.getCallback().updateVisualizationAfterNewTopology();

        } else if (advancedJTable_networkElement instanceof AdvancedJTable_domains) {

            openStackDomains.clear();
            this.os.identity().domains().list().forEach(n -> addOpenStackDomain(n));

        } else if (advancedJTable_networkElement instanceof AdvancedJTable_endpoints) {

            openStackEndpoints.clear();
            this.os.identity().serviceEndpoints().listEndpoints().forEach(n -> addOpenStackEndpoint(n));

        } else if (advancedJTable_networkElement instanceof AdvancedJTable_services) {

            openStackServices.clear();
            this.os.identity().serviceEndpoints().list().forEach(n -> addOpenStackService(n));

        } else if (advancedJTable_networkElement instanceof AdvancedJTable_regions) {

            openStackRegions.clear();
            this.os.identity().regions().list().forEach(n -> addOpenStackRegion(n));

        } else if (advancedJTable_networkElement instanceof AdvancedJTable_credentials) {

            openStackCredentials.clear();
            this.os.identity().credentials().list().forEach(n -> addOpenStackCredential(n));

        } else if (advancedJTable_networkElement instanceof AdvancedJTable_groups) {

            openStackGroups.clear();
            this.os.identity().groups().list().forEach(n -> addOpenStackGroup(n));

        } else if (advancedJTable_networkElement instanceof AdvancedJTable_policies) {

            openStackPolicies.clear();
            this.os.identity().policies().list().forEach(n -> addOpenStackPolicy(n));

        } else if (advancedJTable_networkElement instanceof AdvancedJTable_roles) {

            openStackRoles.clear();
            this.os.identity().roles().list().forEach(n -> addOpenStackRole(n));

        } else if (advancedJTable_networkElement instanceof AdvancedJTable_networks || advancedJTable_networkElement instanceof AdvancedJTable_subnets || advancedJTable_networkElement instanceof AdvancedJTable_routers || advancedJTable_networkElement instanceof AdvancedJTable_ports || advancedJTable_networkElement instanceof AdvancedJTable_servers) {

            openStackNetworks.clear();
            openStackSubnets.clear();
            openStackRouters.clear();
            openStackPorts.clear();
            openStackServers.clear();

            netPlan.removeAllNodes();

            this.os.networking().network().list().forEach(n -> addOpenStackNetwork(n));
            this.os.networking().subnet().list().forEach(n -> addOpenStackSubnet(n));
            this.os.networking().router().list().forEach(n -> addOpenStackRouter(n));
            this.os.networking().port().list().forEach(n -> addOpenStackPort(n));


            this.os.compute().servers().listAll(true).forEach(n -> addOpenStackServer(n));

            doTopology();
            final VisualizationState vs = this.openStackNet.getCallback().getVisualizationState();
            Pair<BidiMap<NetworkLayer, Integer>, Map<NetworkLayer, Boolean>> res =
                    vs.suggestCanvasUpdatedVisualizationLayerInfoForNewDesign(new HashSet<>(this.openStackNet.getCallback().getDesign().getNetworkLayers()));
            vs.setCanvasLayerVisibilityAndOrder(this.openStackNet.getCallback().getDesign(), res.getFirst(), res.getSecond());
            this.openStackNet.getCallback().updateVisualizationAfterNewTopology();
            //osn.getCallback().updateVisualizationAfterNewTopology();
            openStackNet.getCallback().getViewEditTopTables().updateViewForDeterminateAjtableAndOpenStackClient(Arrays.asList(ViewEditTopologyTablesPane.AJTableType.NETWORKS, ViewEditTopologyTablesPane.AJTableType.SUBNETS,ViewEditTopologyTablesPane.AJTableType.ROUTERS,ViewEditTopologyTablesPane.AJTableType.PORTS,ViewEditTopologyTablesPane.AJTableType.SERVERS),this);

        } else if (advancedJTable_networkElement instanceof AdvancedJTable_flavors) {

            openStackFlavors.clear();
            this.os.compute().flavors().list().forEach(n -> addOpenStackFlavor(n));

        } else if (advancedJTable_networkElement instanceof AdvancedJTable_floatingIp) {

            openStackFloatingIps.clear();
            this.os.compute().floatingIps().list().forEach(n -> addOpenStackFloatingIP(n));

        } else if (advancedJTable_networkElement instanceof AdvancedJTable_keypairs) {

            openStackKeypairs.clear();
            this.os.compute().keypairs().list().forEach(n -> addOpenStackKeypair(n));

        } else if (advancedJTable_networkElement instanceof AdvancedJTable_securityGroups  || advancedJTable_networkElement instanceof AdvancedJTable_rules) {

            openStackSecurityGroups.clear();
            this.os.compute().securityGroups().list().forEach(n -> addOpenStackSecurityGroup(n));
            addRules();
            openStackNet.getCallback().getViewEditTopTables().updateViewForDeterminateAjtableAndOpenStackClient(Arrays.asList(ViewEditTopologyTablesPane.AJTableType.SECURITYGROUPS, ViewEditTopologyTablesPane.AJTableType.RULES),this);

        } else if (advancedJTable_networkElement instanceof AdvancedJTable_hostResources) {

            openStackHostResources.clear();
            final List<? extends HostResource> hosts = os.compute().host().list();

            for (HostResource hostResource : hosts) {
                if (hostResource.getService().equals("compute"))
                    this.os.compute().host().hostDescribe(hostResource.getHostName()).stream().filter(x -> ((HostResource) x).getProject().equals("(total)")).forEach(p -> addOpenStackHostResource(p));
            }

        } else if (advancedJTable_networkElement instanceof AdvancedJTable_imagesV2) {

            openStackImages.clear();
            /*Get elements of Image(GLANCE)*/
            this.os.imagesV2().list().stream().forEach(n -> addOpenStackImage(n));


        } else if (advancedJTable_networkElement instanceof AdvandecJTable_volume) {

            openStackVolumes.clear();

            if (cinder != null) {
                this.cinder.volumeList().forEach(n-> addOpenStackVolume(n));
            }


        } else if (advancedJTable_networkElement instanceof AdvancedJTable_resources) {

            openStackResources.clear();
            if (gnocchi != null)
                gnocchi.resourcesList().forEach(n -> addOpenStackResource(n));


        } else if (advancedJTable_networkElement instanceof AdvancedJTable_quotas || advancedJTable_networkElement instanceof AdvancedJTable_quotasUsage || advancedJTable_networkElement instanceof AdvancedJTable_limits){

            openStackNet.fillSlicingTabTablesOfNet();
        }
    }
    public void updateThisListNonAdmin(AdvancedJTable_networkElement advancedJTable_networkElement){

         if (advancedJTable_networkElement instanceof AdvancedJTable_networks || advancedJTable_networkElement instanceof AdvancedJTable_subnets || advancedJTable_networkElement instanceof AdvancedJTable_routers || advancedJTable_networkElement instanceof AdvancedJTable_ports || advancedJTable_networkElement instanceof AdvancedJTable_servers) {

            openStackNetworks.clear();
            openStackSubnets.clear();
            openStackRouters.clear();
            openStackPorts.clear();
            openStackServers.clear();

            netPlan.removeAllNodes();

            this.os.networking().network().list().forEach(n -> addOpenStackNetwork(n));
            this.os.networking().subnet().list().forEach(n -> addOpenStackSubnet(n));
            this.os.networking().router().list().forEach(n -> addOpenStackRouter(n));
            this.os.networking().port().list().forEach(n -> addOpenStackPort(n));


            this.os.compute().servers().list().forEach(n -> addOpenStackServer(n));

            doTopology();

            final VisualizationState vs = this.openStackNet.getCallback().getVisualizationState();
            Pair<BidiMap<NetworkLayer, Integer>, Map<NetworkLayer, Boolean>> res =
                    vs.suggestCanvasUpdatedVisualizationLayerInfoForNewDesign(new HashSet<>(this.openStackNet.getCallback().getDesign().getNetworkLayers()));
            vs.setCanvasLayerVisibilityAndOrder(this.openStackNet.getCallback().getDesign(), res.getFirst(), res.getSecond());
            this.openStackNet.getCallback().updateVisualizationAfterNewTopology();

        } else if (advancedJTable_networkElement instanceof AdvancedJTable_flavors) {

            openStackFlavors.clear();
            this.os.compute().flavors().list().forEach(n -> addOpenStackFlavor(n));

        } else if (advancedJTable_networkElement instanceof AdvancedJTable_floatingIp) {

            openStackFloatingIps.clear();
            this.os.compute().floatingIps().list().forEach(n -> addOpenStackFloatingIP(n));

        } else if (advancedJTable_networkElement instanceof AdvancedJTable_keypairs) {

            openStackKeypairs.clear();
            this.os.compute().keypairs().list().forEach(n -> addOpenStackKeypair(n));

        } else if (advancedJTable_networkElement instanceof AdvancedJTable_securityGroups || advancedJTable_networkElement instanceof AdvancedJTable_rules) {

            openStackSecurityGroups.clear();
            this.os.compute().securityGroups().list().forEach(n -> addOpenStackSecurityGroup(n));
             addRules();
             openStackNet.getCallback().getViewEditTopTables().updateViewForDeterminateAjtableAndOpenStackClient(Arrays.asList(ViewEditTopologyTablesPane.AJTableType.SECURITYGROUPS, ViewEditTopologyTablesPane.AJTableType.RULES),this);


         } else if (advancedJTable_networkElement instanceof AdvancedJTable_imagesV2) {

            openStackImages.clear();
            /*Get elements of Image(GLANCE)*/
            this.os.imagesV2().list().stream().forEach(n -> addOpenStackImage(n));
        }else if (advancedJTable_networkElement instanceof AdvancedJTable_quotas || advancedJTable_networkElement instanceof AdvancedJTable_quotasUsage || advancedJTable_networkElement instanceof AdvancedJTable_limits){

             openStackNet.fillSlicingTabTablesOfNet();
         }
    }


    public OpenStackGraphCreator getListAndGraphFromMeasuresFromMetric(OpenStackMeter openStackMeter){

        openStackSummaries.clear();
        openStackMeasures.clear();

        final String metric_id = openStackMeter.getId();

        final JSONArray jsonArray = this.gnocchi.measuresList(metric_id);

        final OpenStackGraphCreator openStackGraphCreator = new OpenStackGraphCreator(this.openStackNet.getCallback(),this);
        final double[] values = new double[jsonArray.length()];
        final String panelTitle ="Graph of "+openStackMeter.getName()+" measurements";
        final String panelYAxisTitle = openStackMeter.getMeter_unit();

        if(jsonArray.length() > 0 ) {
            jsonArray.forEach(n -> {
                OpenStackGnocchiMeasure openStackGnocchiMeasure = OpenStackGnocchiMeasure.createFromAddMeasure(this.openStackNet, ((JSONArray) n).get(0).toString(), ((JSONArray) n).get(1).toString(), ((JSONArray) n).get(2).toString(), this);
                if(!openStackMeasures.contains(openStackGnocchiMeasure))
                    openStackMeasures.add(openStackGnocchiMeasure);
            });

            for (int i = 0; i < values.length; i++) {

                values[i] = (double) ((JSONArray) jsonArray.get(i)).get(2);
            }

            openStackSummaries.clear();

            OpenStackSummary openStackSummary = OpenStackSummary.createFromAddSummary(this.openStackNet,metric_id , values, this);

            if(!openStackSummaries.contains(openStackSummary))
                openStackSummaries.add(openStackSummary);

        }
        openStackGraphCreator.createPanel(panelTitle,panelYAxisTitle,values);


        return openStackGraphCreator;
    }
    public Pair<JPanel,Map<String,Object>> getAnalisisData(String metric_id){

        openStackMeters.clear();
        openStackMeasures.clear();
        openStackSummaries.clear();

        Map<String,Object> information = new HashMap<>();
        JPanel grafica = new JPanel();

        addOpenStackMeter(gnocchi.meter(metric_id));

        OpenStackMeter openStackMeter = openStackMeters.get(0);

        if(openStackMeter !=null) {
            if (openStackMeters.size() > 0) {
                information.put("ID", openStackMeter.getId());
                information.put("Name", openStackMeter.getName());
                information.put("Unit", openStackMeter.getMeter_unit());
                information.put("Project ID", openStackMeter.getMeter_project_id());
                //information.put("Resource ID", openStackMeter.getMeter_resource_id());
                information.put("Type", openStackMeter.getMeter_type());
                information.put("User ID", openStackMeter.getMeter_user_id());
            }


            if (getListAndGraphFromMeasuresFromMetric(openStackMeter) != null)
                grafica = getListAndGraphFromMeasuresFromMetric(openStackMeter).getPanel();
        }

        return Pair.of(grafica,information);



    }
    public String getName(){return this.name;}
    public String getProjectId(){return this.os_project_id;}
    public Boolean isConnected(){return this.connect;}

    public OSClient.OSClientV3 getClient(){
        updateClient();
        return this.os;
    }
    private OpenStackClient updateClient(){
        this.os = OSFactory.clientFromToken(token);
        return this;
    }
    public OpenStackNetCreate getOpenStackNetCreate(){ updateClient(); return this.openStackNetCreate; }
    public OpenStackNetDelete getOpenStackNetDelete(){ updateClient(); return this.openStackNetDelete; }
    public OpenStackNet getOpenStackNet (){return  this.openStackNet;}
    public Token getToken (){return this.token;}
    public NetPlan getNetPlanDesign(){return this.netPlan;}

    public void addRules(){
        openStackRules.clear();
        openStackSecurityGroups.stream().forEach(n->n.getSecGroupExtensionRules().stream().forEach(x->addOpenStackRule(x)));
    }
    /* Add OpenStackNetworkElements of Keystone*/
    public OpenStackUser addOpenStackUser (User user){
       final OpenStackUser res = OpenStackUser.createFromAddUser(this.openStackNet ,user,this);
        if(openStackUsers.contains(res)) return res;
        openStackUsers.add(res);
        return res;
    }
    public OpenStackProject addOpenStackProject(Project project){
        //System.out.println(project.getId());
        final OpenStackProject res = OpenStackProject.createFromAddProject(this.openStackNet,project,this);
        if(openStackProjects.contains(res)) return res;
        openStackProjects.add(res);
        return res;
    }
    public OpenStackDomain addOpenStackDomain(Domain domain) {
        final OpenStackDomain res = OpenStackDomain.createFromAddDomain(this.openStackNet,domain,this);
        if(openStackDomains.contains(res)) return res;
        openStackDomains.add(res);
        return res;
    }
    public OpenStackEndpoint addOpenStackEndpoint(Endpoint endpoint) {
        final OpenStackEndpoint res = OpenStackEndpoint.createFromAddEndpoint(this.openStackNet,endpoint ,this);
        if(openStackEndpoints.contains(res)) return res;
        openStackEndpoints.add(res);
        return res;
    }
    public OpenStackService addOpenStackService(Service service) {
        final OpenStackService res = OpenStackService.createFromAddService(this.openStackNet,service,this);
        if(openStackServices.contains(res)) return res;
        openStackServices.add(res);
        return res;
    }
    public OpenStackRegion addOpenStackRegion(Region region) {
        final OpenStackRegion res = OpenStackRegion.createFromAddRegion(this.openStackNet,region,this);
        if(openStackRegions.contains(res)) return res;
        openStackRegions.add(res);
        return res;
    }
    public OpenStackCredential addOpenStackCredential(Credential credential) {
        final OpenStackCredential res = OpenStackCredential.createFromAddCredential(this.openStackNet,credential,this);
        if(openStackCredentials.contains(res)) return res;
        openStackCredentials.add(res);
        return res;
    }
    public OpenStackGroup addOpenStackGroup(Group group) {
        final OpenStackGroup res = OpenStackGroup.createFromAddGroup(this.openStackNet,group,this);
        if(openStackGroups.contains(res)) return res;
        openStackGroups.add(res);
        return res;
    }
    public OpenStackPolicy addOpenStackPolicy(Policy policy) {
        final OpenStackPolicy res = OpenStackPolicy.createFromAddPolicy(this.openStackNet,policy,this);
        if(openStackPolicies.contains(res)) return res;
        openStackPolicies.add(res);
        return res;
    }
    public OpenStackRole addOpenStackRole(Role role) {
        final OpenStackRole res = OpenStackRole.createFromAddRole(this.openStackNet,role,this);
        if(openStackRoles.contains(res)) return res;
        openStackRoles.add(res);
        return res;
    }

    /* Add OpenStackNetworkElements of Neutron*/
    public OpenStackNetwork addOpenStackNetwork(Network network) {
        final OpenStackNetwork res = OpenStackNetwork.createFromAddNetwork(this.openStackNet ,network,this);
        if(openStackNetworks.contains(res)) return res;
        openStackNetworks.add(res);
        return res;
    }
    public OpenStackSubnet addOpenStackSubnet (Subnet subnet) {
        final OpenStackSubnet res = OpenStackSubnet.createFromAddSubnet(this.openStackNet,subnet,this);
        if(openStackSubnets.contains(res)) return res;
        openStackSubnets.add(res);
        return res;
    }
    public OpenStackRouter addOpenStackRouter(Router router) {
        final OpenStackRouter res = OpenStackRouter.createFromAddRouter(this.openStackNet,router,this);
        if(openStackRouters.contains(res)) return res;
        openStackRouters.add(res);
        return res;
    }
    public OpenStackPort addOpenStackPort(Port port) {
        final OpenStackPort res = OpenStackPort.createFromAddPort(this.openStackNet,port,this);
        if(openStackPorts.contains(res)) return res;
        openStackPorts.add(res);
        return res;
    }

    /* Add OpenStackNetworkElements of Nova*/
    public OpenStackServer addOpenStackServer(Server server) {
        final OpenStackServer res = OpenStackServer.createFromAddServer(this.openStackNet,server,this);
        if(openStackServers.contains(res)) return res;
        openStackServers.add(res);
        return res;
    }
    public OpenStackFlavor addOpenStackFlavor(Flavor flavor) {
        final OpenStackFlavor res = OpenStackFlavor.createFromAddFlavor(this.openStackNet,flavor,this);
        if(openStackFlavors.contains(res)) return res;
        openStackFlavors.add(res);
        return res;
    }
    public OpenStackFloatingIp addOpenStackFloatingIP(FloatingIP floatingIP) {
        final OpenStackFloatingIp res = OpenStackFloatingIp.createFromAddFloatingIp(this.openStackNet,floatingIP,this);
        if(openStackFloatingIps.contains(res)) return res;
        openStackFloatingIps.add(res);
        return res;
    }
    public OpenStackKeypair addOpenStackKeypair(Keypair keypair){

        final OpenStackKeypair res = OpenStackKeypair.createFromAddKeypair(this.openStackNet,keypair,this);
        if(openStackKeypairs.contains(res)) return res;
        openStackKeypairs.add(res);
        return res;
    }
    public OpenStackSecurityGroup addOpenStackSecurityGroup(SecGroupExtension securityGroup) {
        final OpenStackSecurityGroup res = OpenStackSecurityGroup.createFromAddSegurityGroup(this.openStackNet,securityGroup,this);
        if(openStackSecurityGroups.contains(res)) return res;
        openStackSecurityGroups.add(res);
        return res;
    }
    public OpenStackHostResource addOpenStackHostResource(HostResource hostResource) {
        final OpenStackHostResource res = OpenStackHostResource.createFromAddHostResource(this.openStackNet,hostResource,this);
        if(openStackHostResources.contains(res)) return res;
        openStackHostResources.add(res);

        return res;
    }
    public OpenStackRule addOpenStackRule(SecGroupExtension.Rule rule) {
        final OpenStackRule res = OpenStackRule.createFromAddRule(this.openStackNet,rule,this);
        if(openStackRules.contains(res)) return res;
        openStackRules.add(res);

        return res;
    }

    /* Add OpenStackNetworkElements of Glance*/
    public OpenStackImageV2 addOpenStackImage(org.openstack4j.model.image.v2.Image image) {
        final OpenStackImageV2 res = OpenStackImageV2.createFromAddImageV2(this.openStackNet,image,this);
        if(openStackImages.contains(res)) return res;
        openStackImages.add(res);
        return res;
    }

    /* Add OpenStackNetworkElements of Ceilometer*/
    public OpenStackMeter addOpenStackMeter(Meter meter) {
        final OpenStackMeter res = OpenStackMeter.createFromAddMeter(this.openStackNet ,meter,this);
        if(openStackMeters.contains(res)) return res;
        openStackMeters.add(res);
        return res;
    }
    public OpenStackResource addOpenStackResource(Resource resource) {
        final OpenStackResource res = OpenStackResource.createFromAddResource(this.openStackNet ,resource,this);
        if(openStackResources.contains(res)) return res;
        openStackResources.add(res);
        return res;
    }
    public OpenStackVolume addOpenStackVolume (Volume volume){
        final OpenStackVolume res = OpenStackVolume.createFromAddVolume(this.openStackNet ,volume,this);
        if(openStackVolumes.contains(res)) return res;
        openStackVolumes.add(res);
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
    public List<OpenStackRule> getOpenStackRules () { return Collections.unmodifiableList(openStackRules); }

    /*Get list from OpenStackNetworkElements of GLANCE*/
    public List<OpenStackImageV2> getOpenStackImages () { return Collections.unmodifiableList(openStackImages); }

    public List<OpenStackVolume> getOpenStackVolumes () { return Collections.unmodifiableList(openStackVolumes); }

    /*Get list from OpenStackNetworkElements of CEILOMETER*/
    public List<OpenStackMeter> getOpenStackMeters () { return Collections.unmodifiableList(openStackMeters); }
    public List<OpenStackResource> getOpenStackResources () { return Collections.unmodifiableList(openStackResources); }
    public List<OpenStackGnocchiMeasure> getOpenStackMeasures () { return Collections.unmodifiableList(openStackMeasures); }
    public List<OpenStackSummary> getOpenStackSummmaries () { return Collections.unmodifiableList(openStackSummaries); }

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
            try {
                openStackRouter.getNpNode().setUrlNodeIcon(this.getNetPlanDesign().getNetworkLayerDefault(), new URL("https://cdn0.iconfinder.com/data/icons/network-database-1/65/34-512.png"));
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
        index = -networkList.size()*10/2;
        double index2= 0;
        for(OpenStackNetwork openStackNetwork: networkList){

            if(openStackNetwork.isNetworkIsRouterExternal()){

                openStackNetwork.getNpNode().setXYPositionMap(new Point2D.Double(index2,0));

                index2 += 20;

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
        index2 = 0;
        for(OpenStackSubnet openStackSubnet: subnetList){

            if(openStackNetworks.stream().filter(x->x.getId().equals(openStackSubnet.getSubnetNetworkId())).findFirst().get().isNetworkIsRouterExternal()){
                openStackSubnet.getNpNode().setXYPositionMap(new Point2D.Double(index2,20.0));
                index2+=20;
                try {
                    openStackSubnet.getNpNode().setUrlNodeIcon(this.getNetPlanDesign().getNetworkLayerDefault(), new URL("https://cdn0.iconfinder.com/data/icons/cloud-computing-20/48/32-128.png"));
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            }else{
                openStackSubnet.getNpNode().setXYPositionMap(new Point2D.Double(index,-60.0));
                index = index + 15;
                try {
                    openStackSubnet.getNpNode().setUrlNodeIcon(this.getNetPlanDesign().getNetworkLayerDefault(), new URL("http://icons.iconarchive.com/icons/icons8/android/256/Network-Cloud-Storage-icon.png"));
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
                openStackServer.getNpNode().setUrlNodeIcon(this.getNetPlanDesign().getNetworkLayerDefault(), new URL("http://icons.iconarchive.com/icons/icons8/windows-8/256/Network-Virtual-Machine-2-icon.png"));
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }

        Map<String, String> colores = getColores() ;
        for(OpenStackNetwork openStackNetwork: networkList){
            for(OpenStackSubnet openStackSubnet:subnetList){
                if(openStackNetwork.getId().equals(openStackSubnet.getSubnetNetworkId())){
                    Map<String,String> attributes = new HashMap<>();
                    attributes.put("Network ",openStackNetwork.getName());
                    attributes.put("Subnet ",openStackSubnet.getName());
                    attributes.put("Project",getOpenStackProjects().stream().filter(n->n.getId().equals(openStackSubnet.getSubnetTenantId())).findFirst().get().getProjectName());
                    attributes.put("Color",colores.get(openStackNetwork.getNetworkTenantId()));
                    this.getNetPlanDesign().addLinkBidirectional(openStackNetwork.getNpNode(),openStackSubnet.getNpNode(),20000,200000,20000,attributes);
                }
            }

        }

        for(OpenStackPort openStackPort: this.getOpenStackPorts()){
            for(OpenStackRouter openStackRouter : routerList){
                for(OpenStackNetwork openStackNetwork: networkList){
                    if(openStackNetwork.getId().equals(openStackPort.getPortNetworkId()) && openStackRouter.getId().equals(openStackPort.getPortDeviceId())){
                        Map<String,String> attributes = new HashMap<>();
                        attributes.put("Color",colores.get(openStackRouter.getRouterTenantId()));
                        attributes.put("Network ",openStackNetwork.getName());
                        attributes.put("Router ",openStackRouter.getRouterName());
                        attributes.put("Project",getOpenStackProjects().stream().filter(n->n.getId().equals(openStackRouter.getRouterTenantId())).findFirst().get().getProjectName());
                        this.getNetPlanDesign().addLinkBidirectional(openStackNetwork.getNpNode(),openStackRouter.getNpNode(),20000,200000,20000,attributes);
                    }
                }
            }
        }

        for(OpenStackServer openStackServer: this.getOpenStackServers()){
            for(OpenStackSubnet openStackSubnet: subnetList) {
                if(OpenStackUtils.belongsToThisNetwork(openStackServer.getServer(), openStackSubnet.getSubnet())  && openStackSubnet.getSubnetTenantId().equals(openStackServer.getServerTenantId())){
                    Map<String,String> attributes = new HashMap<>();
                    attributes.put("Color",colores.get(openStackSubnet.getSubnetTenantId()));
                    attributes.put("Subnet ",openStackSubnet.getName());
                    attributes.put("Server ",openStackServer.getServerName());
                    attributes.put("Project",getOpenStackProjects().stream().filter(n->n.getId().equals(openStackSubnet.getSubnetTenantId())).findFirst().get().getProjectName());
                    this.getNetPlanDesign().addLinkBidirectional(openStackServer.getNpNode(),openStackSubnet.getNpNode(),20000,200000,20000,attributes);
                }
            }
        }


    }

    public Map<String, String> getColores(){

        Map<String,String> colores = new HashMap<>();
        Random random = new Random(77);
        for(OpenStackProject openStackProject: getOpenStackProjects()){
            colores.put(openStackProject.getId(),"#" +Integer.toHexString(random.nextInt(255))+Integer.toHexString(random.nextInt(255))+Integer.toHexString(random.nextInt(255)));
            openStackProject.setColor(Color.decode(colores.get(openStackProject.getId())));
        }
        //System.out.println(colores);
        return colores;

    }



}
