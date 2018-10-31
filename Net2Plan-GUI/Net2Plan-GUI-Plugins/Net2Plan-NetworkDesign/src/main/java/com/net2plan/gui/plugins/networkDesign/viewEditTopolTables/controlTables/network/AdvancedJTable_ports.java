package com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.network;

import com.net2plan.gui.plugins.GUINetworkDesign;
import com.net2plan.gui.plugins.networkDesign.openStack.OpenStackClient;
import com.net2plan.gui.plugins.networkDesign.openStack.network.OpenStackPort;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.ViewEditTopologyTablesPane;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.AdvancedJTable_networkElement;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.AjtColumnInfo;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.AjtRcMenu;
import com.net2plan.gui.plugins.networkDesign.visualizationControl.VisualizationState;
import com.net2plan.gui.plugins.utils.GeneralForm;
import com.net2plan.interfaces.networkDesign.NetworkLayer;
import com.net2plan.utils.Pair;
import org.apache.commons.collections15.BidiMap;
import org.openstack4j.model.network.State;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;

public class AdvancedJTable_ports extends AdvancedJTable_networkElement<OpenStackPort>
{
    public AdvancedJTable_ports(GUINetworkDesign callback, OpenStackClient openStackClient)
    {
        super(callback, ViewEditTopologyTablesPane.AJTableType.PORTS , true,openStackClient);
    }

    @Override
    public List<AjtColumnInfo<OpenStackPort>> getNonBasicUserDefinedColumnsVisibleOrNot()
    {

        final List<AjtColumnInfo<OpenStackPort>> res = new LinkedList<>();
        res.add(new AjtColumnInfo<OpenStackPort>(this, String.class, null, "ID", "Port ID", null, n -> n.getId(), AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackPort>(this, String.class, null, "Name", "Port Name", null, n -> n.getName(), AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackPort>(this, Set.class, null, "Allowed Address", "Port allowed address", null, n -> n.getPortAllowedAddressPair(),
                AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackPort>(this, String.class, null, "Device ID", "Port device ID",
                null, n -> n.getPortDeviceId(), AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackPort>(this, String.class, null, "Device Owner", "Port device owner",
                null, n -> n.getPortDeviceOwner(), AGTYPE.NOAGGREGATION, null, null));

        res.add(new AjtColumnInfo<OpenStackPort>(this, Set.class, null, "Fixed IPs", "Port fiexd ips", null, n -> n.getPortFixedIps(), AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackPort>(this, String.class, null, "Host ID", "Port host id", null, n -> n.getPortHostId(), AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackPort>(this, String.class, null, "Mac Address", "Port mac address", null, n -> n.getPortMacAddress(),
                AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackPort>(this, String.class, null, "Network ID", "Port network id",
                null, n -> n.getPortNetworkId(), AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackPort>(this, Map.class, null, "Profile", "Port profile",
                null, n -> n.getPortProfile(), AGTYPE.NOAGGREGATION, null, null));

        res.add(new AjtColumnInfo<OpenStackPort>(this, List.class, null, "Segurity Groups", "Port security groups", null, n -> n.getPortSecurityGroups(), AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackPort>(this, State.class, null, "State", "Port state", null, n -> n.getPortState(), AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackPort>(this, String.class, null, "Tenant ID", "Port tenant id", null, n -> n.getPortTenantId(), AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackPort>(this, Boolean.class, null, "Admin state", "Port admin state", null, n -> n.isAdminStateUp(), AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackPort>(this, Boolean.class, null, "Segurity", "Port security enable", null, n -> n.isPortSecurityEnable(), AGTYPE.NOAGGREGATION, null, null));


        return res;
    }

    @Override
    public List<AjtRcMenu> getNonBasicRightClickMenusInfo()
    {final List<AjtRcMenu> res = new ArrayList<>();

        res.add(new AjtRcMenu("Add port", e -> addPort(this.getSelectedElements()), (a, b) -> true, null));

        res.add(new AjtRcMenu("Remove port", e -> getSelectedElements().forEach(n -> {

            removePort(n);

        }), (a, b) -> b == 1, null));

        /*
        res.add(new AjtRcMenu("Change port's name", e -> getSelectedElements().forEach(n -> {

            generalTableUpdate("Name",n,"");

        }), (a, b) -> b ==1, null));
        */
        res.add(new AjtRcMenu("Refresh", e ->updateTab(), (a, b) -> b >=0, null));

        return res;

    }

    public void addPort(ArrayList<OpenStackPort> openStackPorts){

        Map<String,String> headers = new HashMap<>();
        headers.put("Name","");
        headers.put("Subnet ID","Select");
        headers.put("Router ID","Select");
        GeneralForm generalTableForm = new GeneralForm("Add port",headers,this.ajtType,this.openStackClient,this,openStackPorts.get(0));
        updateTab();
    }
    public void removePort(OpenStackPort port){

        openStackClient.updateClient();
        openStackClient.getOpenStackNetDelete().deleteOpenStackPort(port.getId());
        updateTab();
    }

}