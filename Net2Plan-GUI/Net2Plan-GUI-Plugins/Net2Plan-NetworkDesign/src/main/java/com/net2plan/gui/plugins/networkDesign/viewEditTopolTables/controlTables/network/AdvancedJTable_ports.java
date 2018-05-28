package com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.network;

import com.net2plan.gui.plugins.GUINetworkDesign;
import com.net2plan.gui.plugins.networkDesign.openStack.network.OpenStackPort;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.ViewEditTopologyTablesPane;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.AdvancedJTable_networkElement;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.AjtColumnInfo;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.AjtRcMenu;
import com.net2plan.gui.plugins.networkDesign.visualizationControl.VisualizationState;
import com.net2plan.interfaces.networkDesign.NetworkLayer;
import com.net2plan.utils.Pair;
import com.sun.org.apache.xpath.internal.operations.Bool;
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
    public AdvancedJTable_ports(GUINetworkDesign callback)
    {
        super(callback, ViewEditTopologyTablesPane.AJTableType.PORTS , true);
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
        res.add(new AjtColumnInfo<OpenStackPort>(this, Object.class, null, " ", "", null, n -> n, AGTYPE.NOAGGREGATION, null, null));



        return res;
    }


    @Override
    public List<AjtRcMenu> getNonBasicRightClickMenusInfo()
    {final List<AjtRcMenu> res = new ArrayList<>();

        res.add(new AjtRcMenu("Add port", e -> addPort(), (a, b) -> true, null));

        res.add(new AjtRcMenu("Remove port", e -> getSelectedElements().forEach(n -> {

            removePort(n);

        }), (a, b) -> b == 1, null));

        res.add(new AjtRcMenu("Change port's name", e -> getSelectedElements().forEach(n -> {

            createTableForUpdate("Name",n);

        }), (a, b) -> b ==1, null));

        res.add(new AjtRcMenu("Change port's tenant id", e -> getSelectedElements().forEach(n -> {

            createTableForUpdate("Tenant id",n);

        }), (a, b) -> b ==1, null));

        res.add(new AjtRcMenu("Change port's device id", e -> getSelectedElements().forEach(n -> {

            createTableForUpdate("Device id",n);

        }), (a, b) -> b ==1, null));

        res.add(new AjtRcMenu("Change port's device owner", e -> getSelectedElements().forEach(n -> {

            createTableForUpdate("Device owner",n);

        }), (a, b) -> b ==1, null));

        res.add(new AjtRcMenu("Change port's host id", e -> getSelectedElements().forEach(n -> {

            createTableForUpdate("Host id",n);

        }), (a, b) -> b ==1, null));

        res.add(new AjtRcMenu("Change port's mac address", e -> getSelectedElements().forEach(n -> {

            createTableForUpdate("MAC",n);

        }), (a, b) -> b ==1, null));

        res.add(new AjtRcMenu("Change port's network id", e -> getSelectedElements().forEach(n -> {

            createTableForUpdate("Network id",n);

        }), (a, b) -> b ==1, null));

        res.add(new AjtRcMenu("Change port's state", e -> getSelectedElements().forEach(n -> {

            createTableForUpdate("State",n);

        }), (a, b) -> b ==1, null));

        res.add(new AjtRcMenu("Change port's vif type", e -> getSelectedElements().forEach(n -> {

            createTableForUpdate("VIF",n);

        }), (a, b) -> b ==1, null));

        res.add(new AjtRcMenu("Change port's nic type", e -> getSelectedElements().forEach(n -> {

            createTableForUpdate("NIC",n);

        }), (a, b) -> b ==1, null));



        return res;

    }

    public void createTableForUpdate(String key, OpenStackPort port) {
        JFrame jfM = new JFrame(key);
        jfM.setLayout(null);

        JButton jbP1 = new JButton("Enter");

        JPanel jp1 = new JPanel(new GridLayout(6, 2, 30, 10));//filas, columnas, espacio entre filas, espacio entre columnas
        JLabel l6 = new JLabel(key, SwingConstants.LEFT);
        jp1.add(l6);
        JTextField os_text_change = new JTextField();
        jp1.add(os_text_change);


        jp1.setVisible(true);
        jp1.setBounds(10, 10, 200, 200);
        jbP1.setBounds(75, 90, 90, 25);

        jbP1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                switch(key){
                    case "Name":
                        port.setName(os_text_change.getText());
                        break;
                    case "Tenant id":
                        port.setPortTenantId(os_text_change.getText());
                        break;
                    case "Device id":
                        port.setPortDeviceId(os_text_change.getText());
                        break;
                    case "Device owner":
                        port.setPortDeviceOwner(os_text_change.getText());
                        break;
                    case "Host id":
                        port.setPortHostId(os_text_change.getText());
                        break;
                    case "MAC":
                        port.setPortMacAddress(os_text_change.getText());
                        break;
                    case "Network id":
                        port.setPortNetworkId(os_text_change.getText());
                        break;
                    case "State":
                        //port.set(os_text_change.getText());
                        break;
                    case "VIF":
                        port.setPortVifType(os_text_change.getText());
                        break;
                    case "NIC":
                        port.setPortvNicType(os_text_change.getText());
                        break;


                }
                callback.getOpenStackNet().updateAllTables();
                final VisualizationState vs = callback.getVisualizationState();
                Pair<BidiMap<NetworkLayer, Integer>, Map<NetworkLayer, Boolean>> res =
                        vs.suggestCanvasUpdatedVisualizationLayerInfoForNewDesign(new HashSet<>(callback.getDesign().getNetworkLayers()));
                vs.setCanvasLayerVisibilityAndOrder(callback.getDesign(), res.getFirst(), res.getSecond());
                callback.updateVisualizationAfterNewTopology();
                callback.addNetPlanChange();
                jfM.dispose();
            }});

        jfM.add(jbP1);
        jfM.add(jp1);

        ImageIcon img = new ImageIcon(getClass().getResource("/resources/common/openstack_logo.png"));
        jfM.setIconImage(img.getImage());
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();

        jfM.setSize(250, 160);
        jfM.setLocation(dim.width/2-jfM.getSize().width/2, dim.height/2-jfM.getSize().height/2);

        jfM.setResizable(false);
        jfM.setVisible(true);
        jfM.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

    }
    public void addPort(){

        callback.getOpenStackNet().getOsnc().createOpenStackPort("name","networkid","deviceid","hostid","macaddress","secgroup");
    }
    public void removePort(OpenStackPort port){

        callback.getOpenStackNet().getOsnd().deleteOpenStackPort(port.getId());
    }
}
