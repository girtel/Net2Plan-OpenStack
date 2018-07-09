package com.net2plan.gui.plugins.networkDesign.openStack;


import com.google.common.collect.Lists;
import com.net2plan.gui.plugins.GUINetworkDesign;
import com.net2plan.gui.plugins.networkDesign.openStack.compute.*;
import com.net2plan.gui.plugins.networkDesign.openStack.identity.*;
import com.net2plan.gui.plugins.networkDesign.openStack.image.OpenStackImageV2;
import com.net2plan.gui.plugins.networkDesign.openStack.image.OpenStackTask;
import com.net2plan.gui.plugins.networkDesign.openStack.information.OpenStackInformationProject;
import com.net2plan.gui.plugins.networkDesign.openStack.information.OpenStackInformationUser;
import com.net2plan.gui.plugins.networkDesign.openStack.information.OpenStackSummary;
import com.net2plan.gui.plugins.networkDesign.openStack.network.*;
import com.net2plan.gui.plugins.networkDesign.openStack.orchestration.OpenStackEvent;
import com.net2plan.gui.plugins.networkDesign.openStack.orchestration.OpenStackResource;
import com.net2plan.gui.plugins.networkDesign.openStack.orchestration.OpenStackStack;
import com.net2plan.gui.plugins.networkDesign.openStack.orchestration.OpenStackTemplate;
import com.net2plan.interfaces.networkDesign.NetPlan;
import java.awt.geom.Point2D;
import java.util.*;

import org.openstack4j.api.OSClient.OSClientV3;
import org.openstack4j.api.types.Facing;
import org.openstack4j.model.compute.*;
import org.openstack4j.model.compute.FloatingIP;
import org.openstack4j.model.compute.SecurityGroup;
import org.openstack4j.model.heat.Event;
import org.openstack4j.model.heat.Resource;
import org.openstack4j.model.heat.Stack;
import org.openstack4j.model.identity.v3.*;
import org.openstack4j.model.image.v2.Task;
import org.openstack4j.model.network.*;

import javax.swing.*;

import static edu.emory.mathcs.utils.ConcurrencyUtils.submit;

/**
 *
 * @author Manuel
 */
public class OpenStackNet
{

    private GUINetworkDesign callback;
    private final NetPlan np;
    private OSClientV3 os;
    private OpenStackNetCreate openStackNetCreate;
    private OpenStackNetDelete openStackNetDelete;
    private String system;
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
    public final List<OpenStackNetwork> openStackNetworks = new ArrayList<> ();
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

    /*List of OpenStackNetworkElements of GLANCE*/
    public final List<OpenStackImageV2> openStackImageV2 = new ArrayList<> ();
    public final List<OpenStackTask> openStackTasks = new ArrayList<> ();

    /*List of OpenStackNetworkElements of HEAT*/
    public final List<OpenStackStack> openStackStacks = new ArrayList<> ();
    public final List<OpenStackTemplate> openStackTemplates= new ArrayList<> ();
    public final List<OpenStackResource> openStackResources = new ArrayList<> ();
    public final List<OpenStackEvent> openStackEvents = new ArrayList<> ();

    /*List of openstacknetworkelements of information*/
    public final List<OpenStackInformationProject> openStackInformationProject = new ArrayList<> ();
    public final List<OpenStackInformationUser> openStackInformationUser = new ArrayList<> ();
    public final List<OpenStackSummary> openStackSummaries = new ArrayList<> ();
    public final  NetPlan getNetPlan () { return np; }

    public OpenStackNet()
    {
        this.np = new NetPlan();
    }

    public OpenStackNet (GUINetworkDesign callback, OSClientV3 os ,String system)
    {
        this.callback = callback;
        this.np = callback.getDesign();
        this.os=os;
        this.openStackNetCreate = new OpenStackNetCreate(os,system);
        this.openStackNetDelete = new OpenStackNetDelete(os,system);
        this.system=system;
    }

    public static OpenStackNet buildOpenStackNetFromServer(GUINetworkDesign callback, String os_auth_url, String os_username, String os_password, String os_project_name,String os_user_domain_name,String os_project_domain_id,String system)
    {
        try
        {

            final OpenStackNet res = new TopologyCreator(callback, os_auth_url, os_username, os_password, os_project_name,os_user_domain_name,os_project_domain_id).getOpenStackNet(system);
            return res;
        } catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }


    public OSClientV3 getOSClientV3(){
        return this.os;
    }

    public OpenStackNetCreate getOpenStackNetCreate(){ return this.openStackNetCreate; }

