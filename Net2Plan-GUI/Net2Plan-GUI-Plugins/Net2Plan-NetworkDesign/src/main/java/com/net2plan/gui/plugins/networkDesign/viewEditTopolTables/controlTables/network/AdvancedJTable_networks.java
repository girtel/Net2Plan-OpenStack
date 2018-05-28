package com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.network;

import com.net2plan.gui.plugins.GUINetworkDesign;
import com.net2plan.gui.plugins.networkDesign.openStack.network.OpenStackNetwork;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.ViewEditTopologyTablesPane;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.AdvancedJTable_networkElement;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.AjtColumnInfo;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.AjtRcMenu;
import com.net2plan.gui.plugins.networkDesign.visualizationControl.VisualizationState;
import com.net2plan.interfaces.networkDesign.NetworkLayer;
import com.net2plan.utils.Pair;
import org.apache.commons.collections15.BidiMap;
import org.openstack4j.model.network.NetworkType;
import org.openstack4j.model.network.State;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;

public class AdvancedJTable_networks extends AdvancedJTable_networkElement<OpenStackNetwork>
{
    public AdvancedJTable_networks(GUINetworkDesign callback)
    {
        super(callback, ViewEditTopologyTablesPane.AJTableType.NETWORKS , true);
    }

    @Override
    public List<AjtColumnInfo<OpenStackNetwork>> getNonBasicUserDefinedColumnsVisibleOrNot()
    {

        final List<AjtColumnInfo<OpenStackNetwork>> res = new LinkedList<>();
        res.add(new AjtColumnInfo<OpenStackNetwork>(this, String.class, null, "ID", "Network ID", null, n -> n.getId(), AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackNetwork>(this, String.class, null, "Name", "Network Name", null, n -> n.getName(), AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackNetwork>(this, String.class, null, "Provider", "Network Provider PhyNet", null, n -> n.getNetworkProviderPhyNet(),
                AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackNetwork>(this, String.class, null, "Provider ID", "Network Provider SegID",
                null, n -> n.getNetworkProviderSegID(), AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackNetwork>(this, String.class, null, "Tenant ID", "NetworkTenantId",
                null, n -> n.getNetworkTenantId(), AGTYPE.NOAGGREGATION, null, null));

        res.add(new AjtColumnInfo<OpenStackNetwork>(this, State.class, null, "State", "Network state", null, n -> n.getNetworkState(), AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackNetwork>(this, NetworkType.class, null, "Type", "Network type", null, n -> n.getNetworkType(), AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackNetwork>(this, List.class, null, "Neutron", "Network neutron subnets", null, n -> n.getNetworkNeutronSubnets(),
                AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackNetwork>(this, List.class, null, "Subnets", "Network subnets",
                null, n -> n.getNetworkSubnets(), AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackNetwork>(this, Boolean.class, null, "Admin State", "Network admin state",
                null, n -> n.isNetworkIsAdminStateUp(), AGTYPE.NOAGGREGATION, null, null));

        res.add(new AjtColumnInfo<OpenStackNetwork>(this, Boolean.class, null, "Router external", "Network router external", null, n -> n.isNetworkIsRouterExternal(), AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackNetwork>(this, Boolean.class, null, "Shared", "Network shared", null, n -> n.isNetworkIsShared(), AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackNetwork>(this, Object.class, null, " ", "", null, n -> n, AGTYPE.NOAGGREGATION, null, null));


        return res;
    }


    @Override
    public List<AjtRcMenu> getNonBasicRightClickMenusInfo()
    {final List<AjtRcMenu> res = new ArrayList<>();



        res.add(new AjtRcMenu("Change the network's name", e -> getSelectedElements().forEach(n -> {

            createTableForUpdate("Name",n);

        }), (a, b) -> b == 1, null));

        res.add(new AjtRcMenu("Add network", e -> addNetwork(), (a, b) -> true, null));

        res.add(new AjtRcMenu("Remove network", e -> getSelectedElements().forEach(n -> {

            removeNetwork(n);

        }), (a, b) -> b == 1, null));
        return res;

    }

    public void createTableForUpdate(String key, OpenStackNetwork network) {
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
                        network.setName(os_text_change.getText());
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

    public void addNetwork(){
        callback.getOpenStackNet().getOsnc().createOpenStackNetwork("Name","segment","tenant");
    }
    public void removeNetwork(OpenStackNetwork network){

        callback.getOpenStackNet().getOsnd().deleteOpenStackNetwork(network.getId());
    }
}
