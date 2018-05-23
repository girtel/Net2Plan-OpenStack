package com.net2plan.gui.plugins.networkDesign.openStack.compute;

import com.net2plan.gui.plugins.networkDesign.openStack.OpenStackNet;
import com.net2plan.gui.plugins.networkDesign.openStack.OpenStackNetworkElement;

import java.util.List;

public class OpenStackFloatingIpDns  extends OpenStackNetworkElement
{


    private String floatingIPId;
    private String floatingIPInstanceId;
    private String floatingIPPool;
    private String floatingIPFloatingIpAddress;
    private String floatingIPFixedIpAddress;

    public static OpenStackFloatingIpDns createFromAddFloatingIp (OpenStackNet osn , String floatingIPId, String floatingIPInstanceId, String floatingIPPool, String floatingIPFloatingIpAddress, String floatingIPFixedIpAddress)
    {
        final OpenStackFloatingIpDns res = new OpenStackFloatingIpDns(osn);
        res.floatingIPId= floatingIPId;
        res.floatingIPInstanceId=floatingIPInstanceId;
        res.floatingIPPool=floatingIPPool;
        res.floatingIPFloatingIpAddress=floatingIPFloatingIpAddress;
        res.floatingIPFixedIpAddress=floatingIPFixedIpAddress;
        return res;
    }

    private OpenStackFloatingIpDns (OpenStackNet osn )
    {
        super (osn ,  null, (List<OpenStackNetworkElement>) (List<?>) osn.list_osFloatingIpDns);
    }

    @Override
    public String getId () { return this.floatingIPId; }
    public String getFloatingIPInstanceId () { return this.floatingIPInstanceId; }
    public String getFloatingIPPool () { return this.floatingIPPool; }
    public String getFloatingIPFloatingIpAddress () { return this.floatingIPFloatingIpAddress; }
    public String getFloatingIPFixedIpAddress () { return this.floatingIPFixedIpAddress; }


    @Override
    public String get50CharactersDescription()
    {
        return "FloatingIP" + this.getId();
    }


}