    public OpenStackNetDelete getOpenStackNetDelete(){ return this.openStackNetDelete; }

    /* Add OpenStackNetworkElements of Keystone*/
    public OpenStackUser addOpenStackUser (User user)
    {
        final OpenStackUser res = OpenStackUser.createFromAddUser(this ,user);
        if(openStackUsers.contains(res)) return res;
        openStackUsers.add(res);
        return res;
    }

    public OpenStackProject addOpenStackProject(Project project)
    {
        final OpenStackProject res = OpenStackProject.createFromAddProject(this,project);
        if(openStackProjects.contains(res)) return res;
        openStackProjects.add(res);
        return res;
    }

    public OpenStackDomain addOpenStackDomain(Domain domain)
    {
        final OpenStackDomain res = OpenStackDomain.createFromAddDomain(this,domain);
        if(openStackDomains.contains(res)) return res;
        openStackDomains.add(res);
        return res;
    }

    public OpenStackEndpoint addOpenStackEndpoint(Endpoint endpoint)
    {
        final OpenStackEndpoint res = OpenStackEndpoint.createFromAddEndpoint(this,endpoint);
        if(openStackEndpoints.contains(res)) return res;
        openStackEndpoints.add(res);
        return res;
    }

    public OpenStackService addOpenStackService(Service service)
    {
        final OpenStackService res = OpenStackService.createFromAddService(this,service);
        if(openStackServices.contains(res)) return res;
        openStackServices.add(res);
        return res;
    }

    public OpenStackRegion addOpenStackRegion(Region region)
    {
        final OpenStackRegion res = OpenStackRegion.createFromAddRegion(this,region);
        if(openStackRegions.contains(res)) return res;
        openStackRegions.add(res);
        return res;
    }

    public OpenStackCredential addOpenStackCredential(Credential credential)
    {
        final OpenStackCredential res = OpenStackCredential.createFromAddCredential(this,credential);
        if(openStackCredentials.contains(res)) return res;
        openStackCredentials.add(res);
        return res;
    }

    public OpenStackGroup addOpenStackGroup(Group group)
    {
        final OpenStackGroup res = OpenStackGroup.createFromAddGroup(this,group);
        if(openStackGroups.contains(res)) return res;
        openStackGroups.add(res);
        return res;
    }
    public OpenStackPolicy addOpenStackPolicy(Policy policy)
    {
        final OpenStackPolicy res = OpenStackPolicy.createFromAddPolicy(this,policy);
        if(openStackPolicies.contains(res)) return res;
        openStackPolicies.add(res);
        return res;
    }

    public OpenStackRole addOpenStackRole(Role role)
    {
        final OpenStackRole res = OpenStackRole.createFromAddRole(this,role);
        if(openStackRoles.contains(res)) return res;
        openStackRoles.add(res);
        return res;
    }


    /* Add OpenStackNetworkElements of Neutron*/
    public OpenStackNetwork addOpenStackNetwork(Network network)
    {
        final OpenStackNetwork res = OpenStackNetwork.createFromAddNetwork(this ,network);
        if(openStackNetworks.contains(res)) return res;
        openStackNetworks.add(res);
        return res;
    }

    public OpenStackSubnet addOpenStackSubnet (Subnet subnet)
    {
        final OpenStackSubnet res = OpenStackSubnet.createFromAddSubnet(this,subnet);
        if(openStackSubnets.contains(res)) return res;
        openStackSubnets.add(res);
        return res;
    }

    public OpenStackRouter addOpenStackRouter(Router router)
    {
        final OpenStackRouter res = OpenStackRouter.createFromAddRouter(this,router);
        if(openStackRouters.contains(res)) return res;
        openStackRouters.add(res);
        return res;
    }

    public OpenStackPort addOpenStackPort(Port port)
    {
        final OpenStackPort res = OpenStackPort.createFromAddPort(this,port);
        if(openStackPorts.contains(res)) return res;
        openStackPorts.add(res);
        return res;
    }

