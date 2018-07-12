package com.net2plan.gui.plugins.networkDesign.openStack.network;


import com.net2plan.gui.plugins.networkDesign.openStack.OpenStackNet;
import com.net2plan.gui.plugins.networkDesign.openStack.OpenStackNetworkElement;
import com.net2plan.interfaces.networkDesign.Node;
import com.net2plan.interfaces.networkDesign.Resource;

import java.awt.geom.Point2D;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;
import org.openstack4j.model.network.*;

/**
 *
 * @author Manuel
 */

public class OpenStackSubnet extends OpenStackNetworkElement
{
    private String subnetId = "";
    private String subnetName = "";
    private String subnetCidr = "";
    private String subnetGateway = "";
    private String subnetNetworkId = "";
    private List<? extends Pool> subnetAllocationPools;
    private List<String> subnetDnsNames;
    private List<? extends HostRoute> subnetHostRoutes;
    private IPVersionType subnetIpVersion;
    private String subnetTenantId;
    private boolean subnetIsDHCPEnabled;
    private Ipv6AddressMode subnetIpv6AddressMode;
    private Ipv6RaMode subnetIpv6RaMode;
    final Node npNode;
    private Subnet osSubnet;

    public static OpenStackSubnet createFromAddSubnet (OpenStackNet osn ,Subnet subnet)
    {

        final OpenStackSubnet res = new OpenStackSubnet(osn, subnet);
        res.subnetId = subnet.getId();
        res.subnetName =subnet.getName();
        res.subnetCidr = subnet.getCidr();
        res.subnetGateway = subnet.getGateway();
        res.subnetNetworkId =subnet.getNetworkId();

        res.subnetAllocationPools = subnet.getAllocationPools();
        res.subnetDnsNames =subnet.getDnsNames();
        res.subnetHostRoutes = subnet.getHostRoutes();
        res.subnetIpVersion = subnet.getIpVersion();
        res.subnetTenantId =subnet.getTenantId();
        res.subnetIsDHCPEnabled = subnet.isDHCPEnabled();
        res.subnetIpv6AddressMode = subnet.getIpv6AddressMode();
        res.subnetIpv6RaMode =subnet.getIpv6RaMode();

        return res;
    }

    public OpenStackSubnet(OpenStackNet osn, Subnet subnet)
    {
        super (osn , null , (List<OpenStackNetworkElement>) (List<?>) osn.openStackSubnets);
        this.osSubnet = subnet;

        Map<String,String> attributes = new HashMap<>();
        attributes.put("rightClick","no");
        final Node npNode2 = osn.getNetPlan().addNode(0, 0, "", attributes);
        npNode2.setName(subnet.getId());


            try {
                npNode2.setUrlNodeIcon(osn.getNetPlan().getNetworkLayerDefault(), new URL(getClass().getResource("/resources/gui/figs/Switch.png").toURI().toURL().toString()));
            } catch (Exception e) {
                e.printStackTrace();
            }

        this.npNode = npNode2;
    }

    @Override
    public String getId () { return this.subnetId; }
    public String getName () { return this.subnetName; }
    public String getSubnetCidr () { return this.subnetCidr; }
    public String getSubnetGateway () { return this.subnetGateway; }
    public String getSubnetNetworkId () { return this.subnetNetworkId; }
    public List<? extends Pool> getSubnetAllocationPools () { return this.subnetAllocationPools; }
    public List<String> getSubnetDnsNames () { return this.subnetDnsNames; }
    public List<? extends HostRoute> getSubnetHostRoutes () { return this.subnetHostRoutes; }
    public IPVersionType getSubnetIpVersion () { return this.subnetIpVersion; }
    public String getSubnetTenantId () { return this.subnetTenantId; }
    public boolean isSubnetIsDHCPEnabled () { return this.subnetIsDHCPEnabled; }
    public Ipv6AddressMode getSubnetIpv6AddressMode () { return this.subnetIpv6AddressMode; }
    public Ipv6RaMode getSubnetIpv6RaMode () { return this.subnetIpv6RaMode; }

    public void setXYPositionMap(Point2D pos) {
        npNode.setXYPositionMap(pos);
    }
    @Override
    public String get50CharactersDescription()
    {
        String description = "Subnet: " +
                this.NEWLINE + "ID " + this.getId() +
                this.NEWLINE + "Name " + this.getName() +
                this.NEWLINE + "CIDR " + this.getSubnetCidr() +
                this.NEWLINE + "Gateway " + this.getSubnetGateway() +
                this.NEWLINE + "Network ID " + this.getSubnetNetworkId() +
                this.NEWLINE + "IP version " + this.getSubnetIpVersion() +
                this.NEWLINE + "Tenant ID " + this.getSubnetTenantId() +
                this.NEWLINE + "DHCP Enable " + this.isSubnetIsDHCPEnabled() +
                this.NEWLINE + "IPv6 Address Mode " + this.getSubnetIpv6AddressMode() +
                this.NEWLINE + "IPv6 Ra Mode " + this.getSubnetIpv6RaMode();

        description += this.NEWLINE + "Pool" + this.NEWLINE;
        for(Pool pool : this.getSubnetAllocationPools()) {
            description += pool + " " + NEWLINE;
        }
        description += this.NEWLINE + "DNS" + this.NEWLINE;
        for(String dns : this.getSubnetDnsNames()) {
            description += dns + " " + NEWLINE;
        }
        description += this.NEWLINE + "Host Route" + this.NEWLINE;
        for(HostRoute hostRoute : this.getSubnetHostRoutes()) {
            description += hostRoute + " " + NEWLINE;
        }
        return description;

    }

