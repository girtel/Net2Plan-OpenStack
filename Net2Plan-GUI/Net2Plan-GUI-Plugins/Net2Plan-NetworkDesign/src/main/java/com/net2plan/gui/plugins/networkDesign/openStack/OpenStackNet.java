package com.net2plan.gui.plugins.networkDesign.openStack;


import com.google.common.collect.Lists;
import com.net2plan.gui.plugins.GUINetworkDesign;
import com.net2plan.gui.plugins.networkDesign.openStack.network.*;
import com.net2plan.interfaces.networkDesign.NetPlan;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.openstack4j.api.OSClient.OSClientV3;
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

    /*OpenStackNetworkElements of Neutron*/
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

    public List<OpenStackNetwork> getOpenStackNetworks () { return Collections.unmodifiableList(openStackNetworks); }
    public List<OpenStackSubnet> getOpenStackSubnets () { return Collections.unmodifiableList(openStackSubnets); }
    public List<OpenStackRouter> getOpenStackRouters () { return Collections.unmodifiableList(openStackRouters); }
    public List<OpenStackPort> getOpenStackPorts () { return Collections.unmodifiableList(openStackPorts); }

    public OpenStackNetworkElement getOpenStackNetworkElementByOpenStackId (String openStackId)
    {
        final List<OpenStackNetworkElement> allOpenStackNetworkElements = Lists.newArrayList();
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

    public void updateRouterTable(){
        openStackRouters.clear();

        callback.getDesign().removeAllNodes();
        List<Router> routers = (List<Router>) os.networking().router().list();
        for (Router router : routers)
            addOpenStackRouter(router);

        distributeTopologyOverCircle();
    }

    public void refreshListTable(){
        openStackNetworks.clear();
        openStackRouters.clear();
        openStackPorts.clear();
        openStackSubnets.clear();
        callback.getDesign().removeAllNodes();
        /* Get elements of Network(NEUTRON)*/
        final List<Network> networks = (List<Network>) os.networking().network().list();
        final List<Subnet> subnets = (List<Subnet>) os.networking().subnet().list();
        final List<Router> routers = (List<Router>) os.networking().router().list();
        final List<Port> ports = (List<Port>) os.networking().port().list();

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