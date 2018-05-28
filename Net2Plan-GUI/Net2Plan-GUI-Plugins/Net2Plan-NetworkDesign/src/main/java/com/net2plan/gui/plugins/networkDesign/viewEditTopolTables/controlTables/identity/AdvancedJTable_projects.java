package com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.identity;

import com.net2plan.gui.plugins.GUINetworkDesign;
import com.net2plan.gui.plugins.networkDesign.openStack.identity.OpenStackProject;
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

public class AdvancedJTable_projects extends AdvancedJTable_networkElement<OpenStackProject>
{
    public AdvancedJTable_projects(GUINetworkDesign callback)
    {
        super(callback, ViewEditTopologyTablesPane.AJTableType.PROJECTS , true);
    }

    @Override
    public List<AjtColumnInfo<OpenStackProject>> getNonBasicUserDefinedColumnsVisibleOrNot()
    {

        final List<AjtColumnInfo<OpenStackProject>> res = new LinkedList<>();
        res.add(new AjtColumnInfo<OpenStackProject>(this, String.class, null, "ID", "Project ID", null, n -> n.getId(), AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackProject>(this, String.class, null, "Name", "Project Name", null, n -> n.getProjectName(), AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackProject>(this, String.class, null, "Description", "Project description", null, n -> n.getProjectDescription(),
                AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackProject>(this, String.class, null, "Domain", "Project domain", null, n -> n.getProjectDomain(),
                AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackProject>(this, String.class, null, "Domain ID", "Project domain id", null, n -> n.getProjectDomainId(),
                AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackProject>(this, String.class, null, "Parents", "Project parents", null, n -> n.getProjectParents(),
                AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackProject>(this, String.class, null, "Parents ID", "Project parents id", null, n -> n.getProjectParentId(),
                AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackProject>(this, String.class, null, "Subtree", "Project Subtree", null, n -> n.getProjectSubtree(),
                AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackProject>(this, List.class, null, "Links", "Project links",
                null, n -> n.getProjectLinks(), AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackProject>(this, Object.class, null, " ", "", null, n -> n, AGTYPE.NOAGGREGATION, null, null));



        return res;
    }


    @Override
    public List<AjtRcMenu> getNonBasicRightClickMenusInfo()
    {final List<AjtRcMenu> res = new ArrayList<>();

        res.add(new AjtRcMenu("Add project", e -> addProject(), (a, b) -> true, null));

        res.add(new AjtRcMenu("Remove project", e -> getSelectedElements().forEach(n -> {

            removeProject(n);

        }), (a, b) -> b == 1, null));

        res.add(new AjtRcMenu("Change project's name", e -> getSelectedElements().forEach(n -> {

            createTableForUpdate("Name",n);

        }), (a, b) -> b ==1, null));

        res.add(new AjtRcMenu("Change project's parent id", e -> getSelectedElements().forEach(n -> {

            createTableForUpdate("Parent id",n);

        }), (a, b) -> b ==1, null));

        res.add(new AjtRcMenu("Change project's domain id", e -> getSelectedElements().forEach(n -> {

            createTableForUpdate("Domain id",n);

        }), (a, b) -> b ==1, null));

        res.add(new AjtRcMenu("Change project's description", e -> getSelectedElements().forEach(n -> {

            createTableForUpdate("Description",n);

        }), (a, b) -> b ==1, null));

        res.add(new AjtRcMenu("Change project's parents", e -> getSelectedElements().forEach(n -> {

            createTableForUpdate("Parents",n);

        }), (a, b) -> b ==1, null));

        res.add(new AjtRcMenu("Change project's subtree", e -> getSelectedElements().forEach(n -> {

            createTableForUpdate("Subtree",n);

        }), (a, b) -> b ==1, null));




        return res;

    }

    public void createTableForUpdate(String key, OpenStackProject project) {
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
                        project.setProjectName(os_text_change.getText());
                        break;
                    case "Parent id":
                        project.setProjectParentId(os_text_change.getText());
                        break;
                    case "Domain id":
                        project.setProjectDomainId(os_text_change.getText());
                        break;
                    case "Description":
                        project.setProjectDescription(os_text_change.getText());
                        break;
                    case "Parents":
                        project.setProjectParents(os_text_change.getText());
                        break;
                    case "Subtree":
                        project.setProjectSubtree(os_text_change.getText());
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
    public void addProject(){

        callback.getOpenStackNet().getOsnc().createOpenStackProject("name","description","domainid","parentid","parents","subtree");
    }
    public void removeProject(OpenStackProject project){

        callback.getOpenStackNet().getOsnd().deleteOpenStackProject(project.getId());
    }

}