    public void setName (JSONObject jsonObject) {

        try{
            this.osn.getOSClientV3().networking().subnet().update(osSubnet.toBuilder().name(jsonObject.getString("Name")).build());

        }catch(Exception ex){

        logPanel();
        System.out.println(ex.toString());

        }
    }
    public void addSubnetDns (JSONObject jsonObject) {

        try {
            this.osn.getOSClientV3().networking().subnet().update(osSubnet.toBuilder().addDNSNameServer(jsonObject.getString("DNS")).build());

        }catch (Exception ex){

            logPanel();
            System.out.println(ex.toString());
        }

    }
    public void addPool (JSONObject jsonObject) {
        try{

            this.osn.getOSClientV3().networking().subnet().update(osSubnet.toBuilder().addPool(prepareIp(jsonObject.getString("Start")),prepareIp(jsonObject.getString("End"))).build());

        }catch(Exception ex){

            logPanel();
            System.out.println(ex.toString());

        }
    }
    public void changeSubnetCidr (JSONObject jsonObject) {
        try{
            this.osn.getOSClientV3().networking().subnet().update(osSubnet.toBuilder().cidr(prepareCidr(jsonObject.getString("CIDR"))).build());


            }catch(Exception ex){

            logPanel();
            System.out.println(ex.toString());

             }
    }
    public void addRoute (JSONObject object) {

        try{

        this.osn.getOSClientV3().networking().subnet().update(osSubnet.toBuilder().addHostRoute(prepareCidr(object.getString("Destination")),prepareCidr(object.getString("Next hop"))).build());


        }catch(Exception ex){

            logPanel();
            System.out.println(ex.toString());

        }

    }
    public void subnetNoGateway () {

        try{

            this.osn.getOSClientV3().networking().subnet().update(osSubnet.toBuilder().noGateway().build());

        }catch(Exception ex){

            logPanel();
            System.out.println(ex.toString());

        }

    }
    public void setSubnetGateway (JSONObject jsonObject) {
        try{
        this.osn.getOSClientV3().networking().subnet().update(osSubnet.toBuilder().gateway(prepareIp(jsonObject.getString("Gateway"))).build());

        }catch(Exception ex){

        logPanel();
        System.out.println(ex.toString());

    }

}
    public void setSubnetNetworkId (JSONObject jsonObject) {

        try{
        this.osn.getOSClientV3().networking().subnet().update(osSubnet.toBuilder().networkId(jsonObject.getString("Network ID")).build());
        }catch(Exception ex){

        logPanel();
        System.out.println(ex.toString());

    }

}
    public void setSubnetIpVersion (JSONObject jsonObject ) {
        try{
        this.osn.getOSClientV3().networking().subnet().update(osSubnet.toBuilder().ipVersion(IPVersionType.valueOf(jsonObject.getString("IP version"))).build());

        }catch(Exception ex){

        logPanel();
        System.out.println(ex.toString());

    }

}
    public void setSubnetTenantId (JSONObject jsonObject) {
        try{
        this.osn.getOSClientV3().networking().subnet().update(osSubnet.toBuilder().tenantId(jsonObject.getString("Project ID")).build());
        }catch(Exception ex){

        logPanel();
        System.out.println(ex.toString());

    }

}
    public void isSubnetIsDHCPEnabled (boolean value) {

        try{

            this.osn.getOSClientV3().networking().subnet().update(osSubnet.toBuilder().enableDHCP(value).build());

        }catch(Exception ex){

            logPanel();
            System.out.println(ex.toString());

        }

    }
    public void setSubnetIpv6AddressMode (Ipv6AddressMode value) {
        try{
        this.osn.getOSClientV3().networking().subnet().update(osSubnet.toBuilder().ipv6AddressMode(value).build());
        }catch(Exception ex){

        logPanel();
        System.out.println(ex.toString());

    }


}
    public void setSubnetIpv6RaMode (Ipv6RaMode value) {

        try{

        this.osn.getOSClientV3().networking().subnet().update(osSubnet.toBuilder().ipv6RaMode(value).build());

    }catch(Exception ex){

    logPanel();
    System.out.println(ex.toString());

}


}
public Node getNpNode(){
        return  npNode;
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

