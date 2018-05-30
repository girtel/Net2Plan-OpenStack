package com.net2plan.gui.plugins.networkDesign.openStack.network;


import com.net2plan.gui.plugins.networkDesign.openStack.OpenStackNet;
import com.net2plan.gui.plugins.networkDesign.openStack.OpenStackNetworkElement;
import com.net2plan.interfaces.networkDesign.Resource;
import java.util.List;

import com.sun.tools.internal.ws.wsdl.document.soap.SOAPUse;
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


    @Override
    public String get50CharactersDescription()
    {
        return "Subnet: " + this.getId();
    }

    public void setName (String value) {

        try{
            this.osn.getOSClientV3().networking().subnet().update(osSubnet.toBuilder().name(value).build());

        }catch(Exception ex){

        logPanel();
        System.out.println(ex.toString());

        }
    }
    public void setSubnetGateway (String value) { this.osn.getOSClientV3().networking().subnet().update(osSubnet.toBuilder().gateway(value).build());}
    public void setSubnetNetworkId (String value) { this.osn.getOSClientV3().networking().subnet().update(osSubnet.toBuilder().networkId(value).build());}
    public void setSubnetIpVersion (IPVersionType value) { this.osn.getOSClientV3().networking().subnet().update(osSubnet.toBuilder().ipVersion(value).build());}
    public void setSubnetTenantId (String value) {this.osn.getOSClientV3().networking().subnet().update(osSubnet.toBuilder().tenantId(value).build()); }
    public void isSubnetIsDHCPEnabled (boolean value) {

        try{

            this.osn.getOSClientV3().networking().subnet().update(osSubnet.toBuilder().enableDHCP(value).build());

        }catch(Exception ex){

            logPanel();
            System.out.println(ex.toString());

        }

    }
    public void setSubnetIpv6AddressMode (Ipv6AddressMode value) { this.osn.getOSClientV3().networking().subnet().update(osSubnet.toBuilder().ipv6AddressMode(value).build()); }
    public void setSubnetIpv6RaMode (Ipv6RaMode value) { this.osn.getOSClientV3().networking().subnet().update(osSubnet.toBuilder().ipv6RaMode(value).build());}


}

