package com.net2plan.gui.plugins.networkDesign.openStack.compute;

import com.net2plan.gui.plugins.networkDesign.openStack.OpenStackNet;
import com.net2plan.gui.plugins.networkDesign.openStack.OpenStackNetworkElement;
import org.openstack4j.model.common.Link;
import org.openstack4j.model.compute.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class OpenStackServer  extends OpenStackNetworkElement
{


    private String serverId;
    private String serverName;
    private String serverAccessIPv4;
    private String serverAccessIPv6;
    private Addresses serverAddresses;
    private String serverAdminPass;
    private String serverAvailabilityZone;
    private String serverConfigDrive;
    private Date serverCreated;
    private Server.DiskConfig serverDiskConfig;
    private Fault serverFault;
    private Flavor serverFlavor;
    private String serverFlavorId;
    private String serverHost;
    private String serverHostId;
    private String serverHypervisorHostname;
    private Image serverImage;
    private String serverImageId;
    private String serverInstanceName;
    private String serverKeyName;
    private Date serverLaunchedAt;
    private Map<String,String> serverMetadata;
    private List<? extends Link> serverLinks;
    private List<String> serverOsExtendedVolumesAttached;
    private String serverPowerState;
    private Integer serverProgress;
    private List<? extends SecurityGroup> serverSecurityGroups;
    private Server.Status serverStatus;
    private String serverTaskState;
    private String serverTenantId;
    private Date serverTerminatedAt;
    private Date serverUpdate;
    private String serverUserId;
    private String serverUuid;
    private String serverVmState;

    public static OpenStackServer createFromAddServer (OpenStackNet osn , String serverId, String serverName, String serverAccessIPv4, String serverAccessIPv6, Addresses serverAddresses, String serverAdminPass, String serverAvailabilityZone, String serverConfigDrive, Date serverCreated, Server.DiskConfig serverDiskConfig, Fault serverFault, Flavor serverFlavor, String serverFlavorId, String serverHost, String serverHostId, String serverHypervisorHostname, Image serverImage, String serverImageId, String serverInstanceName, String serverKeyName, Date serverLaunchedAt, Map<String,String> serverMetadata, List<? extends Link> serverLinks, List<String> serverOsExtendedVolumesAttached, String serverPowerState, Integer serverProgress, List<? extends SecurityGroup> serverSecurityGroups, Server.Status serverStatus, String serverTaskState, String serverTenantId, Date serverTerminatedAt, Date serverUpdate, String serverUserId, String serverUuid, String serverVmState)
    {
        final OpenStackServer res = new OpenStackServer(osn);
        res.serverId= serverId;
        res.serverName=serverName;
        res.serverAccessIPv4=serverAccessIPv4;
        res.serverAccessIPv6=serverAccessIPv6;
        res.serverAddresses=serverAddresses;
        res.serverAdminPass=serverAdminPass;
        res.serverAvailabilityZone= serverAvailabilityZone;
        res.serverConfigDrive=serverConfigDrive;
        res.serverCreated=serverCreated;
        res.serverDiskConfig=serverDiskConfig;
        res.serverFault=serverFault;
        res.serverFlavor=serverFlavor;
        res.serverFlavorId= serverFlavorId;
        res.serverHost=serverHost;
        res.serverHostId=serverHostId;
        res.serverHypervisorHostname=serverHypervisorHostname;
        res.serverImage=serverImage;
        res.serverImageId=serverImageId;
        res.serverInstanceName= serverInstanceName;
        res.serverKeyName=serverKeyName;
        res.serverLaunchedAt=serverLaunchedAt;
        res.serverMetadata=serverMetadata;
        res.serverLinks=serverLinks;
        res.serverOsExtendedVolumesAttached=serverOsExtendedVolumesAttached;
        res.serverPowerState=serverPowerState;
        res.serverProgress=serverProgress;
        res.serverSecurityGroups=serverSecurityGroups;
        res.serverStatus=serverStatus;
        res.serverTaskState=serverTaskState;
        res.serverTenantId= serverTenantId;
        res.serverTerminatedAt=serverTerminatedAt;
        res.serverUpdate=serverUpdate;
        res.serverUserId=serverUserId;
        res.serverUuid=serverUuid;
        res.serverVmState=serverVmState;
        return res;
    }

    private OpenStackServer (OpenStackNet osn )
    {
        super (osn ,  null, (List<OpenStackNetworkElement>) (List<?>) osn.list_osServers);
    }

    @Override
    public String getId () { return this.serverId; }
    public String getServerName () { return this.serverName; }
    public String getServerAccessIPv4 () { return this.serverAccessIPv4; }
    public String getServerAccessIPv6 () { return this.serverAccessIPv6; }
    public Addresses getServerAddresses () { return this.serverAddresses; }
    public String getServerAdminPass () { return this.serverAdminPass; }


    @Override
    public String get50CharactersDescription()
    {
        return "Server" + this.getId();
    }


}
