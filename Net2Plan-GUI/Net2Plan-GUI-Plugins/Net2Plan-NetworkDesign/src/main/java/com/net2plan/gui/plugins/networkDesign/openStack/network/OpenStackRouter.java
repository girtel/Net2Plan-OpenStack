package com.net2plan.gui.plugins.networkDesign.openStack.network;

import com.net2plan.gui.plugins.networkDesign.openStack.OpenStackNet;
import com.net2plan.gui.plugins.networkDesign.openStack.OpenStackNetworkElement;
import com.net2plan.interfaces.networkDesign.Node;
import java.awt.geom.Point2D;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import org.openstack4j.model.network.ExternalGateway;
import org.openstack4j.model.network.HostRoute;
import org.openstack4j.model.network.Router;
import org.openstack4j.model.network.State;

/**
 *
 * @author Manuel
 */
public class OpenStackRouter extends OpenStackNetworkElement
{
    final Node npNode;

    private String routerId;
    private String routerName;
    private String routerTenantId;
    private State routerState;
    private boolean routerIsAdminStateUp;
    private boolean routerIsDistributed;
    private List<? extends HostRoute> routerRoutes;
    private ExternalGateway routerExternalGatewayInfo;
    private Router osRouter;

    public static OpenStackRouter createFromAddRouter (OpenStackNet osn ,Router router)
    {
        final Node npNode2 = osn.getNetPlan().addNode(0,0,"",null);
        npNode2.setName(router.getId());

        if(router.getName().equals("router1")) {
            try {
                System.out.println("Nsme");
                npNode2.setUrlNodeIcon( osn.getNetPlan().getNetworkLayerDefault(),new URL("https://findicons.com/files/icons/1035/human_o2/128/router_gnome_netstatus_75_100.png"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else{
            try {
                System.out.println("Nsme");
                npNode2.setUrlNodeIcon( osn.getNetPlan().getNetworkLayerDefault(),new URL("http://www.myiconfinder.com/uploads/iconsets/256-256-ab5e2d6a7b779ce5a246fb00a5f163f6-router.png"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        final OpenStackRouter res = new OpenStackRouter(osn,npNode2,router);
        res.routerId = router.getId();
        res.routerName = router.getName();
        res.routerTenantId = router.getTenantId();
        res.routerState = router.getStatus();
        res.routerIsAdminStateUp = router.isAdminStateUp();
        res.routerIsDistributed = router.getDistributed();
        res.routerRoutes = router.getRoutes();
        res.routerExternalGatewayInfo = router.getExternalGatewayInfo();
        return res;
    }
    public OpenStackRouter(OpenStackNet osn, Node npNode, Router router)
    {
        super (osn , npNode , (List<OpenStackNetworkElement>) (List<?>) osn.openStackRouters);
        this.npNode = npNode;
        this.osRouter = router;
    }


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
        return "Router: " + this.getId();
    }

    public void setName (String value) {

        try{

        this.osn.getOSClientV3().networking().router().update(osRouter.toBuilder().name(value).build());

        }catch(Exception ex){

        logPanel();
        System.out.println(ex.toString());

        }

    }
    public void setRouterTenantId (String value) { this.osn.getOSClientV3().networking().router().update(osRouter.toBuilder().tenantId(value).build()); }
    public void isAdminStateUp (boolean value) {
        try{

        this.osn.getOSClientV3().networking().router().update(osRouter.toBuilder().adminStateUp(value).build());

        }catch(Exception ex){

        logPanel();
        System.out.println(ex.toString());

        }
    }
    public void isDistributed (boolean value) {
        try{
        this.osn.getOSClientV3().networking().router().update(osRouter.toBuilder().distributed(value).build());

        }catch(Exception ex){

            logPanel();
            System.out.println(ex.toString());

        }
        }
    public void setRouterExternalGatewayInfo (ExternalGateway value) { this.osn.getOSClientV3().networking().router().update(osRouter.toBuilder().externalGateway(value).build()); }
}