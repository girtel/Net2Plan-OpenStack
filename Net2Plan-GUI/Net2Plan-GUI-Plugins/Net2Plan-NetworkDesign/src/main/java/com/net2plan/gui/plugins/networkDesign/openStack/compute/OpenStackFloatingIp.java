package com.net2plan.gui.plugins.networkDesign.openStack.compute;

import com.net2plan.gui.plugins.networkDesign.openStack.OpenStackClient;
import com.net2plan.gui.plugins.networkDesign.openStack.OpenStackNet;
import com.net2plan.gui.plugins.networkDesign.openStack.OpenStackNetworkElement;
import org.openstack4j.model.compute.FloatingIP;
import sun.plugin2.os.windows.FLASHWINFO;

import java.util.List;

public class OpenStackFloatingIp extends OpenStackNetworkElement
{


    private String floatingIPId;
    private String floatingIPInstanceId;
    private String floatingIPPool;
    private String floatingIPFloatingIpAddress;
    private String floatingIPFixedIpAddress;
    private FloatingIP osFloatingIP;
    private OpenStackClient openStackClient;
    public static OpenStackFloatingIp createFromAddFloatingIp (OpenStackNet osn ,FloatingIP floatingIP,OpenStackClient openStackClient)
    {
        final OpenStackFloatingIp res = new OpenStackFloatingIp(osn, floatingIP,openStackClient);
        res.floatingIPId= floatingIP.getId();
        res.floatingIPInstanceId=floatingIP.getInstanceId();
        res.floatingIPPool=floatingIP.getPool();
        res.floatingIPFloatingIpAddress=floatingIP.getFloatingIpAddress();
        res.floatingIPFixedIpAddress=floatingIP.getFixedIpAddress();
        return res;
    }

    private OpenStackFloatingIp(OpenStackNet osn, FloatingIP floatingIP, OpenStackClient openStackClient )
    {
        super (osn ,  null, (List<OpenStackNetworkElement>) (List<?>) openStackClient.openStackFloatingIps);
        this.openStackClient=openStackClient;
        this.osFloatingIP = floatingIP;
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
        String description = "Floating IP: " +
                this.NEWLINE + "ID " + this.getId() +
                this.NEWLINE + "Pool " + this.getFloatingIPPool() +
                this.NEWLINE + "Instance/Server ID " + this.getFloatingIPInstanceId() +
                this.NEWLINE + "Fixed IP Address" + this.getFloatingIPFixedIpAddress() +
                this.NEWLINE + "Floating IP Address " + this.getFloatingIPFloatingIpAddress() +
                this.NEWLINE + "Links" + this.NEWLINE;

        return description;
    }


}
