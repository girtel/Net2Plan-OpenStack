package com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.identity;

import com.net2plan.gui.plugins.GUINetworkDesign;
import com.net2plan.gui.plugins.networkDesign.openStack.identity.OpenStackUser;
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

public class AdvancedJTable_users extends AdvancedJTable_networkElement<OpenStackUser>
{
    public AdvancedJTable_users(GUINetworkDesign callback)
    {
        super(callback, ViewEditTopologyTablesPane.AJTableType.USERS , true);
    }

    @Override
    public List<AjtColumnInfo<OpenStackUser>> getNonBasicUserDefinedColumnsVisibleOrNot()
    {

        final List<AjtColumnInfo<OpenStackUser>> res = new LinkedList<>();
        res.add(new AjtColumnInfo<OpenStackUser>(this, String.class, null, "ID", "User ID", null, n -> n.getId(), AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackUser>(this, String.class, null, "Name", "User Name", null, n -> n.getName(), AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackUser>(this, String.class, null, "Domain ID", "Domain ID", null, n -> n.getDomainId(),
                AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackUser>(this, String.class, null, "Email", "User email",
                null, n -> n.getEmail(), AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackUser>(this, String.class, null, "Description", "User description",
                null, n -> n.getDescription(), AGTYPE.NOAGGREGATION, null, null));

        return res;
    }


    @Override
    public List<AjtRcMenu> getNonBasicRightClickMenusInfo()
    {final List<AjtRcMenu> res = new ArrayList<>();

        res.add(new AjtRcMenu("Add new user", e -> getSelectedElements().forEach(n -> {

            addNewUser(n);

        }), (a, b) -> true, null));

        res.add(new AjtRcMenu("Remove user", e -> getSelectedElements().forEach(n -> {

            removeUser(n);


        }), (a, b) -> b==b, null));

        res.add(new AjtRcMenu("Change the user's name", e -> getSelectedElements().forEach(n -> {

            createTableForUpdate("Name",n);

        }), (a, b) -> b ==1, null));
        res.add(new AjtRcMenu("Change the user's email", e -> getSelectedElements().forEach(n -> {

            createTableForUpdate("Email",n);

        }), (a, b) -> b ==1, null));
        res.add(new AjtRcMenu("Change the user's description", e -> getSelectedElements().forEach(n -> {

            createTableForUpdate("Description",n);

        }), (a, b) -> b ==1, null));

        return res;

    }

    public void createTableForUpdate(String key, OpenStackUser user){
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
                        user.updateUserName(os_text_change.getText());
                        break;
                    case "Email":
                        user.updateUserEmail(os_text_change.getText());
                        break;
                    case "Description":
                        user.updateUserDescription(os_text_change.getText());
                        break;

                }
                callback.getOpenStackNet().updateUserTable();
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

    public void addNewUser(OpenStackUser user){
        JFrame jfM = new JFrame("Add user");
        jfM.setLayout(null);

        JButton jbP1 = new JButton("Enter");

        JPanel jp1 = new JPanel(new GridLayout(6, 2, 30, 10));//filas, columnas, espacio entre filas, espacio entre columnas
        JLabel l6 = new JLabel("Properties", SwingConstants.LEFT);
        jp1.add(l6);
        JLabel label = new JLabel("", SwingConstants.LEFT);
        jp1.add(label);
        JLabel labelName = new JLabel("Name", SwingConstants.LEFT);
        jp1.add(labelName);
        JTextField os_name_change = new JTextField();
        jp1.add(os_name_change);
        JLabel labelDescription = new JLabel("Description", SwingConstants.LEFT);
        jp1.add(labelDescription);
        JTextField os_description_change = new JTextField();
        jp1.add(os_description_change);
        JLabel labelPassword = new JLabel("Password", SwingConstants.LEFT);
        jp1.add(labelPassword);
        JTextField os_password_change = new JTextField();
        jp1.add(os_password_change);
        JLabel labelEmail = new JLabel("Email", SwingConstants.LEFT);
        jp1.add(labelEmail);
        JTextField os_email_change = new JTextField();
        jp1.add(os_email_change);

        jp1.setVisible(true);
        jp1.setBounds(10, 10, 200, 200);
        jbP1.setBounds(75, 200, 90, 25);

        jbP1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                callback.getOpenStackNet().createNewUser(os_name_change.getText(),os_description_change.getText(),os_password_change.getText(),os_email_change.getText());

                callback.getOpenStackNet().updateUserTable();
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

        jfM.setSize(250, 275);
        jfM.setLocation(dim.width/2-jfM.getSize().width/2, dim.height/2-jfM.getSize().height/2);

        jfM.setResizable(false);
        jfM.setVisible(true);
        jfM.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    public void removeUser(OpenStackUser user){

        user.deleteUser();
        callback.getOpenStackNet().updateUserTable();
        final VisualizationState vs = callback.getVisualizationState();
        Pair<BidiMap<NetworkLayer, Integer>, Map<NetworkLayer, Boolean>> res =
                vs.suggestCanvasUpdatedVisualizationLayerInfoForNewDesign(new HashSet<>(callback.getDesign().getNetworkLayers()));
        vs.setCanvasLayerVisibilityAndOrder(callback.getDesign(), res.getFirst(), res.getSecond());
        callback.updateVisualizationAfterNewTopology();
        callback.addNetPlanChange();
    }



}