    /* Add OpenStackNetworkElements of Neutron*/
    public OpenStackServer addOpenStackServer(Server server)
    {
        final OpenStackServer res = OpenStackServer.createFromAddServer(this,server);
        if(openStackServers.contains(res)) return res;
        openStackServers.add(res);
        return res;
    }
    public OpenStackFlavor addOpenStackFlavor(Flavor flavor)
    {
        final OpenStackFlavor res = OpenStackFlavor.createFromAddFlavor(this,flavor);
        if(openStackFlavors.contains(res)) return res;
        openStackFlavors.add(res);
        return res;
    }
    public OpenStackImage addOpenStackImage(Image image)
    {
        final OpenStackImage res = OpenStackImage.createFromAddImage(this,image);
        if(openStackImages.contains(res)) return res;
        openStackImages.add(res);
        return res;
    }
    public OpenStackFloatingIp addOpenStackFloatingIP(FloatingIP floatingIP)
    {
        final OpenStackFloatingIp res = OpenStackFloatingIp.createFromAddFloatingIp(this,floatingIP);
        if(openStackFloatingIps.contains(res)) return res;
        openStackFloatingIps.add(res);
        return res;
    }

    public OpenStackKeypair addOpenStackKeypair(Keypair keypair)
    {

        final OpenStackKeypair res = OpenStackKeypair.createFromAddKeypair(this,keypair);
        if(openStackKeypairs.contains(res)) return res;
        openStackKeypairs.add(res);
        return res;
    }

    public OpenStackSecurityGroup addOpenStackSecurityGroup(SecGroupExtension securityGroup)
    {
        final OpenStackSecurityGroup res = OpenStackSecurityGroup.createFromAddSegurityGroup(this,securityGroup);
        if(openStackSecurityGroups.contains(res)) return res;
        openStackSecurityGroups.add(res);
        return res;
    }


    /* Add OpenStackNetworkElements of Glance*/
    public OpenStackImageV2 addOpenStackImageV2(org.openstack4j.model.image.v2.Image image)
    {
        final OpenStackImageV2 res = OpenStackImageV2.createFromAddImageV2(this ,image);
        if(openStackImageV2.contains(res)) return res;
        openStackImageV2.add(res);
        return res;
    }

        public OpenStackTask addOpenStackTask(Task task)
    {
        final OpenStackTask res = OpenStackTask.createFromAddTask(this,task);
        if(openStackTasks.contains(res)) return res;
        openStackTasks.add(res);
        return res;
    }


    /* Add OpenStackNetworkElements of Heat*/
    public OpenStackStack addOpenStackStack(Stack stack)
    {
        final OpenStackStack res = OpenStackStack.createFromAddStack(this ,stack);
        if(openStackStacks.contains(res)) return res;
        openStackStacks.add(res);
        return res;
    }

    public OpenStackTemplate addOpenStackTemplate (Map<String,Object> template)
    {
        final OpenStackTemplate res = OpenStackTemplate.createFromAddTemplate(this,template);
        if(openStackTemplates.contains(res)) return res;
        openStackTemplates.add(res);
        return res;
    }

    public OpenStackResource addOpenStackResource(Resource resource)
    {
        final OpenStackResource res = OpenStackResource.createFromAddResource(this,resource);
        if(openStackResources.contains(res)) return res;
        openStackResources.add(res);
        return res;
    }

    public OpenStackEvent addOpenStackEvent(Event event)
    {
        final OpenStackEvent res = OpenStackEvent.createFromAddEvent(this,event);
        if(openStackEvents.contains(res)) return res;
        openStackEvents.add(res);
        return res;
    }


    /*Information*/
    public OpenStackInformationProject addInformationOfThisProject(){
        final OpenStackInformationProject res = OpenStackInformationProject.createFromAddInformationProject(this,this.os.getToken().getProject());
        if(openStackInformationProject.contains(res)) return res;
        openStackInformationProject.add(res);
        return res;
    }
    public OpenStackInformationUser addInformationOfThisUser(){
        final OpenStackInformationUser res = OpenStackInformationUser.createFromAddInformationUser(this,this.os.getToken().getUser());
        if(openStackInformationUser.contains(res)) return res;
        openStackInformationUser.add(res);
        return res;
    }
    public OpenStackSummary addSummary(){
        final OpenStackSummary res = OpenStackSummary.createFromAddSummary(this,this.os);
        if(openStackSummaries.contains(res)) return res;
        openStackSummaries.add(res);
        return res;
    }

    public String getTopologyName () { return np.getNetPlan().getNetworkName(); }
    public String getTopologyDescription () { return np.getNetPlan().getNetworkDescription(); }
    public void setTopologyName (String name) { this.np.getNetPlan().setNetworkName(name); }


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

