package com.net2plan.gui.plugins.networkDesign.openStack;


import com.net2plan.interfaces.networkDesign.Resource;
import java.util.List;
import org.openstack4j.model.network.HostRoute;
import org.openstack4j.model.network.IPVersionType;
import org.openstack4j.model.network.Ipv6AddressMode;
import org.openstack4j.model.network.Ipv6RaMode;
import org.openstack4j.model.network.Pool;

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

    static OpenStackSubnet createFromAddSubnet (OpenStackNet osn ,String subnetId,String subnetName,List<? extends Pool> subnetAllocationPools,String subnetCidr,List<String> subnetDnsNames,String subnetGateway,List<? extends HostRoute> subnetHostRoutes,IPVersionType subnetIpVersion,Ipv6AddressMode subnetIpv6AddressMode,Ipv6RaMode subnetIpv6RaMode,String subnetNetworkId,String subnetTenantId,boolean subnetIsDHCPEnabled)
    {

        final OpenStackSubnet res = new OpenStackSubnet(osn,null);
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

    public OpenStackSubnet(OpenStackNet osn, Resource npDummyResource)
    {
        super (osn , npDummyResource , (List<OpenStackNetworkElement>) (List<?>) osn.list_osSubnets);

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


    OpenStackSubnet getOpenStackSubnet () { return this; }

    @Override
    public String get50CharactersDescription()
    {
        return this.getId();
    }
}

