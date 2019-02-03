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
import java.util.stream.Collectors;

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
        //res.add(new AjtColumnInfo<OpenStackPort>(this, String.class, null, "ID", "Port ID", null, n -> n.getId(), AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackPort>(this, String.class, null, "Name", "Port Name", null, n -> n.getName(), AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackPort>(this, Set.class, null, "Allowed Address", "Port allowed address", null, n -> n.getPortAllowedAddressPair(),
                AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackPort>(this, String.class, null, "Device", "Port device ",
                null, n -> callback.getOpenStackNet().getOpenStackNetworkElementByOpenStackId(n.getPortDeviceId()), AGTYPE.NOAGGREGATION, null, null));
     /*   res.add(new AjtColumnInfo<OpenStackPort>(this, String.class, null, "Device Owner", "Port device owner",
                null, n -> n.getPortDeviceOwner(), AGTYPE.NOAGGREGATION, null, null));
*/
        res.add(new AjtColumnInfo<OpenStackPort>(this, Set.class, null, "Fixed IPs", "Port fiexd ips", null, n -> n.getPortFixedIps(), AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackPort>(this, String.class, null, "Host ", "Port host", null, n -> callback.getOpenStackNet().getOpenStackNetworkElementByOpenStackId(n.getPortHostId()), AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackPort>(this, String.class, null, "Mac Address", "Port mac address", null, n -> n.getPortMacAddress(),
                AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackPort>(this, String.class, null, "Network ", "Port network ",
                null, n ->callback.getOpenStackNet().getOpenStackNetworkElementByOpenStackId(n.getPortNetworkId()), AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackPort>(this, Map.class, null, "Profile", "Port profile",
                null, n -> n.getPortProfile(), AGTYPE.NOAGGREGATION, null, null));

        res.add(new AjtColumnInfo<OpenStackPort>(this, List.class, null, "Segurity Groups", "Port security groups", null, n -> n.getPortSecurityGroups().stream().map(x->callback.getOpenStackNet().getOpenStackNetworkElementByOpenStackId(x)).collect(Collectors.toList()), AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackPort>(this, State.class, null, "State", "Port state", null, n -> n.getPortState(), AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackPort>(this, String.class, null, "Project", "Port project", null, n -> callback.getOpenStackNet().getOpenStackNetworkElementByOpenStackId(n.getPortTenantId()), AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackPort>(this, Boolean.class, null, "Admin state", "Port admin state", null, n -> n.isAdminStateUp(), AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackPort>(this, Boolean.class, null, "Segurity", "Port security enable", null, n -> n.isPortSecurityEnable(), AGTYPE.NOAGGREGATION, null, null));


        return res;
    }

    @Override
    public List<AjtRcMenu> getNonBasicRightClickMenusInfo()
    {
        final List<AjtRcMenu> res = new ArrayList<>();

        res.add(new AjtRcMenu("Add port", e -> addPort(), (a, b) -> b==b, null));

        res.add(new AjtRcMenu("Remove port", e -> removePort(getSelectedElements()), (a, b) -> b >= 1, null));

        /*
        res.add(new AjtRcMenu("Change port's name", e -> getSelectedElements().forEach(n -> {

            generalTableUpdate("Name",n,"");

        }), (a, b) -> b ==1, null));
        */

        return res;

    }

    public void addPort(){

        Map<String,String> headers = new HashMap<>();
        headers.put("Name","");
        headers.put("Subnet ID","Select");
        headers.put("Router ID","Select");
        GeneralForm generalTableForm = new GeneralForm("Add port",headers,this.ajtType,this.openStackClient,this,null);
        //updateTab();
    }
    public void removePort(List<OpenStackPort> ports){

        ports.forEach(port -> openStackClient.getOpenStackNetDelete().deleteOpenStackNetworkElement(port));
        updateThisTab();
    }

}