package com.net2plan.gui.plugins.networkDesign.openStack;


import com.net2plan.gui.plugins.GUINetworkDesign;
import com.net2plan.interfaces.networkDesign.Net2PlanException;

import java.lang.reflect.Member;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openstack4j.api.Builders;
import org.openstack4j.api.OSClient;
import org.openstack4j.api.OSClient.OSClientV3;
import org.openstack4j.api.OSClient.OSClientV2;
import org.openstack4j.api.types.Facing;
import org.openstack4j.core.transport.Config;
import org.openstack4j.model.common.Identifier;
import org.openstack4j.model.compute.*;
import org.openstack4j.model.heat.Event;
import org.openstack4j.model.heat.Resource;
import org.openstack4j.model.heat.Stack;
import org.openstack4j.model.heat.Template;
import org.openstack4j.model.identity.v3.*;
import org.openstack4j.model.image.v2.ContainerFormat;
import org.openstack4j.model.image.v2.DiskFormat;
import org.openstack4j.model.image.v2.Task;
import org.openstack4j.model.network.Network;
import org.openstack4j.model.network.Port;
import org.openstack4j.model.network.Router;
import org.openstack4j.model.network.Subnet;
import org.openstack4j.openstack.OSFactory;
import org.openstack4j.openstack.identity.v2.domain.KeystoneService;

import static edu.emory.mathcs.utils.ConcurrencyUtils.setNumberOfThreads;
import static edu.emory.mathcs.utils.ConcurrencyUtils.submit;

/**
 *
 * @author Manuel
 */
class TopologyCreator
{
    private  OSClientV3 os;
    private final GUINetworkDesign callback;

    TopologyCreator(GUINetworkDesign callback, String os_auth_url, String os_username, String os_password, String os_project_name, String os_user_domain_name , String os_project_domain_id)
    {
        OSFactory.enableHttpLoggingFilter(true);


            os = OSFactory.builderV3()
                    .endpoint(os_auth_url)
                    .credentials(os_username, os_password, Identifier.byName(os_user_domain_name))
                    .scopeToProject(Identifier.byName(os_project_name), Identifier.byId(os_project_domain_id))
                    .authenticate();

        this.callback = callback;
    }





