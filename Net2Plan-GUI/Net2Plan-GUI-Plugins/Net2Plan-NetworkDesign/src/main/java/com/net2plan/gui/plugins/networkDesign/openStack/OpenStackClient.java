package com.net2plan.gui.plugins.networkDesign.openStack;

import com.google.common.collect.Lists;
import com.net2plan.gui.plugins.networkDesign.openStack.compute.*;
import com.net2plan.gui.plugins.networkDesign.openStack.network.OpenStackNetwork;
import com.net2plan.gui.plugins.networkDesign.openStack.network.OpenStackPort;
import com.net2plan.gui.plugins.networkDesign.openStack.network.OpenStackRouter;
import com.net2plan.gui.plugins.networkDesign.openStack.network.OpenStackSubnet;
import com.net2plan.gui.plugins.utils.MyRunnable;
import org.json.JSONObject;
import org.openstack4j.api.OSClient;
import org.openstack4j.api.types.Facing;
import org.openstack4j.model.common.Identifier;
import org.openstack4j.model.compute.*;
import org.openstack4j.model.identity.v3.Token;
import org.openstack4j.model.network.Network;
import org.openstack4j.model.network.Port;
import org.openstack4j.model.network.Router;
import org.openstack4j.model.network.Subnet;
import org.openstack4j.openstack.OSFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static edu.emory.mathcs.utils.ConcurrencyUtils.submit;

public class OpenStackClient {

    private String os_auth_url;
    private String os_username;
    private String os_password;
    private String os_user_domain_name;
    private String os_project_id;

    private OSClient.OSClientV3 os;
    private OpenStackNetCreate openStackNetCreate;
    private OpenStackNetDelete openStackNetDelete;

    private String name;

    /*List of OpenStackNetworkElements of NEUTRON*/
    public final List<OpenStackNetwork> openStackNetworks = new ArrayList<>();
    public final List<OpenStackSubnet> openStackSubnets = new ArrayList<> ();
    public final List<OpenStackRouter> openStackRouters = new ArrayList<> ();
    public final List<OpenStackPort> openStackPorts = new ArrayList<> ();

    /*List of OpenStackNetworkElements of NOVA*/
    public final List<OpenStackServer> openStackServers = new ArrayList<> ();
    public final List<OpenStackFlavor> openStackFlavors = new ArrayList<> ();
    public final List<OpenStackFloatingIp> openStackFloatingIps = new ArrayList<> ();
    public final List<OpenStackImage> openStackImages = new ArrayList<> ();
    public final List<OpenStackKeypair> openStackKeypairs = new ArrayList<> ();
    public final List<OpenStackSecurityGroup> openStackSecurityGroups = new ArrayList<> ();
    public final List<OpenStackHostResource> openStackHostResources = new ArrayList<> ();

    private OpenStackNet osn;

