package com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.identity;

import com.net2plan.gui.plugins.GUINetworkDesign;
import com.net2plan.gui.plugins.networkDesign.openStack.identity.OpenStackPolicy;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.ViewEditTopologyTablesPane;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.AdvancedJTable_networkElement;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.AjtColumnInfo;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.AjtRcMenu;
import com.net2plan.gui.plugins.networkDesign.visualizationControl.VisualizationState;
import com.net2plan.interfaces.networkDesign.NetworkLayer;
import com.net2plan.utils.Pair;
import org.apache.commons.collections15.BidiMap;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;

public class AdvancedJTable_policies extends AdvancedJTable_networkElement<OpenStackPolicy>
{
    public AdvancedJTable_policies(GUINetworkDesign callback)
    {
        super(callback, ViewEditTopologyTablesPane.AJTableType.GROUPS , true);
    }

    @Override
    public List<AjtColumnInfo<OpenStackPolicy>> getNonBasicUserDefinedColumnsVisibleOrNot()
    {

        final List<AjtColumnInfo<OpenStackPolicy>> res = new LinkedList<>();
        res.add(new AjtColumnInfo<OpenStackPolicy>(this, String.class, null, "ID", "Policy ID", null, n -> n.getId(), AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackPolicy>(this, String.class, null, "User ID", "Policy user id", null, n -> n.getPolicyUserId(), AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackPolicy>(this, String.class, null, "Project ID", "Policy project id", null, n -> n.getPolicyProjectId(),
                AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackPolicy>(this, String.class, null, "Type", "Policy type", null, n -> n.getPolicyType(),
                AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackPolicy>(this, String.class, null, "Blob", "Policy blob", null, n -> n.getPolicyBlob(),
                AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackPolicy>(this, List.class, null, "Links", "Policy links",
                null, n -> n.getPolicyLinks(), AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackPolicy>(this, Object.class, null, " ", "", null, n -> n, AGTYPE.NOAGGREGATION, null, null));



        return res;
    }


    @Override
    public List<AjtRcMenu> getNonBasicRightClickMenusInfo()
    {final List<AjtRcMenu> res = new ArrayList<>();

        res.add(new AjtRcMenu("Add policy", e -> addPolicy(), (a, b) -> true, null));

        res.add(new AjtRcMenu("Remove policy", e -> getSelectedElements().forEach(n -> {

            removePolicy(n);

        }), (a, b) -> b == 1, null));

        res.add(new AjtRcMenu("Change policy's user id", e -> getSelectedElements().forEach(n -> {

            createTableForUpdate("User id",n);

        }), (a, b) -> b ==1, null));

        res.add(new AjtRcMenu("Change policy's project id", e -> getSelectedElements().forEach(n -> {

            createTableForUpdate("Project id",n);

        }), (a, b) -> b ==1, null));

        res.add(new AjtRcMenu("Change policy's type ", e -> getSelectedElements().forEach(n -> {

            createTableForUpdate("Type",n);

        }), (a, b) -> b ==1, null));

        res.add(new AjtRcMenu("Change policy's blob ", e -> getSelectedElements().forEach(n -> {

            createTableForUpdate("Blob",n);

        }), (a, b) -> b ==1, null));



        return res;

    }

    public void createTableForUpdate(String key, OpenStackPolicy policy) {
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
                    case "User id":
                        policy.setPolicyUserId(os_text_change.getText());
                        break;
                    case "Project id":
                        policy.setPolicyProjectId(os_text_change.getText());
                        break;
                    case "Type":
                        policy.setPolicyType(os_text_change.getText());
                        break;
                    case "Blob":
                        policy.setPolicyBlob(os_text_change.getText());
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
    public void addPolicy(){

        callback.getOpenStackNet().getOsnc().createOpenStackPolicy("userid","projectid","type","blob");
    }
    public void removePolicy(OpenStackPolicy policy){

        callback.getOpenStackNet().getOsnd().deleteOpenStackPolicy(policy.getId());
    }


}
