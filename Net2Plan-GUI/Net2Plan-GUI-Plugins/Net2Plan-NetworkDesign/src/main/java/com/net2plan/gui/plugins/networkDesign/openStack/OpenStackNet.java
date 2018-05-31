package com.net2plan.gui.plugins.networkDesign.openStack;


import com.google.common.collect.Lists;
import com.net2plan.gui.plugins.GUINetworkDesign;
import com.net2plan.gui.plugins.networkDesign.openStack.identity.*;
import com.net2plan.gui.plugins.networkDesign.openStack.network.*;
import com.net2plan.interfaces.networkDesign.NetPlan;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.openstack4j.api.OSClient.OSClientV3;
import org.openstack4j.model.identity.v3.*;
import org.openstack4j.model.network.*;

import javax.swing.*;

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


    public final  NetPlan getNetPlan () { return np; }

    public OpenStackNet()
    {
        this.np = new NetPlan();
    }

    public OpenStackNet (GUINetworkDesign callback, OSClientV3 os )
    {
        this.callback = callback;
        this.np = callback.getDesign();
        this.os=os;
        this.openStackNetCreate = new OpenStackNetCreate(os);
        this.openStackNetDelete = new OpenStackNetDelete(os);
    }

    public static OpenStackNet buildOpenStackNetFromServer(GUINetworkDesign callback, String os_auth_url, String os_username, String os_password, String os_project_name,String os_user_domain_name,String os_project_domain_id)
    {
        try
        {

            final OpenStackNet res = new TopologyCreator(callback, os_auth_url, os_username, os_password, os_project_name,os_user_domain_name,os_project_domain_id).getOpenStackNet();
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

        callback.getDesign().removeAllNodes();

        /* Get elements of Identity(Keystone)*/
        final List<User> users = (List<User>) os.identity().users().list();
        final List<? extends Project> projects = (List<? extends Project>)os.identity().tokens().getProjectScopes(os.getToken().getUser().getId());
        final List<Domain> domains = (List<Domain>) os.identity().domains().list();
        final List<Endpoint> endpoints = (List<Endpoint>) os.identity().serviceEndpoints().listEndpoints();
        final List<Service> services = (List<Service>) os.identity().serviceEndpoints().list();
        final List<Region> regions = (List<Region>)os.identity().regions().list();
        final List<Credential> credentials = (List<Credential>) os.identity().credentials().list();
        final List<Group> groups = (List<Group>) os.identity().groups().list();
        final List<Policy> policies = (List<Policy>) os.identity().policies().list();
        final List<Role> roles = (List<Role>)os.identity().roles().list();

        /* Get elements of Network(NEUTRON)*/
        final List<Network> networks = (List<Network>) os.networking().network().list();
        final List<Subnet> subnets = (List<Subnet>) os.networking().subnet().list();
        final List<Router> routers = (List<Router>) os.networking().router().list();
        final List<Port> ports = (List<Port>) os.networking().port().list();

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

        distributeTopologyOverCircle();
    }


}