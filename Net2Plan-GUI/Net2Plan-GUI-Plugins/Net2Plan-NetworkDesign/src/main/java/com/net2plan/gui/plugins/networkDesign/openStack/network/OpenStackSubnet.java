package com.net2plan.gui.plugins.networkDesign.openStack.network;

import com.net2plan.gui.plugins.networkDesign.openStack.OpenStackNet;
import com.net2plan.gui.plugins.networkDesign.openStack.OpenStackNetworkElement;
import org.openstack4j.api.Builders;
import org.openstack4j.model.network.*;

import java.util.List;

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

   public static OpenStackSubnet createFromAddSubnet (OpenStackNet osn , String subnetId, String subnetName, List<? extends Pool> subnetAllocationPools, String subnetCidr, List<String> subnetDnsNames, String subnetGateway, List<? extends HostRoute> subnetHostRoutes, IPVersionType subnetIpVersion, Ipv6AddressMode subnetIpv6AddressMode, Ipv6RaMode subnetIpv6RaMode, String subnetNetworkId, String subnetTenantId, boolean subnetIsDHCPEnabled)
    {

        final OpenStackSubnet res = new OpenStackSubnet(osn);
        res.subnetId = subnetId;
        res.subnetName =subnetName;
        res.subnetCidr = subnetCidr;
        res.subnetGateway = subnetGateway;
        res.subnetNetworkId =subnetNetworkId;

        res.subnetAllocationPools = subnetAllocationPools;
        res.subnetDnsNames =subnetDnsNames;
        res.subnetHostRoutes = subnetHostRoutes;
        res.subnetIpVersion = subnetIpVersion;
        res.subnetTenantId =subnetTenantId;
        res.subnetIsDHCPEnabled = subnetIsDHCPEnabled;
        res.subnetIpv6AddressMode = subnetIpv6AddressMode;
        res.subnetIpv6RaMode =subnetIpv6RaMode;

        return res;
    }

    public OpenStackSubnet(OpenStackNet osn)
    {
        super (osn , null , (List<OpenStackNetworkElement>) (List<?>) osn.list_osSubnets);
       // this.osSubnet = this.osn.getOs().networking().subnet().get(this.subnetId);
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
        return "Subnet" + this.getId();
    }

    public void setName (String value) { this.osn.getOs().networking().subnet().update(osSubnet.toBuilder().name(value).build());}
    public void setSubnetGateway (String value) { this.osn.getOs().networking().subnet().update(osSubnet.toBuilder().gateway(value).build());}
    public void setSubnetNetworkId (String value) { this.osn.getOs().networking().subnet().update(osSubnet.toBuilder().networkId(value).build());}
    public void setSubnetIpVersion (IPVersionType value) { this.osn.getOs().networking().subnet().update(osSubnet.toBuilder().ipVersion(value).build());}
    public void setSubnetTenantId (String value) {this.osn.getOs().networking().subnet().update(osSubnet.toBuilder().tenantId(value).build()); }
    public void isSubnetIsDHCPEnabled (boolean value) {this.osn.getOs().networking().subnet().update(osSubnet.toBuilder().enableDHCP(value).build()); }
    public void setSubnetIpv6AddressMode (Ipv6AddressMode value) { this.osn.getOs().networking().subnet().update(osSubnet.toBuilder().ipv6AddressMode(value).build()); }
    public void setSubnetIpv6RaMode (Ipv6RaMode value) { this.osn.getOs().networking().subnet().update(osSubnet.toBuilder().ipv6RaMode(value).build());}
}

