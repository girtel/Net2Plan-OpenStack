package com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.network;

import com.net2plan.gui.plugins.GUINetworkDesign;
import com.net2plan.gui.plugins.networkDesign.openStack.OpenStackNet;
import com.net2plan.gui.plugins.networkDesign.openStack.network.OpenStackSubnet;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.ViewEditTopologyTablesPane;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.AdvancedJTable_networkElement;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.AjtColumnInfo;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.AjtRcMenu;
import com.net2plan.gui.plugins.networkDesign.visualizationControl.VisualizationState;
import com.net2plan.interfaces.networkDesign.NetworkLayer;
import com.net2plan.utils.Pair;
import org.apache.commons.collections15.BidiMap;
import org.openstack4j.model.network.IPVersionType;
import org.openstack4j.model.network.Ipv6AddressMode;
import org.openstack4j.model.network.Ipv6RaMode;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;

public class AdvancedJTable_subnets extends AdvancedJTable_networkElement<OpenStackSubnet>
{
    public AdvancedJTable_subnets(GUINetworkDesign callback)
    {
        super(callback, ViewEditTopologyTablesPane.AJTableType.SUBNETS , true);
    }

    @Override
    public List<AjtColumnInfo<OpenStackSubnet>> getNonBasicUserDefinedColumnsVisibleOrNot()
    {

        OpenStackNet osn = callback.getOpenStackNet();
        final List<AjtColumnInfo<OpenStackSubnet>> res = new LinkedList<>();
        res.add(new AjtColumnInfo<OpenStackSubnet>(this, String.class, null, "ID", "Subnet ID", null, n -> n.getId(), AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackSubnet>(this, String.class, null, "Name", "Subnet Name", null, n -> n.getName(), AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackSubnet>(this, String.class, null, "Cidr", "Subnet Cidr", null, n -> n.getSubnetCidr(),
                AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackSubnet>(this, String.class, null, "Gateway", "Subnet Gateway",
                null, n -> n.getSubnetGateway(), AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackSubnet>(this, String.class, null, "Network ID", "Subnet network ID",
                null, n -> n.getSubnetNetworkId(), AGTYPE.NOAGGREGATION, null, null));

        res.add(new AjtColumnInfo<OpenStackSubnet>(this, List.class, null, "Allocation pool", "Subnet allocation pool", null, n -> n.getSubnetAllocationPools(), AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackSubnet>(this, List.class, null, "DNS", "Subnet dns list", null, n -> n.getSubnetDnsNames(), AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackSubnet>(this, List.class, null, "Host routes", "Subnet host routes", null, n -> n.getSubnetHostRoutes(), AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackSubnet>(this, IPVersionType.class, null, "Ip version type", "Subnet ip version type", null, n -> n.getSubnetIpVersion(), AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackSubnet>(this, String.class, null, "Tenant ID", "Subnet tenant ID", null, n -> n.getSubnetTenantId(), AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackSubnet>(this, Boolean.class, null, "DHCP", "Subnet dhcp", null, n -> n.isSubnetIsDHCPEnabled(), AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackSubnet>(this, Ipv6AddressMode.class, null, "IPv6 address mode", "Subnet ipv6 address mode", null, n -> n.getSubnetIpv6AddressMode(), AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackSubnet>(this, Ipv6RaMode.class, null, "IPv6 Ra mode", "Subnet ipv6 ra mode", null, n -> n.getSubnetIpv6RaMode(), AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackSubnet>(this, Object.class, null, " ", "", null, n -> n, AGTYPE.NOAGGREGATION, null, null));


        return res;
    }


    @Override
    public List<AjtRcMenu> getNonBasicRightClickMenusInfo()
    {
        final List<AjtRcMenu> res = new ArrayList<>();

        res.add(new AjtRcMenu("Add subnet", e -> addSubnet(), (a, b) -> true, null));

        res.add(new AjtRcMenu("Remove subnet", e -> getSelectedElements().forEach(n -> {

            removeSubnet(n);

        }), (a, b) -> b == 1, null));

        res.add(new AjtRcMenu("Change subnet's name", e -> getSelectedElements().forEach(n -> {

            createTableForUpdate("Name",n);

        }), (a, b) -> b ==1, null));

        res.add(new AjtRcMenu("Change subnet's tenant id", e -> getSelectedElements().forEach(n -> {

            createTableForUpdate("Tenant id",n);

        }), (a, b) -> b ==1, null));

        res.add(new AjtRcMenu("Change subnet's gateway", e -> getSelectedElements().forEach(n -> {

            createTableForUpdate("Gateway",n);

        }), (a, b) -> b ==1, null));


        res.add(new AjtRcMenu("Change subnet's network id", e -> getSelectedElements().forEach(n -> {

            createTableForUpdate("Network id",n);

        }), (a, b) -> b ==1, null));

        return res;

    }

    public void createTableForUpdate(String key, OpenStackSubnet subnet) {
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
                        subnet.setName(os_text_change.getText());
                        break;
                    case "Tenant id":
                        subnet.setSubnetTenantId(os_text_change.getText());
                        break;
                    case "Gateway":
                        subnet.setSubnetGateway(os_text_change.getText());
                        break;
                    case "Network id":
                        subnet.setSubnetNetworkId(os_text_change.getText());
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
    public void addSubnet(){
        callback.getOpenStackNet().getOsnc().createOpenStackSubnet("Name","networkid","gateway","tenantid");
    }
    public void removeSubnet(OpenStackSubnet subnet){

        callback.getOpenStackNet().getOsnd().deleteOpenStackSubnet(subnet.getId());
    }
}
