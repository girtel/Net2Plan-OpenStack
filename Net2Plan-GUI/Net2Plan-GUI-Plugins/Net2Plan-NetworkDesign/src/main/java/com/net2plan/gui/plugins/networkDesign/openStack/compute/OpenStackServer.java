package com.net2plan.gui.plugins.networkDesign.openStack.compute;

import com.net2plan.gui.plugins.networkDesign.openStack.OpenStackClient;
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
    private Server osServer;
    private OpenStackClient openStackClient;
    public static OpenStackServer createFromAddServer (OpenStackNet osn , Server server,OpenStackClient openStackClient)
    {
        final OpenStackServer res = new OpenStackServer(osn,server,openStackClient);
        res.serverId= server.getId();
        res.serverName=server.getName();
        res.serverAccessIPv4=server.getAccessIPv4();
        res.serverAccessIPv6=server.getAccessIPv6();
        res.serverAddresses=server.getAddresses();
        res.serverAdminPass=server.getAdminPass();
        res.serverAvailabilityZone= server.getAvailabilityZone();
        res.serverConfigDrive=server.getConfigDrive();
        res.serverCreated=server.getCreated();
        res.serverDiskConfig=server.getDiskConfig();
        res.serverFault=server.getFault();
        res.serverFlavor=server.getFlavor();
        res.serverFlavorId= server.getFlavorId();
        res.serverHost=server.getHost();
        res.serverHostId=server.getHostId();
        res.serverHypervisorHostname=server.getHypervisorHostname();
        res.serverImage=server.getImage();
        res.serverImageId=server.getImageId();
        res.serverInstanceName= server.getInstanceName();
        res.serverKeyName=server.getKeyName();
        res.serverLaunchedAt=server.getLaunchedAt();
        res.serverMetadata=server.getMetadata();
        res.serverLinks=server.getLinks();
        res.serverOsExtendedVolumesAttached=server.getOsExtendedVolumesAttached();
        res.serverPowerState=server.getPowerState();
        res.serverProgress=server.getProgress();
        res.serverSecurityGroups=server.getSecurityGroups();
        res.serverStatus=server.getStatus();
        res.serverTaskState=server.getTaskState();
        res.serverTenantId= server.getTenantId();
        res.serverTerminatedAt=server.getTerminatedAt();
        res.serverUpdate=server.getCreated();
        res.serverUserId=server.getUserId();
        res.serverUuid=server.getUuid();
        res.serverVmState=server.getVmState();
        return res;
    }

    private OpenStackServer (OpenStackNet osn, Server server , OpenStackClient openStackClient)
    {
        super (osn ,  null, (List<OpenStackNetworkElement>) (List<?>) openStackClient.openStackServers);
        this.osServer = server;
        this.openStackClient=openStackClient;
    }

    @Override
    public String getId () { return this.serverId; }
    public String getServerName () { return this.serverName; }
    public String getServerAccessIPv4 () { return this.serverAccessIPv4; }
    public String getServerAccessIPv6 () { return this.serverAccessIPv6; }
    public Addresses getServerAddresses () { return this.serverAddresses; }
    public String getServerAdminPass () { return this.serverAdminPass; }
    public String getServerAvailabilityZone () { return this.serverAvailabilityZone; }
    public String getServerConfigDrive () { return this.serverConfigDrive; }
    public Date getServerCreated () { return this.serverCreated; }
    public Server.DiskConfig getServerDiskConfig () { return this.serverDiskConfig; }
    public Fault getServerFault () { return this.serverFault; }
    public Flavor getServerFlavor () { return this.serverFlavor; }
    public String getServerFlavorId () { return this.serverFlavorId; }
    public String getServerHost () { return this.serverHost; }
    public String getServerHostId () { return this.serverHostId; }
    public String getServerHypervisorHostname () { return this.serverHypervisorHostname; }
    public Image getServerImage () { return this.serverImage; }
    public String getServerImageId () { return this.serverImageId; }
    public String getServerInstanceName () { return this.serverInstanceName; }
    public String getServerKeyName () { return this.serverKeyName; }
    public Date getServerLaunchedAt () { return this.serverLaunchedAt; }
    public Map<String,String> getServerMetadata () { return this.serverMetadata; }
    public List<? extends Link> getServerLinks () { return this.serverLinks; }
    public List<String> getServerOsExtendedVolumesAttached () { return this.serverOsExtendedVolumesAttached; }
    public String getServerPowerState () { return this.serverPowerState; }
    public Integer getServerProgress () { return this.serverProgress; }
    public List<? extends SecurityGroup> getServerSecurityGroups () { return this.serverSecurityGroups; }
    public Server.Status getServerStatus () { return this.serverStatus; }
    public String getServerTaskState () { return this.serverTaskState; }
    public String getServerTenantId () { return this.serverTenantId; }
    public Date getServerTerminatedAt () { return this.serverTerminatedAt; }
    public Date getServerUpdate () { return this.serverUpdate; }
    public String getServerUserId () { return this.serverUserId; }
    public String getServerUuid () { return this.serverUuid; }
    public String getServerVmState () { return this.serverVmState; }


    @Override
    public String get50CharactersDescription()
    {
        String description = "Server: " +
                this.NEWLINE + "ID " + this.getId() +
                this.NEWLINE + "Name " + this.getServerName() +
                this.NEWLINE + "Access IPv4 " + this.getServerAccessIPv4() +
                this.NEWLINE + "Access IPv6 " + this.getServerAccessIPv6() +
                this.NEWLINE + "Admin pass " + this.getServerAdminPass() +
                this.NEWLINE + "Availability Zone " + this.getServerAvailabilityZone() +
                this.NEWLINE + "Config drive " + this.getServerConfigDrive() +
                this.NEWLINE + "Flavor ID " + this.getServerFlavorId() +
                this.NEWLINE + "Flavor " + this.getServerFlavor() +
                this.NEWLINE + "Host " + this.getServerHost() +
                this.NEWLINE + "Host ID " + this.getServerHostId() +
                this.NEWLINE + "Hypervisor Hostname " + this.getServerHypervisorHostname() +
                this.NEWLINE + "Image ID " + this.getServerImageId() +
                this.NEWLINE + "Instance Name " + this.getServerInstanceName() +
                this.NEWLINE + "Keyname " + this.getServerKeyName() +
                this.NEWLINE + "Power state " + this.getServerPowerState() +
                this.NEWLINE + "Task state " + this.getServerTaskState() +
                this.NEWLINE + "Project/Tenant ID " + this.getServerTenantId() +
                this.NEWLINE + "User ID " + this.getServerUserId() +
                this.NEWLINE + "Uuid " + this.getServerUuid() +
                this.NEWLINE + "VM State " + this.getServerVmState() +
                this.NEWLINE + "Addresses " + this.getServerAddresses() +
                this.NEWLINE + "Created " + this.getServerCreated() +
                this.NEWLINE + "Disk config" + this.getServerDiskConfig() +
                this.NEWLINE + "Fault " + this.getServerFault() +
                this.NEWLINE + "Progress " + this.getServerProgress() +
                this.NEWLINE + "Status " + this.getServerStatus() +
                this.NEWLINE + "Terminated at " + this.getServerTerminatedAt() +
                this.NEWLINE + "Update at " + this.getServerUpdate() +
                this.NEWLINE + "Launched at " + this.getServerLaunchedAt();

        if(this.getServerOsExtendedVolumesAttached()!=null) {
            description += this.NEWLINE + "Volumes Attached" + this.NEWLINE;
            for (String key : this.getServerOsExtendedVolumesAttached()) {
                description += key + " " + NEWLINE;
            }
        }

        if(this.getServerLinks()!=null) {
            description += this.NEWLINE + "Links" + this.NEWLINE;
            for (Link key : this.getServerLinks()) {
                description += key + " " + NEWLINE;
            }
        }

        if(this.getServerMetadata()!=null) {
            description += this.NEWLINE + "Metadata" + this.NEWLINE;
            for (String key : this.getServerMetadata().keySet()) {
                description += key + " " + this.getServerMetadata().get(key) + NEWLINE;
            }
        }

        if(this.getServerSecurityGroups()!=null) {
            description += this.NEWLINE + "Security Groups" + this.NEWLINE;
            for (SecurityGroup key : this.getServerSecurityGroups()) {
                description += key + " " + NEWLINE;
            }
        }
        return description;
    }


}
