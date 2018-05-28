package com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.identity;

import com.net2plan.gui.plugins.GUINetworkDesign;
import com.net2plan.gui.plugins.networkDesign.openStack.identity.OpenStackGroup;
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

public class AdvancedJTable_groups extends AdvancedJTable_networkElement<OpenStackGroup>
{
    public AdvancedJTable_groups(GUINetworkDesign callback)
    {
        super(callback, ViewEditTopologyTablesPane.AJTableType.GROUPS , true);
    }

    @Override
    public List<AjtColumnInfo<OpenStackGroup>> getNonBasicUserDefinedColumnsVisibleOrNot()
    {

        final List<AjtColumnInfo<OpenStackGroup>> res = new LinkedList<>();
        res.add(new AjtColumnInfo<OpenStackGroup>(this, String.class, null, "ID", "Group ID", null, n -> n.getId(), AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackGroup>(this, String.class, null, "Name", "Group name", null, n -> n.getGroupName(), AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackGroup>(this, String.class, null, "Description", "Group description", null, n -> n.getGroupDescription(),
                AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackGroup>(this, String.class, null, "Domain ID", "Group domain id", null, n -> n.getGroupDomainId(),
                AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackGroup>(this, List.class, null, "Links", "Group links",
                null, n -> n.getGroupLinks(), AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackGroup>(this, Object.class, null, " ", "", null, n -> n, AGTYPE.NOAGGREGATION, null, null));



        return res;
    }


    @Override
    public List<AjtRcMenu> getNonBasicRightClickMenusInfo()
    {final List<AjtRcMenu> res = new ArrayList<>();


        res.add(new AjtRcMenu("Add group", e -> addGroup(), (a, b) -> true, null));

        res.add(new AjtRcMenu("Remove group", e -> getSelectedElements().forEach(n -> {

            removeGroup(n);

        }), (a, b) -> b == 1, null));
        res.add(new AjtRcMenu("Change group's name", e -> getSelectedElements().forEach(n -> {

            createTableForUpdate("Name",n);

        }), (a, b) -> b ==1, null));

        res.add(new AjtRcMenu("Change group's domain id", e -> getSelectedElements().forEach(n -> {

            createTableForUpdate("Domain id",n);

        }), (a, b) -> b ==1, null));

        res.add(new AjtRcMenu("Change group's description", e -> getSelectedElements().forEach(n -> {

            createTableForUpdate("Description",n);

        }), (a, b) -> b ==1, null));






        return res;

    }

    public void createTableForUpdate(String key, OpenStackGroup group) {
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
                        group.setGroupName(os_text_change.getText());
                        break;
                    case "Domain id":
                        group.setGroupDomainId(os_text_change.getText());
                        break;
                    case "Description":
                        group.setGroupDescription(os_text_change.getText());
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
    public void addGroup(){

        callback.getOpenStackNet().getOsnc().createOpenStackGroup("name","description","domainid");
    }
    public void removeGroup(OpenStackGroup group){

        callback.getOpenStackNet().getOsnd().deleteOpenStackGroup(group.getId());
    }

}
