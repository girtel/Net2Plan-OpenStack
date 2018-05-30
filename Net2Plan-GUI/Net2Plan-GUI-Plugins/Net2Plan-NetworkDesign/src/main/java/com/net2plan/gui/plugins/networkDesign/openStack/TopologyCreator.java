package com.net2plan.gui.plugins.networkDesign.openStack;


import com.net2plan.gui.plugins.GUINetworkDesign;
import com.net2plan.interfaces.networkDesign.Net2PlanException;
import java.util.List;
import org.openstack4j.api.OSClient.OSClientV3;
import org.openstack4j.model.common.Identifier;
import org.openstack4j.model.identity.v3.User;
import org.openstack4j.model.network.Network;
import org.openstack4j.model.network.Port;
import org.openstack4j.model.network.Router;
import org.openstack4j.model.network.Subnet;
import org.openstack4j.openstack.OSFactory;

/**
 *
 * @author Manuel
 */
class TopologyCreator
{
    private final OSClientV3 os;
    private final GUINetworkDesign callback;

    TopologyCreator(GUINetworkDesign callback, String os_auth_url, String os_username, String os_password, String os_project_name, String os_user_domain_name , String os_project_domain_id)
    {

            os = OSFactory.builderV3()
                    .endpoint(os_auth_url)
                    .credentials(os_username, os_password, Identifier.byName(os_user_domain_name))
                    .scopeToProject(Identifier.byName(os_project_name), Identifier.byId(os_project_domain_id))
                    .authenticate();

            this.callback = callback;
    }


    OpenStackNet getOpenStackNet()
    {
        /* Empty NetPlan */
        final OpenStackNet osn = new OpenStackNet(callback,os);

        /* Get elements of Network(NEUTRON)*/
        final List<Network> networks = (List<Network>) os.networking().network().list();
        final List<Subnet> subnets = (List<Subnet>) os.networking().subnet().list();
        final List<Router> routers = (List<Router>) os.networking().router().list();
        final List<Port> ports = (List<Port>) os.networking().port().list();


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

        if (routers.isEmpty()) throw new Net2PlanException("The OpenStack topology is empty");

        osn.distributeTopologyOverCircle();

        return osn;
    }


}

