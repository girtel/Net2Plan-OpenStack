package com.net2plan.gui.plugins.networkDesign.openStack.network;

import com.net2plan.gui.plugins.networkDesign.openStack.OpenStackClient;
import com.net2plan.gui.plugins.networkDesign.openStack.OpenStackNet;
import com.net2plan.gui.plugins.networkDesign.openStack.OpenStackNetworkElement;
import com.net2plan.interfaces.networkDesign.Node;
import java.awt.geom.Point2D;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.json.JSONObject;
import org.openstack4j.model.network.ExternalGateway;
import org.openstack4j.model.network.HostRoute;
import org.openstack4j.model.network.Router;
import org.openstack4j.model.network.State;

/**
 *
 * @author Manuel
 */
public class OpenStackRouter extends OpenStackNetworkElement {
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

    public static OpenStackRouter createFromAddRouter(OpenStackNet osn, Router router,OpenStackClient openStackClient) {
        final Node npNode2 = openStackClient.getNetPlanDesign().addNode(0, 0, "", null);
        npNode2.setName(router.getId());

        if (router.getName().equals("router1")) {
            try {

                npNode2.setUrlNodeIcon(openStackClient.getNetPlanDesign().getNetworkLayerDefault(), new URL("http://aux2.iconspalace.com/uploads/router-icon-256.png"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            try {
                npNode2.setUrlNodeIcon(openStackClient.getNetPlanDesign().getNetworkLayerDefault(), new URL("http://aux2.iconspalace.com/uploads/router-icon-256.png"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        final OpenStackRouter res = new OpenStackRouter(osn, npNode2, router,openStackClient);
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

    public OpenStackRouter(OpenStackNet osn, Node npNode, Router router,OpenStackClient openStackClient) {
        super(osn, npNode, (List<OpenStackNetworkElement>) (List<?>) openStackClient.openStackRouters,openStackClient);
        this.npNode = npNode;
        this.osRouter = router;
    }


    @Override
    public String getId() {
        return routerId;
    }

    public String getRouterName() {
        return routerName;
    }

    public String getRouterTenantId() {
        return routerTenantId;
    }

    public State getRouterState() {
        return routerState;
    }

    public boolean isRouterIsAdminStateUp() {
        return routerIsAdminStateUp;
    }

    public boolean isRouterIsDistributed() {
        return routerIsDistributed;
    }

    public List<? extends HostRoute> getRouterRoutes() {
        return routerRoutes;
    }

    public ExternalGateway getRouterExternalGatewayInfo() {
        return routerExternalGatewayInfo;
    }

    public void setXYPositionMap(Point2D pos) {
        npNode.setXYPositionMap(pos);
    }

    public Node getNpNode() {return this.npNode;}

    @Override
    public String get50CharactersDescription() {


        String description = "Router: " +
                this.NEWLINE + "ID " + this.getId() +
                this.NEWLINE + "Name " + this.getRouterName() +
                this.NEWLINE + "Project/Tenant ID " + this.getRouterTenantId() +
                this.NEWLINE + "State " + this.getRouterState() +
                this.NEWLINE + "Is Distributed " + this.isRouterIsDistributed() +
                this.NEWLINE + "Is Admin State Up " + this.isRouterIsAdminStateUp() +
                this.NEWLINE + "Router external gateway info " + this.getRouterExternalGatewayInfo() +
                this.NEWLINE + "Host Route" + this.NEWLINE;
        for (HostRoute hostRoute : this.getRouterRoutes()) {
            description += hostRoute + " " + NEWLINE;
        }
        return description;
    }

    public void setName(JSONObject jsonObject) {

        try {

            this.openStackClient.getClient().networking().router().update(osRouter.toBuilder().name(jsonObject.getString("Name")).build());

        } catch (Exception ex) {

            logPanel();
            System.out.println(ex.toString());

        }

    }

    public void setRouterTenantId(JSONObject jsonObject) {
        try {
            this.openStackClient.getClient().networking().router().update(osRouter.toBuilder().tenantId(jsonObject.getString("Tenant ID")).build());
        }catch(Exception ex){
            System.out.println(ex.toString());
            logPanel();
        }
    }

    public void isAdminStateUp(boolean value) {
        try {

            this.openStackClient.getClient().networking().router().update(osRouter.toBuilder().adminStateUp(value).build());

        } catch (Exception ex) {

            logPanel();
            System.out.println(ex.toString());

        }
    }

    public void isDistributed(boolean value) {
        try {
            this.openStackClient.getClient().networking().router().update(osRouter.toBuilder().distributed(value).build());

        } catch (Exception ex) {

            logPanel();
            System.out.println(ex.toString());

        }
    }

    public void setRouterExternalGatewayInfo(JSONObject jsonObject) {
        try {
            this.openStackClient.getClient().networking().router().update(osRouter.toBuilder().externalGateway(jsonObject.getString("Network ID"),jsonObject.getBoolean("Snapshot")).build());
        } catch (Exception ex) {
            System.out.println(ex.toString());
            logPanel();
        }
    }
    public void clearRouterExternalGatewayInfo() {
        try {
            this.openStackClient.getClient().networking().router().update(osRouter.toBuilder().clearExternalGateway().build());
        } catch (Exception ex) {
            System.out.println(ex.toString());
            logPanel();
        }
    }
    public void noRoutes() {
        try {
            this.openStackClient.getClient().networking().router().update(osRouter.toBuilder().noRoutes().build());
        } catch (Exception ex) {
            System.out.println(ex.toString());
            logPanel();
        }
    }
    public void addRoute(JSONObject jsonObject) {
        try {
            this.openStackClient.getClient().networking().router().update(osRouter.toBuilder().route(prepareCidr(jsonObject.getString("Destination")),prepareCidr(jsonObject.getString("Next hop"))).build());
        } catch (Exception ex) {
            System.out.println(ex.toString());
            logPanel();
        }
    }

    public String prepareIp(String ip){
        String[] parts = ip.split("-");
        String part1 = String.valueOf(Integer.parseInt(parts[0])); // ###
        String part2 = String.valueOf(Integer.parseInt(parts[1])); //###
        String part3 = String.valueOf(Integer.parseInt(parts[2])); // ###
        String part4 =  String.valueOf(Integer.parseInt(parts[3])); // ###
        String response = part1+"."+part2+"."+part3+"."+part4;

        return response;
    }

    public String prepareCidr(String cidr){

        String[] parts = cidr.split("-");
        String part1 = String.valueOf(Integer.parseInt(parts[0])); // ###
        String part2 = String.valueOf(Integer.parseInt(parts[1])); //###
        String part3 = String.valueOf(Integer.parseInt(parts[2])); // ###
        String part4 = parts[3]; //###/##
        String[] newParts = part4.split("/");
        String part5 = String.valueOf(Integer.parseInt(newParts[0])); // ###
        String part6 = String.valueOf(Integer.parseInt(newParts[1])); //##
        String response = part1+"."+part2+"."+part3+"."+part5+"/"+part6;

        return response;
    }
}