package com.net2plan.gui.plugins.networkDesign.openStack;


import com.google.common.collect.Lists;
import com.net2plan.gui.plugins.GUINetworkDesign;
import com.net2plan.gui.plugins.networkDesign.openStack.identity.*;
import com.net2plan.gui.plugins.networkDesign.openStack.network.OpenStackNetwork;
import com.net2plan.gui.plugins.networkDesign.openStack.network.OpenStackPort;
import com.net2plan.gui.plugins.networkDesign.openStack.network.OpenStackRouter;
import com.net2plan.gui.plugins.networkDesign.openStack.network.OpenStackSubnet;
import com.net2plan.interfaces.networkDesign.Link;
import com.net2plan.interfaces.networkDesign.NetPlan;
import com.net2plan.interfaces.networkDesign.Node;
import java.awt.geom.Point2D;
import java.io.File;
import java.net.URL;
import java.util.*;

import org.openstack4j.api.Builders;
import org.openstack4j.api.OSClient.OSClientV3;
import org.openstack4j.api.types.Facing;
import org.openstack4j.model.identity.v3.Domain;
import org.openstack4j.model.identity.v3.Endpoint;
import org.openstack4j.model.identity.v3.User;
import org.openstack4j.model.network.*;

/**
 *
 * @author Manuel
 */
public class OpenStackNet
{

    private GUINetworkDesign callback;
    private final NetPlan np;
    private OSClientV3 os;

    // Network
    public final List<OpenStackRouter> list_osRouters = new ArrayList<> ();
    public final List<OpenStackNetwork> list_osNetworks = new ArrayList<> ();
    public final List<OpenStackSubnet> list_osSubnets = new ArrayList<> ();
    public final List<OpenStackPort> list_osPorts = new ArrayList<> ();

    //Identity
    public final List<OpenStackUser> list_osUsers = new ArrayList<> ();
    public final List<OpenStackCredential> list_osCredentials = new ArrayList<> ();
    public final List<OpenStackDomain> list_osDomains = new ArrayList<> ();
    public final List<OpenStackEndpoint> list_osEndpoints = new ArrayList<> ();
    public final List<OpenStackGroup> list_osGroups = new ArrayList<> ();
    public final List<OpenStackPolice> list_osPolicies = new ArrayList<> ();
    public final List<OpenStackProject> list_osProjects = new ArrayList<> ();
    public final List<OpenStackRegion> list_osRegions = new ArrayList<> ();
    public final List<OpenStackRole> list_osRoles = new ArrayList<> ();
    public final List<OpenStackService> list_osServices = new ArrayList<> ();

    //Compute
    public final List<OpenStackGeneralInformation> list_osInformation = new ArrayList<> ();

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


    public OSClientV3 getOs(){
        return this.os;
    }

    public OpenStackUser addOpenStackUser (User user,String userId, String userName, String userDomainId, String userEmail, String userDescription)
    {
        final OpenStackUser res = OpenStackUser.createFromAddUser(this ,user, userId, userName, userDomainId, userEmail, userDescription);
        if(list_osUsers.contains(res)) return res;
        list_osUsers.add(res);
        return res;
    }

    public OpenStackNetwork addOpenStackNetwork(String networkId,String networkName,State networkStatus,NetworkType networkType,List<? extends Subnet> networkNeutronSubnets,String networkProviderPhyNet,String networkProviderSegID,List <String> networkSubnets,String networkTenantId,boolean networkIsAdminStateUp,boolean networkIsRouterExternal, boolean networkIsShared,Integer networkMTU)
    {
        final OpenStackNetwork res = OpenStackNetwork.createFromAddNetwork(this , networkId, networkName, networkStatus, networkType, networkNeutronSubnets,networkProviderPhyNet,networkProviderSegID,networkSubnets,networkTenantId,networkIsAdminStateUp,networkIsRouterExternal,networkIsShared,networkMTU);
        if(list_osNetworks.contains(res)) return res;
        list_osNetworks.add(res);
        return res;
    }