    /*Get list from OpenStackNetworkElements of NEUTRON*/
    public List<OpenStackServer> getOpenStackServers () { return Collections.unmodifiableList(openStackServers); }
    public List<OpenStackFlavor> getOpenStackFlavor () { return Collections.unmodifiableList(openStackFlavors); }
    public List<OpenStackImage> getOpenStackImages () { return Collections.unmodifiableList(openStackImages); }
    public List<OpenStackFloatingIp> getOpenStackFloatingIpDns () { return Collections.unmodifiableList(openStackFloatingIps); }
    public List<OpenStackKeypair> getOpenStackKeypairs () { return Collections.unmodifiableList(openStackKeypairs); }
    public List<OpenStackSecurityGroup> getOpenStackSecurityGroups () { return Collections.unmodifiableList(openStackSecurityGroups); }

    /*Get list from OpenStackNetworkElements of Glance*/
    public List<OpenStackImageV2> getOpenStackImageV2 () { return Collections.unmodifiableList(openStackImageV2); }
    public List<OpenStackTask> getOpenStackTask () { return Collections.unmodifiableList(openStackTasks); }

    /*Get list from OpenStackNetworkElements of Heat*/
    public List<OpenStackStack> getOpenStackStacks () { return Collections.unmodifiableList(openStackStacks); }
    public List<OpenStackTemplate> getOpenStackTemplates () { return Collections.unmodifiableList(openStackTemplates); }
    public List<OpenStackResource> getOpenStackResources () { return Collections.unmodifiableList(openStackResources); }
    public List<OpenStackEvent> getOpenStackEvents () { return Collections.unmodifiableList(openStackEvents); }


    /*Get list from OpenStackNetworkElements of information*/
    public List<OpenStackInformationProject> getOpenStackInformationProject () { return Collections.unmodifiableList(openStackInformationProject); }
    public List<OpenStackInformationUser> getOpenStackInformationUser () { return Collections.unmodifiableList(openStackInformationUser); }
    public List<OpenStackSummary> getOpenStackSummary () { return Collections.unmodifiableList(openStackSummaries); }

    public OpenStackNetworkElement getOpenStackNetworkElementByOpenStackId (String openStackId)
    {
        final List<OpenStackNetworkElement> allOpenStackNetworkElements = Lists.newArrayList();
        /*OpenStackNetworkElements of Keystone*/
        allOpenStackNetworkElements.addAll(openStackUsers);
        allOpenStackNetworkElements.addAll(openStackProjects);
        allOpenStackNetworkElements.addAll(openStackDomains);
        allOpenStackNetworkElements.addAll(openStackEndpoints);

        allOpenStackNetworkElements.addAll(openStackServices);
        allOpenStackNetworkElements.addAll(openStackRegions);
        allOpenStackNetworkElements.addAll(openStackCredentials);
        allOpenStackNetworkElements.addAll(openStackGroups);

        allOpenStackNetworkElements.addAll(openStackPolicies);
        allOpenStackNetworkElements.addAll(openStackRoles);

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

        /*OpenStackNetworkElements of information*/
        allOpenStackNetworkElements.addAll(openStackInformationProject);
        allOpenStackNetworkElements.addAll(openStackInformationUser);
        allOpenStackNetworkElements.addAll(openStackSummaries);

        Optional<OpenStackNetworkElement> element = allOpenStackNetworkElements.stream().filter(n->n.getId() == openStackId).findFirst();
        if (element.isPresent()) return element.get();
        else return null;
    }

    public void distributeTopologyOverCircle()
    {
        // Node list
        final List<OpenStackRouter> nodeList = getOpenStackRouters();

        final int numNodes = nodeList.size();
        double index = 0.0;

        for (OpenStackRouter node : nodeList)
        {
            final double xCoord = Math.sin(Math.toRadians((360 * index) / numNodes));
            final double yCoord = -Math.cos(Math.toRadians((360 * index) / numNodes));

            node.setXYPositionMap(new Point2D.Double(xCoord, yCoord));

            index++;
        }



    }