    OpenStackNet getOpenStackNet(String system)
    {
         /* Empty NetPlan */
        final OpenStackNet osn = new OpenStackNet(callback,os,system);

               changeOs(Facing.INTERNAL, system);
            /* Get elements of Identity(Keystone)*/
            final List<User> users = (List<User>) os.identity().users().list();
        List<? extends Project> projects = new ArrayList<>();
if(system.equals("ubuntu"))
                projects = os.identity().projects().list();

            final List<Domain> domains = (List<Domain>) os.identity().domains().list();
            final List<Endpoint> endpoints = (List<Endpoint>) os.identity().serviceEndpoints().listEndpoints();
            final List<Service> services = (List<Service>) os.identity().serviceEndpoints().list();
            final List<Region> regions = (List<Region>) os.identity().regions().list();
            final List<Credential> credentials = (List<Credential>) os.identity().credentials().list();
            final List<Group> groups = (List<Group>) os.identity().groups().list();
            final List<Policy> policies = (List<Policy>) os.identity().policies().list();
            final List<Role> roles = (List<Role>) os.identity().roles().list();

            changeOs(Facing.PUBLIC, system);

            /* Get elements of Network(NEUTRON)*/
            final List<Network> networks = (List<Network>) os.networking().network().list();
            final List<Subnet> subnets = (List<Subnet>) os.networking().subnet().list();
            final List<Router> routers = (List<Router>) os.networking().router().list();
            final List<Port> ports = (List<Port>) os.networking().port().list();

            /*Get elements of Compute(NOVA)*/
            final List<Server> servers = (List<Server>) os.compute().servers().list();
            final List<Flavor> flavors = (List<Flavor>) os.compute().flavors().list();
            final List<? extends FloatingIP> floatingIPS = (List<? extends FloatingIP>) os.compute().floatingIps().list();
            final List<Image> images = (List<Image>) os.compute().images().list();
            final List<Keypair> keypairs = (List<Keypair>) os.compute().keypairs().list();
            final List<? extends SecGroupExtension> secGroupExtensions = os.compute().securityGroups().list();

            /*Get elements of Image(Glance)*/
            final List<org.openstack4j.model.image.v2.Image> imagesV2 = (List<org.openstack4j.model.image.v2.Image>) os.imagesV2().list();
            final List<Task> tasks = (List<Task>) os.imagesV2().tasks().list();

        System.out.println(imagesV2);
            /*Get elements of Orchestation(Heat)*/
            final List<Stack> stacks = (List<Stack>) os.heat().stacks().list();
            List<Map<String,Object>> templates = new ArrayList<>();
            List<Resource> resources = new ArrayList<>();
            List<Event> events = new ArrayList<>();
            for(Stack stack:stacks) {
                templates.add((Map<String,Object>) os.heat().templates().getTemplateAsMap(stack.getId()));
                for (Resource resource : (List<Resource>) os.heat().resources().list(stack.getId()))
                    resources.add(resource);
                for (Event event : (List<Event>) os.heat().events().list(stack.getName(),stack.getId()))
                    events.add(event);

            }

        /* Create OpenStackNetworkElement of Keystone Elements*/
            /* Create User objects */
            for (User user : users)
                osn.addOpenStackUser(user);

            /* Create project objects */
            for (Project project : projects)
                osn.addOpenStackProject(project);

            /* Create domain objects */
            for (Domain domain : domains)
                osn.addOpenStackDomain(domain);

            /* Create endpoint objects */
            for (Endpoint endpoint : endpoints)
                osn.addOpenStackEndpoint(endpoint);

            /* Create service objects */
            for (Service service : services)
                osn.addOpenStackService(service);

            /* Create region objects */
            for (Region region : regions)
                osn.addOpenStackRegion(region);

            /* Create credential objects */
            for (Credential credential : credentials)
                osn.addOpenStackCredential(credential);

            /* Create group objects */
            for (Group group : groups)
                osn.addOpenStackGroup(group);

            /* Create policy objects */
            for (Policy policy : policies)
                osn.addOpenStackPolicy(policy);

            /* Create role objects */
            for (Role role : roles)
                osn.addOpenStackRole(role);

            /* Create OpenStackNetworkElement of Neutron Elements*/
            /* Create networks objects */
            for (Network net : networks)
                osn.addOpenStackNetwork(net);

            /* Create subnets objects */
            for (Subnet subnet : subnets)
                osn.addOpenStackSubnet(subnet);

            /* Create routers objects */
            for (Router router : routers)
                osn.addOpenStackRouter(router);
            /* Create routers objects */
            for (Port port : ports)
                osn.addOpenStackPort(port);

            /* Create OpenStackNetworkElement of Nova Elements*/
            /* Create server objects */
            for (Server server : servers) {
                osn.addOpenStackServer(server);
            }
            /* Create flavor objects */
            for (Flavor flavor : flavors) {
                osn.addOpenStackFlavor(flavor);
            }
            /* Create image objects */
            for (Image image : images) {
                osn.addOpenStackImage(image);
            }
            /* Create fIP objects */
            for (FloatingIP floatingIP : floatingIPS) {
                osn.addOpenStackFloatingIP(floatingIP);
            }
            /* Create keypair objects */
            for (Keypair keypair : keypairs) {
                osn.addOpenStackKeypair(keypair);
            }
            /* Create secgroup objects */
            for (SecGroupExtension secGroupExtension : secGroupExtensions) {
                osn.addOpenStackSecurityGroup(secGroupExtension);
            }

            /* Create OpenStackNetworkElement of Glance Elements*/
            /* Create Image objects */
            for (org.openstack4j.model.image.v2.Image imageV2 : imagesV2) {
                osn.addOpenStackImageV2(imageV2);
            }
            /* Create Task objects */
            for (Task task : tasks) {
                osn.addOpenStackTask(task);
            }

            /* Create OpenStackNetworkElement of Heat Elements*/
            /* Create Stack objects */
            for (Stack stack : stacks) {
                osn.addOpenStackStack(stack);
            }
            /* Create Template objects */
            for (Map<String,Object> template : templates) {
                osn.addOpenStackTemplate(template);
            }
            /* Create Resource objects */
            for (Resource resource : resources) {
                osn.addOpenStackResource(resource);
            }
            /* Create Event objects */
            for (Event event : events) {
                osn.addOpenStackEvent(event);
            }

            osn.addInformationOfThisProject();
            osn.addInformationOfThisUser();
            osn.addSummary();




        //if (routers.isEmpty()) throw new Net2PlanException("The OpenStack topology is empty");

        osn.distributeTopologyOverCircle();

        return osn;
    }


    public void changeOs(Facing facing,String system){
        Token token = os.getToken();
        MyRunnable newR;

        if(system.equals("ubuntu")) {
            newR = new MyRunnable(token, facing);
            submit(newR);
            this.os = newR.getOs();
        }


    }
}

 class MyRunnable implements Runnable {
    private OSClientV3 os;

    public MyRunnable(Token token,Facing facing) {
        this.os = OSFactory.clientFromToken(token,facing);
    }
    public OSClientV3 getOs(){
        return os;
    }
    public void run() {

        // can now use the client :)
    }
}