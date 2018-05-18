package com.net2plan.gui.plugins.networkDesign.openStack;

import com.net2plan.interfaces.networkDesign.Node;
import java.awt.geom.Point2D;
import java.util.List;
import org.openstack4j.model.network.ExternalGateway;
import org.openstack4j.model.network.HostRoute;
import org.openstack4j.model.network.State;

/**
 *
 * @author Manuel
 */
public class OpenStackRouter extends OpenStackNetworkElement
{
    final Node npNode;

    private String osn_nodeId = "";
    private String osn_nodeName = "";
    private String osn_nodeTenantId = "";

    static OpenStackRouter createFromAddNode (OpenStackNet osn ,String nodeId,String nodeName,String nodeTenantId, State nodeStatus,boolean nodeIsAdminStateUp,boolean nodeDistributed,List<? extends HostRoute> nodeRoutes, ExternalGateway nodeExternalGatewayInfo)
    {
        final Node npNode2 = osn.getNetPlan().addNode(0,0,"",null);
        npNode2.setName(nodeId);
        final OpenStackRouter res = new OpenStackRouter(osn,npNode2);
        res.osn_nodeId = nodeId;
        res.osn_nodeName = nodeName;
        res.osn_nodeTenantId = nodeTenantId;
        return res;
    }
    public OpenStackRouter(OpenStackNet osn, Node npNode)
    {
        super (osn , npNode , (List<OpenStackNetworkElement>) (List<?>) osn.list_osRouters);
        this.npNode = npNode;
    }

    Node getNpNode () { return npNode; }

    @Override
    public String getId () { return osn_nodeId; }

    public void setXYPositionMap (Point2D pos) { npNode.setXYPositionMap(pos); }



    @Override
    public String get50CharactersDescription()
    {
        return "Node-" + this.getId();
    }
}