    public void refreshListTable(){

        /*Clear identity list*/
        openStackUsers.clear();
        openStackProjects.clear();
        openStackDomains.clear();
        openStackEndpoints.clear();
        openStackServices.clear();
        openStackRegions.clear();
        openStackCredentials.clear();
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
        openStackImages.clear();
        openStackFloatingIps.clear();
        openStackKeypairs.clear();
        openStackSecurityGroups.clear();

        /*Clear Glance list*/
        openStackImageV2.clear();
        openStackTasks.clear();

        /*Clear Neutron list*/
        openStackStacks.clear();
        openStackTemplates.clear();
        openStackResources.clear();
        openStackEvents.clear();

        /*Clear information list*/
        openStackInformationUser.clear();
        openStackInformationProject.clear();
        openStackSummaries.clear();

        callback.getDesign().removeAllNodes();

        Token token = os.getToken();
        MyRunnable newR;
        changeOs(Facing.INTERNAL);
        /* Get elements of Identity(Keystone)*/
        final List<User> users = (List<User>) os.identity().users().list();
         List<? extends Project> projects = new ArrayList<>();
        if(system.equals("ubuntu"))
        projects = os.identity().projects().list();
        final List<Domain> domains = (List<Domain>) os.identity().domains().list();
        final List<Endpoint> endpoints = (List<Endpoint>) os.identity().serviceEndpoints().listEndpoints();
        final List<Service> services = (List<Service>) os.identity().serviceEndpoints().list();
        final List<Region> regions = (List<Region>)os.identity().regions().list();
        final List<Credential> credentials = (List<Credential>) os.identity().credentials().list();
        final List<Group> groups = (List<Group>) os.identity().groups().list();
        final List<Policy> policies = (List<Policy>) os.identity().policies().list();
        final List<Role> roles = (List<Role>)os.identity().roles().list();

        changeOs(Facing.PUBLIC);


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
           addOpenStackUser(user);

        /* Create project objects */
        for (Project project : projects)
            addOpenStackProject(project);

        /* Create domain objects */
        for (Domain domain : domains)
            addOpenStackDomain(domain);

        /* Create endpoint objects */
        for (Endpoint endpoint : endpoints)
            addOpenStackEndpoint(endpoint);

        /* Create service objects */
        for (Service service : services)
            addOpenStackService(service);

        /* Create region objects */
        for (Region region : regions)
            addOpenStackRegion(region);

        /* Create credential objects */
        for (Credential credential : credentials)
            addOpenStackCredential(credential);

        /* Create group objects */
        for (Group group : groups)
            addOpenStackGroup(group);

        /* Create policy objects */
        for (Policy policy : policies)
            addOpenStackPolicy(policy);

        /* Create role objects */
        for (Role role : roles)
            addOpenStackRole(role);

        /* Create OpenStackNetworkElement of Neutron Elements*/
        /* Create networks objects */
        for (Network net : networks)
            addOpenStackNetwork(net);

        /* Create subnets objects */
        for (Subnet subnet : subnets)
            addOpenStackSubnet(subnet);

        /* Create routers objects */
        for (Router router : routers)
            addOpenStackRouter(router);
        /* Create routers objects */
        for (Port port : ports)
            addOpenStackPort(port);

        /* Create OpenStackNetworkElement of Nova Elements*/
        /* Create server objects */
        for (Server server : servers) {
            addOpenStackServer(server);
        }
        /* Create flavor objects */
        for (Flavor flavor : flavors) {
            addOpenStackFlavor(flavor);
        }
        /* Create image objects */
        for (Image image : images) {
            addOpenStackImage(image);
        }
        /* Create fIP objects */
        for (FloatingIP floatingIP : floatingIPS) {
            addOpenStackFloatingIP(floatingIP);
        }
        /* Create keypair objects */
        for (Keypair keypair : keypairs) {
            addOpenStackKeypair(keypair);
        }
        /* Create secgroup objects */
        for (SecGroupExtension secGroupExtension : secGroupExtensions) {
            addOpenStackSecurityGroup(secGroupExtension);
        }

        /* Create OpenStackNetworkElement of Glance Elements*/
        /* Create Image objects */
        for (org.openstack4j.model.image.v2.Image imageV2 : imagesV2) {
            addOpenStackImageV2(imageV2);
        }
        /* Create Task objects */
        for (Task task : tasks) {
            addOpenStackTask(task);
        }

        /* Create OpenStackNetworkElement of Heat Elements*/
        /* Create Stack objects */
        for (Stack stack : stacks) {
            addOpenStackStack(stack);
        }
        /* Create Template objects */
        for (Map<String,Object> template : templates) {
            addOpenStackTemplate(template);
        }
        /* Create Resource objects */
        for (Resource resource : resources) {
            addOpenStackResource(resource);
        }
        /* Create Event objects */
        for (Event event : events) {
            addOpenStackEvent(event);
        }


        addInformationOfThisProject();
        addInformationOfThisUser();
        addSummary();

        distributeTopologyOverCircle();
    }

    public void changeOs(Facing facing){
        Token token = os.getToken();
        MyRunnable newR;

                if(system.equals("ubuntu")) {
                    newR = new MyRunnable(token, facing);
                    submit(newR);
                    this.os = newR.getOs();
                }


    }
}