    public OpenStackClient create(OpenStackNet osn,JSONObject jsonObject,String name){

         System.out.println(jsonObject);
        try {
            this.name = name;
            this.osn = osn;
            os_auth_url = jsonObject.getString("os_auth_url");
            os_username = jsonObject.getString("os_username");
            os_password = jsonObject.getString("os_password");
            os_user_domain_name = jsonObject.getString("os_user_domain_name");
            os_project_id = jsonObject.getString("os_project_id");

            OSFactory.enableHttpLoggingFilter(true);

            OSClient.OSClientV3 os = OSFactory.builderV3()
                    .endpoint(os_auth_url)
                    .credentials(os_username,os_password, Identifier.byName(os_user_domain_name))
                    .scopeToProject(Identifier.byId(os_project_id))
                    .authenticate();

            this.os=os;
            this.openStackNetCreate = new OpenStackNetCreate(os);
            this.openStackNetDelete = new OpenStackNetDelete(os);
            System.out.println(" OpenStackClient new client");
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return this;
    }

    public OpenStackClient clearList(){
        /*Clear Neutron list*/
        openStackNetworks.clear();
        openStackRouters.clear();
        openStackPorts.clear();
        openStackSubnets.clear();

        /*Clear Nova list*/
        openStackServers.clear();
        openStackFlavors.clear();
        openStackImages.clear();
        openStackFloatingIps.clear();
        openStackKeypairs.clear();
        openStackSecurityGroups.clear();
        openStackHostResources.clear();
        System.out.println(" OpenStackClient clear client");
        return this;
    }
    public OpenStackClient fillList(){
        Token token = os.getToken();
        MyRunnable newR = new MyRunnable(token,Facing.PUBLIC);
        submit(newR);
        this.os=newR.getOs();
        System.out.println(" OpenStackClient fill client");

        try {
            /* Get elements of Network(NEUTRON)*/
            System.out.println(((List<Network>) os.networking().network().list()));
            ((List<Network>) os.networking().network().list()).forEach(n -> addOpenStackNetwork(n));
            ((List<Subnet>) os.networking().subnet().list()).forEach(n -> addOpenStackSubnet(n));
            ((List<Router>) os.networking().router().list()).stream().forEach(n -> addOpenStackRouter(n));
            ((List<Port>) os.networking().port().list()).stream().forEach(n -> addOpenStackPort(n));
            System.out.println(" OpenStackClient fill client");
            /*Get elements of Compute(NOVA)*/
            ((List<Server>) os.compute().servers().list()).stream().forEach(n -> addOpenStackServer(n));
            ((List<Flavor>) os.compute().flavors().list()).stream().forEach(n -> addOpenStackFlavor(n));
            ((List<? extends FloatingIP>) os.compute().floatingIps().list()).forEach(n -> addOpenStackFloatingIP(n));
            ((List<Image>) os.compute().images().list()).stream().forEach(n -> addOpenStackImage(n));
            ((List<Keypair>) os.compute().keypairs().list()).stream().forEach(n -> addOpenStackKeypair(n));
            ((List<SecGroupExtension>) os.compute().securityGroups().list()).stream().forEach(n -> addOpenStackSecurityGroup(n));
            final List<? extends HostResource> hosts = os.compute().host().list();
            hosts.stream().forEach(n -> os.compute().host().hostDescribe(((HostResource) n).getHostName()).stream().forEach(p -> addOpenStackHostResource(p)));
        }catch(Exception ex){
            ex.printStackTrace();
        }

        System.out.println(" OpenStackClient fill client");
        return this;
    }

    public String getName(){return this.name;}
    public OSClient.OSClientV3 getClient(){
        return this.os;
    }
    public OpenStackNetCreate getOpenStackNetCreate(){ return this.openStackNetCreate; }
    public OpenStackNetDelete getOpenStackNetDelete(){ return this.openStackNetDelete; }

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

    /* Add OpenStackNetworkElements of Neutron*/
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
    public OpenStackImage addOpenStackImage(Image image) {
        final OpenStackImage res = OpenStackImage.createFromAddImage(osn,image,this);
        if(openStackImages.contains(res)) return res;
        openStackImages.add(res);
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

    /*Get list from OpenStackNetworkElements of NEUTRON*/
    public List<OpenStackNetwork> getOpenStackNetworks () { return Collections.unmodifiableList(openStackNetworks); }
    public List<OpenStackSubnet> getOpenStackSubnets () { return Collections.unmodifiableList(openStackSubnets); }
    public List<OpenStackRouter> getOpenStackRouters () { return Collections.unmodifiableList(openStackRouters); }
    public List<OpenStackPort> getOpenStackPorts () { return Collections.unmodifiableList(openStackPorts); }

    /*Get list from OpenStackNetworkElements of nova*/
    public List<OpenStackServer> getOpenStackServers () { return Collections.unmodifiableList(openStackServers); }
    public List<OpenStackFlavor> getOpenStackFlavor () { return Collections.unmodifiableList(openStackFlavors); }
    public List<OpenStackImage> getOpenStackImages () { return Collections.unmodifiableList(openStackImages); }
    public List<OpenStackFloatingIp> getOpenStackFloatingIpDns () { return Collections.unmodifiableList(openStackFloatingIps); }
    public List<OpenStackKeypair> getOpenStackKeypairs () { return Collections.unmodifiableList(openStackKeypairs); }
    public List<OpenStackSecurityGroup> getOpenStackSecurityGroups () { return Collections.unmodifiableList(openStackSecurityGroups); }
    public List<OpenStackHostResource> getOpenStackHostResource () { return Collections.unmodifiableList(openStackHostResources); }

    public OpenStackNetworkElement getOpenStackNetworkElementByOpenStackId (String openStackId) {

        final List<OpenStackNetworkElement> allOpenStackNetworkElements = Lists.newArrayList();

        /*OpenStackNetworkElements of Neutron*/
        allOpenStackNetworkElements.addAll(openStackNetworks);
        allOpenStackNetworkElements.addAll(openStackSubnets);
        allOpenStackNetworkElements.addAll(openStackRouters);
        allOpenStackNetworkElements.addAll(openStackPorts);

        /*OpenStackNetworkElements of Neutron*/
        allOpenStackNetworkElements.addAll(openStackServers);
        allOpenStackNetworkElements.addAll(openStackFlavors);
        allOpenStackNetworkElements.addAll(openStackImages);
        allOpenStackNetworkElements.addAll(openStackFloatingIps);
        allOpenStackNetworkElements.addAll(openStackKeypairs);
        allOpenStackNetworkElements.addAll(openStackSecurityGroups);
        allOpenStackNetworkElements.addAll(openStackHostResources);

        Optional<OpenStackNetworkElement> element = allOpenStackNetworkElements.stream().filter(n->n.getId() == openStackId).findFirst();
        if (element.isPresent()) return element.get();
        else return null;
    }
}
