package com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.network;

import com.net2plan.gui.plugins.GUINetworkDesign;
import com.net2plan.gui.plugins.networkDesign.openStack.network.OpenStackRouter;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.ViewEditTopologyTablesPane;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.AdvancedJTable_networkElement;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.AjtColumnInfo;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.AjtRcMenu;
import com.net2plan.gui.plugins.networkDesign.visualizationControl.VisualizationState;
import com.net2plan.interfaces.networkDesign.NetworkLayer;
import com.net2plan.utils.Pair;
import org.apache.commons.collections15.BidiMap;
import org.openstack4j.model.network.ExternalGateway;
import org.openstack4j.model.network.State;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;

public class AdvancedJTable_routers extends AdvancedJTable_networkElement<OpenStackRouter>
{
    public AdvancedJTable_routers(GUINetworkDesign callback)
    {
        super(callback, ViewEditTopologyTablesPane.AJTableType.ROUTERS , true);
    }

    @Override
    public List<AjtColumnInfo<OpenStackRouter>> getNonBasicUserDefinedColumnsVisibleOrNot()
    {

        final List<AjtColumnInfo<OpenStackRouter>> res = new LinkedList<>();
        res.add(new AjtColumnInfo<OpenStackRouter>(this, String.class, null, "ID", "Router ID", null, n -> n.getId(), AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackRouter>(this, String.class, null, "Name", "Router name", null, n -> n.getRouterName(), AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackRouter>(this, String.class, null, "Tenant ID", "Router tenant id", null, n -> n.getRouterTenantId(), AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackRouter>(this, State.class, null, "State", "Router state", null, n -> n.getRouterState(), AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackRouter>(this, Boolean.class, null, "AdminStateUp", "Router admin state", null, n -> n.isRouterIsAdminStateUp(), AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackRouter>(this, Boolean.class, null, "Distributed", "Router distributed", null, n -> n.isRouterIsDistributed(), AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackRouter>(this, List.class, null, "Routes", "Router routes", null, n -> n.getRouterRoutes(), AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackRouter>(this, ExternalGateway.class, null, "Gateway info", "Router external gateway info", null, n -> n.getRouterExternalGatewayInfo(), AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackRouter>(this, Object.class, null, " ", "", null, n -> n, AGTYPE.NOAGGREGATION, null, null));


        return res;
    }


    @Override
    public List<AjtRcMenu> getNonBasicRightClickMenusInfo()
    {final List<AjtRcMenu> res = new ArrayList<>();

        res.add(new AjtRcMenu("Add router", e -> addRouter(), (a, b) -> true, null));

        res.add(new AjtRcMenu("Remove router", e -> getSelectedElements().forEach(n -> {

            removeRouter(n);

        }), (a, b) -> b == 1, null));

        res.add(new AjtRcMenu("Change router's name", e -> getSelectedElements().forEach(n -> {

            createTableForUpdate("Name",n);

        }), (a, b) -> b ==1, null));

        res.add(new AjtRcMenu("Change router's tenant id", e -> getSelectedElements().forEach(n -> {

            createTableForUpdate("Tenant id",n);

        }), (a, b) -> b ==1, null));


        return res;

    }

    public void createTableForUpdate(String key, OpenStackRouter router) {
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
                        router.setName(os_text_change.getText());
                        break;
                    case "Tenant id":
                        router.setRouterTenantId(os_text_change.getText());
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
    public void addRouter(){
        callback.getOpenStackNet().getOsnc().createOpenStackRouter("Name","tenantid","gateway");
    }
    public void removeRouter(OpenStackRouter router){

        callback.getOpenStackNet().getOsnd().deleteOpenStackRouter(router.getId());
    }

}