    public OpenStackSubnet addOpenStackSubnet (String subnetId,String subnetName,List<? extends Pool> subnetAllocationPools,String subnetCidr,List<String> subnetDnsNames,String subnetGateway,List<? extends HostRoute> subnetHostRoutes,IPVersionType subnetIpVersion,Ipv6AddressMode subnetIpv6AddressMode,Ipv6RaMode subnetIpv6RaMode,String subnetNetworkId,String subnetTenantId,boolean subnetIsDHCPEnabled)
    {
        final OpenStackSubnet res = OpenStackSubnet.createFromAddSubnet(this,subnetId, subnetName, subnetAllocationPools, subnetCidr, subnetDnsNames, subnetGateway,subnetHostRoutes,subnetIpVersion,subnetIpv6AddressMode,subnetIpv6RaMode,subnetNetworkId,subnetTenantId,subnetIsDHCPEnabled);
        if(list_osSubnets.contains(res)) return res;
        list_osSubnets.add(res);
        return res;
    }

    public OpenStackRouter addOpenStackRouter(String nodeId,String nodeName,String nodeTenantId, State nodeStatus,boolean nodeIsAdminStateUp,boolean nodeDistributed,List<? extends HostRoute> nodeRoutes, ExternalGateway nodeExternalGatewayInfo)
    {
        final OpenStackRouter res = OpenStackRouter.createFromAddRouter(this,nodeId, nodeName,nodeTenantId, nodeStatus, nodeIsAdminStateUp, nodeDistributed, nodeRoutes,nodeExternalGatewayInfo);
        if(list_osRouters.contains(res)) return res;
        list_osRouters.add(res);
        return res;
    }

    public OpenStackPort addOpenStackPort( String portId, String portName,  String portTenantId, Set<? extends AllowedAddressPair> portAllowedAddressPair,String portDeviceId, String portDeviceOwner, Set<? extends IP> portFixedIps,String portHostId,String portMacAddress,String portNetworkId,Map<String,Object> portProfile, List<String> portSecurityGroups,State portState,boolean isAdminStateUp,boolean portSecurityEnable)
    {
        final OpenStackPort res = OpenStackPort.createFromAddPort(this,portId,portName,portTenantId,portAllowedAddressPair,portDeviceId,portDeviceOwner,portFixedIps,portHostId,portMacAddress,portNetworkId,portProfile,portSecurityGroups,portState,isAdminStateUp,portSecurityEnable);
        if(list_osPorts.contains(res)) return res;
        list_osPorts.add(res);
        return res;
    }
    public OpenStackCredential addOpenStackCredential(String credentialId, String credentialUserId, String credentialProjectId, String credentialBlob, String credentialType,Map<String,String> credentialLinks)
    {
        final OpenStackCredential res = OpenStackCredential.createFromAddCredential(this,credentialId,credentialUserId,credentialProjectId,credentialBlob,credentialType,credentialLinks);
        if(list_osCredentials.contains(res)) return res;
        list_osCredentials.add(res);
        return res;
    }

    public OpenStackDomain addOpenStackDomain(String domainId, String domainName,String domainDescription, boolean domainEnabled, Map<String,String>domainLinks)
    {
        final OpenStackDomain res = OpenStackDomain.createFromAddDomain(this,domainId,domainName,domainDescription,domainEnabled,domainLinks);
        if(list_osDomains.contains(res)) return res;
        list_osDomains.add(res);
        return res;
    }
    public OpenStackEndpoint addOpenStackEndpoint(String endpointId, String endpointName, String endpointDescription, boolean endpointEnabled, Map<String,String> endpointLinks, String endpointRegion, String endpointRegionId, Facing endpointIface, String endpointServiceId, String endpointType, URL endpointUrl)
    {
        final OpenStackEndpoint res = OpenStackEndpoint.createFromAddEndpoint(this,endpointId,endpointName,endpointDescription,endpointEnabled,endpointLinks,endpointRegion,endpointRegionId,endpointIface,endpointServiceId,endpointType,endpointUrl);
        if(list_osEndpoints.contains(res)) return res;
        list_osEndpoints.add(res);
        return res;
    }

