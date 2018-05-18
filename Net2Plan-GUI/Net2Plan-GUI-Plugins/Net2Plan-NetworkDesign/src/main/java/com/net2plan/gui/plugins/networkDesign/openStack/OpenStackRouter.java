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

    private String routerId = "";
    private String routerName = "";
    private String routerTenantId = "";
    private State routerState;
    private boolean routerIsAdminStateUp;
    private boolean routerIsDistributed;
    private List<? extends HostRoute> routerRoutes;
    private ExternalGateway routerExternalGatewayInfo;


    static OpenStackRouter createFromAddNode (OpenStackNet osn ,String routerId,String routerName,String routerTenantId, State routerState,boolean routerIsAdminStateUp,boolean routerIsDistributed,List<? extends HostRoute> routerRoutes, ExternalGateway routerExternalGatewayInfo)
    {
        final Node npNode2 = osn.getNetPlan().addNode(0,0,"",null);
        npNode2.setName(routerId);
        final OpenStackRouter res = new OpenStackRouter(osn,npNode2);
        res.routerId = routerId;
        res.routerName = routerName;
        res.routerTenantId = routerTenantId;
        res.routerState = routerState;
        res.routerIsAdminStateUp = routerIsAdminStateUp;
        res.routerIsDistributed = routerIsDistributed;
        res.routerRoutes = routerRoutes;
        res.routerExternalGatewayInfo = routerExternalGatewayInfo;
        return res;
    }
    public OpenStackRouter(OpenStackNet osn, Node npNode)
    {
        super (osn , npNode , (List<OpenStackNetworkElement>) (List<?>) osn.list_osRouters);
        this.npNode = npNode;
    }

    Node getNpNode () { return npNode; }

    @Override
    public String getId () { return routerId; }
    public String getRouterName () { return routerName; }
    public String getRouterTenantId () { return routerTenantId; }
    public State getRouterState () { return routerState; }
    public boolean isRouterIsAdminStateUp () { return routerIsAdminStateUp; }
    public boolean isRouterIsDistributed () { return routerIsDistributed; }
    public List<? extends HostRoute> getRouterRoutes () { return routerRoutes; }
    public ExternalGateway getRouterExternalGatewayInfo () { return routerExternalGatewayInfo; }

    public void setXYPositionMap (Point2D pos) { npNode.setXYPositionMap(pos); }



    @Override
    public String get50CharactersDescription()
    {
        return "Node-" + this.getId();
    }
}
