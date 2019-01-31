package com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.telemetry;

import com.net2plan.gui.plugins.GUINetworkDesign;
import com.net2plan.gui.plugins.networkDesign.openStack.OpenStackClient;
import com.net2plan.gui.plugins.networkDesign.openStack.telemetry.OpenStackMeter;
import com.net2plan.gui.plugins.networkDesign.openStack.telemetry.OpenStackResource;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.ViewEditTopologyTablesPane;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.AdvancedJTable_networkElement;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.AjtColumnInfo;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.AjtRcMenu;
import com.net2plan.gui.plugins.utils.AnalisisFrame;
import com.net2plan.gui.plugins.utils.Graficos;
import javafx.scene.control.Spinner;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;

public class AdvancedJTable_resources extends AdvancedJTable_networkElement<OpenStackResource>
{
    public AdvancedJTable_resources(GUINetworkDesign callback, OpenStackClient openStackClient)
    {
        super(callback, ViewEditTopologyTablesPane.AJTableType.RESOURCES , true,openStackClient);
    }

    @Override
    public List<AjtColumnInfo<OpenStackResource>> getNonBasicUserDefinedColumnsVisibleOrNot()
    {

        final List<AjtColumnInfo<OpenStackResource>> res = new LinkedList<>();
        res.add(new AjtColumnInfo<OpenStackResource>(this, Object.class, null, "ID", "Resource ID", null, n -> n.getId(), AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackResource>(this, String.class, null, "Type", "Resource Type", null, n -> n.getType(), AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackResource>(this, Object.class, null, "Source", "Resource Source", null, n -> {
            if(callback.getOpenStackNet().getOpenStackNetworkElementByOpenStackId(n.getSourceId())!=null)
                return callback.getOpenStackNet().getOpenStackNetworkElementByOpenStackId(n.getSourceId()) ;

            return n.getSourceId();
             }, AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackResource>(this, Object.class, null, "Project", "Resource Project", null, n -> callback.getOpenStackNet().getOpenStackNetworkElementByOpenStackId(n.getResource_project_id()), AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackResource>(this, Map.class, null, "Metrics", "Resource Metrics", null, n -> n.getMetrics(), AGTYPE.NOAGGREGATION, null, null));
        return res;
    }


    @Override
    public List<AjtRcMenu> getNonBasicRightClickMenusInfo()
    {final List<AjtRcMenu> res = new ArrayList<>();

        res.add(new AjtRcMenu("Get metrics of resource", e -> getSelectedElements().forEach(n -> {

            System.out.println(n);
         n.getOpenStackClient().updateMeterList(n.getSourceId());

        }), (a, b) -> b == 1, null));

        res.add(new AjtRcMenu("Get metrics of resource 2", e -> getSelectedElements().forEach(n -> {

            metricFrame(n);

        }), (a, b) -> b == 1, null));
        res.add(new AjtRcMenu("Refresh", e ->updateTab(), (a, b) -> b >=0, null));


        return res;

    }

    public void refrescaMetrics(ArrayList<OpenStackResource> openStackResources) {

    }

    public void metricFrame(OpenStackResource openStackResource){
        AnalisisFrame analisisFrame = new AnalisisFrame(callback,openStackResource);
        /*JPanel jp1;
        JTextField os_username, os_auth_url,os_project_id,os_user_domain_name;
        JPasswordField os_password;
        JFrame jfM;
        JLabel labelUser,labelPassword,labelUrl,labelProject,labelUDomain;

        jfM = new JFrame("Credentials");
        jp1 = new JPanel(new GridBagLayout());//filas, columnas, espacio entre filas, espacio entre columnas

        jfM.setLayout(null);

        labelUser = new JLabel("OS_USERNAME",  SwingConstants.LEFT);
        labelPassword = new JLabel("OS_PASSWORD",  SwingConstants.LEFT);
        labelUrl = new JLabel("OS_AUTH_URL",  SwingConstants.LEFT);
        labelProject = new JLabel("OS_PROJECT_NAME",  SwingConstants.LEFT);
        labelUDomain = new JLabel("OS_U_DOMAIN_NAME", SwingConstants.LEFT);


        os_username = new JTextField(10);
        os_password = new JPasswordField();
        os_auth_url = new JTextField();
        os_project_id = new JTextField();
        os_user_domain_name = new JTextField();

        Object[] monthStrings = openStackResource.getMetrics().keySet().toArray(); //get month names

        JComboBox jComboBox = new JComboBox(monthStrings);

        jComboBox.addActionListener(new ActionListener() {//add actionlistner to listen for change
            @Override
            public void actionPerformed(ActionEvent e) {

                String s = (String) jComboBox.getSelectedItem();//get the selected item

                openStackResource.getOpenStackClient().updateMeterList2(openStackResource.getSourceId(),openStackResource.getMetrics().get(s));
                Graficos graficos = openStackResource.getOpenStackClient().updateMeasuresList2(openStackResource.getOpenStackClient().openStackMeters.get(0),openStackResource.getMetrics().get(s));
                //System.out.println(openStackResource.getMetrics().get(s));
                if(graficos != null) grafica = graficos.getPanel();

            }
        });

        jComboBox.setSelectedIndex(0);
        JSplitPane splitPane = new JSplitPane();

        splitPane.setTopComponent(callback.getViewEditTopTables().createPanelComponentInfo(ViewEditTopologyTablesPane.AJTableType.METERS,openStackClient).getSecond());


        splitPane.setBottomComponent(callback.getViewEditTopTables().createPanelComponentInfo(ViewEditTopologyTablesPane.AJTableType.MEASURES,openStackClient).getSecond());
        splitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
        splitPane.setResizeWeight(0.3);
        splitPane.setEnabled(true);
        splitPane.setOneTouchExpandable(true);
        splitPane.setDividerLocation(0.4);

        jp1.add(splitPane);

        JSplitPane splitPane2 = new JSplitPane();

        if (grafica != null)
        splitPane2.setTopComponent(grafica);

        splitPane2.setBottomComponent(callback.getViewEditTopTables().createPanelComponentInfo(ViewEditTopologyTablesPane.AJTableType.SUMMARY,openStackClient).getSecond());
        splitPane2.setOrientation(JSplitPane.VERTICAL_SPLIT);
        splitPane2.setResizeWeight(0.3);
        splitPane2.setEnabled(true);
        splitPane2.setOneTouchExpandable(true);
        splitPane2.setDividerLocation(0.4);

        jp1.add(splitPane2);
        jp1.setVisible(true);
        jp1.setSize(900,500);
        JSplitPane splitPane3 = new JSplitPane();

        splitPane3.setTopComponent(jComboBox);


        splitPane3.setBottomComponent(jp1);
        splitPane3.setOrientation(JSplitPane.VERTICAL_SPLIT);
        splitPane3.setResizeWeight(0.3);
        splitPane3.setEnabled(true);
        splitPane3.setOneTouchExpandable(true);
        splitPane3.setDividerLocation(0.4);

        JPanel jPanel2 = new JPanel(new GridBagLayout());
        jPanel2.add(jComboBox);
        jPanel2.add(jp1);
        jPanel2.setVisible(true);
        jPanel2.setSize(900,400);
        jfM.add(jPanel2);

        ImageIcon img = new ImageIcon(getClass().getResource("/resources/common/openstack_logo.png"));
        jfM.setIconImage(img.getImage());
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();

        jfM.setSize(1000, 500);
        jfM.setLocation(dim.width/2-jfM.getSize().width/2, dim.height/2-jfM.getSize().height/2);

        jfM.setResizable(true);
        jfM.setVisible(true);
        jfM.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);*/
    }



}