    public OpenStackGroup addOpenStackGroup(String groupId, String groupName, String groupDescription, String groupDomain, Map<String,String> groupLinks)
    {
        final OpenStackGroup res = OpenStackGroup.createFromAddGroup(this,groupId,groupName,groupDescription,groupDomain,groupLinks);
        if(list_osGroups.contains(res)) return res;
        list_osGroups.add(res);
        return res;
    }
    public OpenStackPolice addOpenStackPolicy(String policyId,String policyUserId, String policyProjectId,String policyType,String policyBlob,Map<String,String> policyLinks)
    {
        final OpenStackPolice res = OpenStackPolice.createFromAddPolicy(this,policyId,policyUserId,policyProjectId,policyType,policyBlob,policyLinks);
        if(list_osPolicies.contains(res)) return res;
        list_osPolicies.add(res);
        return res;
    }

    public OpenStackProject addOpenStackProject(String projectId, String projectName, String projectParentId, String projectDomainId, Domain projectDomain, String projectDescription, String projectParents, String projectSubtree, boolean projectEnabled, Map<String,String>projectLinks)
    {
        final OpenStackProject res = OpenStackProject.createFromAddProject(this,projectId,projectName,projectParentId,projectDomainId,projectDomain,projectDescription,projectParents,projectSubtree,projectEnabled,projectLinks);
        if(list_osProjects.contains(res)) return res;
        list_osProjects.add(res);
        return res;
    }
    public OpenStackRegion addOpenStackRegion(String regionId,String regionDescription,String regionParentRegionId)
    {
        final OpenStackRegion res = OpenStackRegion.createFromAddRegion(this,regionId,regionDescription,regionParentRegionId);
        if(list_osRegions.contains(res)) return res;
        list_osRegions.add(res);
        return res;
    }
    public OpenStackRole addOpenStackRole(String roleId,String roleName,String roleDomainId,Map<String,String> roleLinks)
    {
        final OpenStackRole res = OpenStackRole.createFromAddRole(this,roleId,roleName,roleDomainId,roleLinks);
        if(list_osRoles.contains(res)) return res;
        list_osRoles.add(res);
        return res;
    }
    public OpenStackService addOpenStackService(String serviceId, String serviceName, String serviceDescription, String serviceType, Integer serviceVersion, boolean serviceEnabled, List<? extends Endpoint>serviceEndpoints, Map<String,String>serviceLinks)
    {
        final OpenStackService res = OpenStackService.createFromAddService(this,serviceId,serviceName,serviceDescription,serviceType,serviceVersion,serviceEnabled,serviceEndpoints,serviceLinks);
        if(list_osServices.contains(res)) return res;
        list_osServices.add(res);
        return res;
    }

    public OpenStackGeneralInformation addOpenStackInformation()
    {
        String projectID = os.getToken().getProject().getId();
         OpenStackGeneralInformation res = OpenStackGeneralInformation.createFromAddInformation(this,projectID,"User", list_osUsers.size());
        if(!list_osInformation.contains(res)) {
            list_osInformation.add(res);
        }

        res = OpenStackGeneralInformation.createFromAddInformation(this,projectID,"Network", list_osNetworks.size());
        if(!list_osInformation.contains(res)) {
            list_osInformation.add(res);
        }

        res = OpenStackGeneralInformation.createFromAddInformation(this,projectID,"Router", list_osRouters.size());
        if(!list_osInformation.contains(res)) {
            list_osInformation.add(res);
        }

        res = OpenStackGeneralInformation.createFromAddInformation(this,projectID,"Subnet", list_osSubnets.size());
        if(!list_osInformation.contains(res)) {
            list_osInformation.add(res);
        }
        return res;
    }

    public String getTopologyName () { return np.getNetPlan().getNetworkName(); }
    public String getTopologyDescription () { return np.getNetPlan().getNetworkDescription(); }
    public void setTopologyName (String name) { this.np.getNetPlan().setNetworkName(name); }

