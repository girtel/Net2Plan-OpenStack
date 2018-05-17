package com.net2plan.gui.plugins.networkDesign.openStack;


import com.google.common.collect.Lists;
import com.net2plan.gui.plugins.GUINetworkDesign;
import com.net2plan.interfaces.networkDesign.Link;
import com.net2plan.interfaces.networkDesign.NetPlan;
import com.net2plan.interfaces.networkDesign.Node;
import java.awt.geom.Point2D;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.openstack4j.api.OSClient.OSClientV3;
import org.openstack4j.model.identity.v3.User;
import org.openstack4j.model.network.ExternalGateway;
import org.openstack4j.model.network.HostRoute;
import org.openstack4j.model.network.IPVersionType;
import org.openstack4j.model.network.Ipv6AddressMode;
import org.openstack4j.model.network.Ipv6RaMode;
import org.openstack4j.model.network.NetworkType;
import org.openstack4j.model.network.Pool;
import org.openstack4j.model.network.State;
import org.openstack4j.model.network.Subnet;

/**
 *
 * @author Manuel
 */
public class OpenStackNet
{

    private GUINetworkDesign callback;
    private final NetPlan np;
    private OSClientV3 os;
    final List<OpenStackRouter> list_osRouters = new ArrayList<> ();
    final List<OpenStackUser> list_osUsers = new ArrayList<> ();
    final List<OpenStackNetwork> list_osNetworks = new ArrayList<> ();
    final List<OpenStackSubnet> list_osSubnets = new ArrayList<> ();
    final  NetPlan getNetPlan () { return np; }

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

    public OpenStackNetwork addOpenStackNetwork(String networkId,String networkName,State networkStatus,NetworkType networkType,List<? extends Subnet> networkNeutronSubnets,String networkProviderPhyNet,String networkProviderSegID,List <String> networkSubnets,String networkTenantId,boolean networkIsAdminStateUp,boolean networkIsRouterExternal, boolean networkIsShared)
    {
        final OpenStackNetwork res = OpenStackNetwork.createFromAddNetwork(this , networkId, networkName, networkStatus, networkType, networkNeutronSubnets,networkProviderPhyNet,networkProviderSegID,networkSubnets,networkTenantId,networkIsAdminStateUp,networkIsRouterExternal,networkIsShared);
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

    public OpenStackRouter addOpenStackNode(String nodeId,String nodeName,String nodeTenantId, State nodeStatus,boolean nodeIsAdminStateUp,boolean nodeDistributed,List<? extends HostRoute> nodeRoutes, ExternalGateway nodeExternalGatewayInfo)
    {
        final OpenStackRouter res = OpenStackRouter.createFromAddNode(this,nodeId, nodeName,nodeTenantId, nodeStatus, nodeIsAdminStateUp, nodeDistributed, nodeRoutes,nodeExternalGatewayInfo);
        if(list_osRouters.contains(res)) return res;
        list_osRouters.add(res);
        return res;
    }


    public String getTopologyName () { return np.getNetPlan().getNetworkName(); }
    public String getTopologyDescription () { return np.getNetPlan().getNetworkDescription(); }
    public void setTopologyName (String name) { this.np.getNetPlan().setNetworkName(name); }

    public List<OpenStackRouter> getOpenStackNodes () { return Collections.unmodifiableList(list_osRouters); }
    public List<OpenStackUser> getOpenStackUsers () { return Collections.unmodifiableList(list_osUsers); }
    public List<OpenStackNetwork> getOpenStackNetworks () { return Collections.unmodifiableList(list_osNetworks); }
    public List<OpenStackSubnet> getOpenStackSubnets () { return Collections.unmodifiableList(list_osSubnets); }


    public OpenStackNetworkElement getOpenStackNetworkElementByInternalId (long internalId)
    {
        final List<OpenStackNetworkElement> allOpenStackNetworkElements = Lists.newArrayList();
        allOpenStackNetworkElements.addAll(list_osRouters);
        allOpenStackNetworkElements.addAll(list_osUsers);
        allOpenStackNetworkElements.addAll(list_osNetworks);
        allOpenStackNetworkElements.addAll(list_osSubnets);

        Optional<OpenStackNetworkElement> element = allOpenStackNetworkElements.stream().filter(n->n.getInternalId() == internalId).findFirst();
        if (element.isPresent()) return element.get();
        else return null;
    }


    public OpenStackNetworkElement getOpenStackNetworkElementByOpenStackId (String openStackId)
    {
        final List<OpenStackNetworkElement> allOpenStackNetworkElements = Lists.newArrayList();
        allOpenStackNetworkElements.addAll(list_osRouters);
        allOpenStackNetworkElements.addAll(list_osUsers);
        allOpenStackNetworkElements.addAll(list_osNetworks);
        allOpenStackNetworkElements.addAll(list_osSubnets);

        Optional<OpenStackNetworkElement> element = allOpenStackNetworkElements.stream().filter(n->n.getId() == openStackId).findFirst();
        if (element.isPresent()) return element.get();
        else return null;
    }

    public void distributeTopologyOverCircle()
    {
        // Node list
        final List<OpenStackRouter> nodeList = getOpenStackNodes();

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
}