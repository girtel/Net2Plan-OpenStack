package com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.compute;

import com.net2plan.gui.plugins.GUINetworkDesign;
import com.net2plan.gui.plugins.networkDesign.openStack.compute.OpenStackServer;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.ViewEditTopologyTablesPane;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.AdvancedJTable_networkElement;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.AjtColumnInfo;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.AjtRcMenu;
import org.openstack4j.model.compute.*;

import java.util.*;

public class AdvancedJTable_servers extends AdvancedJTable_networkElement<OpenStackServer>
{
    public AdvancedJTable_servers(GUINetworkDesign callback)
    {
        super(callback, ViewEditTopologyTablesPane.AJTableType.SERVERS , true);
    }

    @Override
    public List<AjtColumnInfo<OpenStackServer>> getNonBasicUserDefinedColumnsVisibleOrNot()
    {

        final List<AjtColumnInfo<OpenStackServer>> res = new LinkedList<>();
        res.add(new AjtColumnInfo<OpenStackServer>(this, String.class, null, "ID", "Server ID", null, n -> n.getId(), AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackServer>(this, String.class, null, "Name", "Server name", null, n -> n.getServerName(), AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackServer>(this, String.class, null, "IPv4", "Server Access IPv4", null, n -> n.getServerAccessIPv4(), AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackServer>(this, String.class, null, "IPv6", "Server Access IPv6", null, n -> n.getServerAccessIPv6(), AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackServer>(this, Addresses.class, null, "Addresses", "Server Addresses", null, n -> n.getServerAddresses(), AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackServer>(this, String.class, null, "Admin pass", "Server admin pass", null, n -> n.getServerAdminPass(), AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackServer>(this, String.class, null, "Availability Zone", "Server Availability Zone", null, n -> n.getServerAvailabilityZone(), AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackServer>(this, String.class, null, "Config Drive", "Server Config Drive", null, n -> n.getServerConfigDrive(), AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackServer>(this, Date.class, null, "Created at", "Server created at", null, n -> n.getServerCreated(), AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackServer>(this, Server.DiskConfig.class, null, "Disk config", "Server Disk config", null, n -> n.getServerDiskConfig(), AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackServer>(this, Fault.class, null, "Fault", "Server fault", null, n -> n.getServerFault(), AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackServer>(this, Flavor.class, null, "Flavor", "Server flavor", null, n -> n.getServerFlavor(), AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackServer>(this, String.class, null, "Flavor ID", "Server flavor ID", null, n -> n.getServerFlavorId(), AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackServer>(this, String.class, null, "Host", "Server host", null, n -> n.getServerHost(), AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackServer>(this, String.class, null, "Host ID", "Server host ID", null, n -> n.getServerHostId(), AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackServer>(this, String.class, null, "Hostname", "Hypervisor hostname", null, n -> n.getServerHypervisorHostname(), AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackServer>(this, Image.class, null, "Image", "Server image", null, n -> n.getServerImage(), AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackServer>(this, String.class, null, "Image ID", "Server image ID", null, n -> n.getServerImageId(), AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackServer>(this, String.class, null, "Key name", "Server key name", null, n -> n.getServerKeyName(), AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackServer>(this, Date.class, null, "Launched at", "Server launched at", null, n -> n.getServerLaunchedAt(), AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackServer>(this, Map.class, null, "Links", "Server links", null, n -> n.getServerLinks(), AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackServer>(this, List.class, null, "Metadata", "Server metadata", null, n -> n.getServerMetadata(), AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackServer>(this, List.class, null, "Volumes", "Server volumes attached", null, n -> n.getServerOsExtendedVolumesAttached(), AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackServer>(this, String.class, null, "State", "Server state", null, n -> n.getServerPowerState(), AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackServer>(this, Integer.class, null, "Progress", "Server progress", null, n -> n.getServerProgress(), AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackServer>(this, List.class, null, "Security groups", "Server security groups", null, n -> n.getServerSecurityGroups(), AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackServer>(this, Server.Status.class, null, "Status", "Server status", null, n -> n.getServerStatus(), AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackServer>(this, String.class, null, "Task state", "Server task state", null, n -> n.getServerTaskState(), AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackServer>(this, String.class, null, "Tenant ID", "Server tenant id", null, n -> n.getServerTenantId(), AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackServer>(this, Date.class, null, "Terminated At", "Server terminated at", null, n -> n.getServerTerminatedAt(), AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackServer>(this, Date.class, null, "Update", "Server update", null, n -> n.getServerUpdate(), AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackServer>(this, String.class, null, "User ID", "Server user id", null, n -> n.getServerUserId(), AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackServer>(this, String.class, null, "Uuid", "Server Uuid", null, n -> n.getServerUuid(), AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackServer>(this, String.class, null, "Vm state", "Server Uuid", null, n -> n.getServerVmState(), AGTYPE.NOAGGREGATION, null, null));




        return res;
    }


    @Override
    public List<AjtRcMenu> getNonBasicRightClickMenusInfo()
    {final List<AjtRcMenu> res = new ArrayList<>();


        res.add(new AjtRcMenu("Add server", e -> addServer(), (a, b) -> true, null));



        return res;

    }
    public void addServer(){

      //  callback.getOpenStackNet().getOpenStackNetCreate().createOpenStackServer("name","flavorid","imageid","keypairid","userdata",null);
    }



}