    public List<OpenStackRouter> getOpenStackRouters () { return Collections.unmodifiableList(list_osRouters); }
    public List<OpenStackUser> getOpenStackUsers () { return Collections.unmodifiableList(list_osUsers); }
    public List<OpenStackNetwork> getOpenStackNetworks () { return Collections.unmodifiableList(list_osNetworks); }
    public List<OpenStackSubnet> getOpenStackSubnets () { return Collections.unmodifiableList(list_osSubnets); }
    public List<OpenStackPort> getOpenStackPorts () { return Collections.unmodifiableList(list_osPorts); }
    public List<OpenStackCredential> getOpenStackCredentials () { return Collections.unmodifiableList(list_osCredentials); }
    public List<OpenStackGeneralInformation> getOpenStackInformation () { return Collections.unmodifiableList(list_osInformation); }
    public List<OpenStackDomain> getOpenStackDomains () { return Collections.unmodifiableList(list_osDomains); }
    public List<OpenStackEndpoint> getOpenStackEndpoints () { return Collections.unmodifiableList(list_osEndpoints); }
    public List<OpenStackPolice> getOpenStackPolicies () { return Collections.unmodifiableList(list_osPolicies); }
    public List<OpenStackProject> getOpenStackProjects () { return Collections.unmodifiableList(list_osProjects); }
    public List<OpenStackRegion> getOpenStackRegions () { return Collections.unmodifiableList(list_osRegions); }
    public List<OpenStackRole> getOpenStackRoles () { return Collections.unmodifiableList(list_osRoles); }
    public List<OpenStackService> getOpenStackServices () { return Collections.unmodifiableList(list_osServices); }

    public OpenStackNetworkElement getOpenStackNetworkElementByOpenStackId (String openStackId)
    {
        final List<OpenStackNetworkElement> allOpenStackNetworkElements = Lists.newArrayList();
        allOpenStackNetworkElements.addAll(list_osRouters);
        allOpenStackNetworkElements.addAll(list_osUsers);
        allOpenStackNetworkElements.addAll(list_osNetworks);
        allOpenStackNetworkElements.addAll(list_osSubnets);
        allOpenStackNetworkElements.addAll(list_osInformation);
        allOpenStackNetworkElements.addAll(list_osCredentials);
        allOpenStackNetworkElements.addAll(list_osEndpoints);
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

    public void updateUserTable(){
        list_osUsers.clear();
        List<User> users = (List<User>) os.identity().users().list();
        for (User user : users)
            addOpenStackUser(user,user.getId(), user.getName(), user.getDomainId(), user.getEmail(), user.getDescription());
    }

    public void updateRouterTable(){
        list_osRouters.clear();

        callback.getDesign().removeAllNodes();
        List<Router> routers = (List<Router>) os.networking().router().list();
        for (Router router : routers)
            addOpenStackRouter(router.getId(), router.getName(), router.getTenantId(), router.getStatus(), router.isAdminStateUp(), router.getDistributed(), router.getRoutes(), router.getExternalGatewayInfo());

        distributeTopologyOverCircle();
    }

    public void updateNetworkTable(){
        list_osNetworks.clear();
        final List<Network> networks = (List<Network>) os.networking().network().list();
        for (Network net : networks)
            addOpenStackNetwork(net.getId(),net.getName(),net.getStatus(),net.getNetworkType(),net.getNeutronSubnets(),net.getProviderPhyNet(),net.getProviderSegID(),net.getSubnets(),net.getTenantId(),net.isAdminStateUp(),net.isRouterExternal(),net.isShared(),net.getMTU());

    }

    public void updateSubnetTable(){
        list_osSubnets.clear();
        final List<Subnet> subnets = (List<Subnet>) os.networking().subnet().list();
        for (Subnet subnet : subnets)
            addOpenStackSubnet(subnet.getId(),subnet.getName(),subnet.getAllocationPools(),subnet.getCidr(),subnet.getDnsNames(),subnet.getGateway(),subnet.getHostRoutes(),subnet.getIpVersion(),subnet.getIpv6AddressMode(),subnet.getIpv6RaMode(),subnet.getNetworkId(),subnet.getTenantId(),subnet.isDHCPEnabled());

    }

    public void updateInformationTable(){

        list_osInformation.clear();
        addOpenStackInformation();
    }

    public void updateAllTables(){

        updateUserTable();
        updateInformationTable();
        updateNetworkTable();
        updateRouterTable();
        updateSubnetTable();

    }
    public void createNewUser (String userName, String userDescription,String password,String email){

        this.os.identity().users().create(Builders.user()
                .name(userName)
                .description(userDescription)
                .password(password)
                .email(email)
                .domainId(this.os.getToken().getUser().getDomainId())
                .build());
    }
}