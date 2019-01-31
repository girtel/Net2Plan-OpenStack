package com.net2plan.gui.plugins.utils;

import com.net2plan.gui.plugins.GUINetworkDesign;
import com.net2plan.gui.plugins.networkDesign.openStack.OpenStackClient;
import com.net2plan.gui.plugins.networkDesign.openStack.telemetry.OpenStackResource;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.ViewEditTopologyTablesPane;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.AdvancedJTable_abstractElement;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.AdvancedJTable_networkElement;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.compute.*;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.extra.AdvancedJTable_summaries;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.identity.*;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.image.AdvancedJTable_imagesV2;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.network.AdvancedJTable_networks;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.network.AdvancedJTable_ports;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.network.AdvancedJTable_routers;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.network.AdvancedJTable_subnets;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.telemetry.AdvancedJTable_measures;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.telemetry.AdvancedJTable_meters;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.telemetry.AdvancedJTable_resources;
import com.net2plan.interfaces.networkDesign.NetPlan;
import com.net2plan.internal.ErrorHandling;
import com.net2plan.utils.Pair;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.EnumMap;
import java.util.Map;

public class AnalisisFrame extends JFrame {
    JPanel grafica;
    GUINetworkDesign callback;
    OpenStackResource openStackResource;
    JComboBox jComboBox;
    JSplitPane splitPane,splitPane2,splitPane3;
    JPanel jp1;
    OpenStackClient openStackClient;

    private final Map<ViewEditTopologyTablesPane.AJTableType, Pair<AdvancedJTable_abstractElement, FilteredTablePanel>> ajTables = new EnumMap<>(ViewEditTopologyTablesPane.AJTableType.class);

    public AnalisisFrame (GUINetworkDesign callback, OpenStackResource openStackResource){
        this.callback = callback;
        this.openStackResource=openStackResource;
        this.openStackClient = openStackResource.getOpenStackClient();

        ImageIcon img = new ImageIcon(getClass().getResource("/resources/common/openstack_logo.png"));
        setIconImage(img.getImage());
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();

        setSize(1000, 500);
        setLocation(dim.width/2-getSize().width/2, dim.height/2-getSize().height/2);
        setResizable(true);
        setVisible(true);
        setLayout(new BorderLayout());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        jComboBox = new JComboBox(openStackResource.getMetrics().keySet().toArray());
        jp1 = new JPanel();//filas, columnas, espacio entre filas, espacio entre columnas

        jp1.setVisible(true);
        jp1.setSize(900,500);

        splitPane = new JSplitPane();
        splitPane2 = new JSplitPane();

        splitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
        splitPane.setResizeWeight(0.3);
        splitPane.setEnabled(true);
        splitPane.setOneTouchExpandable(true);
        splitPane.setDividerLocation(0.4);

        splitPane2.setOrientation(JSplitPane.VERTICAL_SPLIT);
        splitPane2.setResizeWeight(0.3);
        splitPane2.setEnabled(true);
        splitPane2.setOneTouchExpandable(true);
        splitPane2.setDividerLocation(0.4);

        JPanel jPanel2 = new JPanel();
        jPanel2.add(jComboBox);
        jPanel2.add(jp1);
        jPanel2.setVisible(true);
        jPanel2.setSize(900,400);
        JScrollPane jScrollPane = new JScrollPane(jPanel2);
        add(jScrollPane);

        jp1.add(splitPane);
        jp1.add(splitPane2);

         jComboBox.addActionListener(new ActionListener() {//add actionlistner to listen for change
            @Override
            public void actionPerformed(ActionEvent e) {
                updateView();
            }
        });

        jComboBox.setSelectedIndex(0);

    }


    public void recomput(){
        String s = (String) jComboBox.getSelectedItem();//get the selected item

        openStackResource.getOpenStackClient().updateMeterList2(openStackResource.getSourceId(),openStackResource.getMetrics().get(s));
        Graficos graficos = openStackResource.getOpenStackClient().updateMeasuresList2(openStackResource.getOpenStackClient().openStackMeters.get(0),openStackResource.getMetrics().get(s));
        //System.out.println(openStackResource.getMetrics().get(s));
        if(graficos != null) grafica = graficos.getPanel();

        splitPane.setTopComponent(callback.getViewEditTopTables().createPanelComponentInfo(ViewEditTopologyTablesPane.AJTableType.METERS,openStackClient).getSecond());
        splitPane.setBottomComponent(callback.getViewEditTopTables().createPanelComponentInfo(ViewEditTopologyTablesPane.AJTableType.MEASURES,openStackClient).getSecond());

        if (grafica != null)
            splitPane2.setTopComponent(grafica);

        splitPane2.setBottomComponent(callback.getViewEditTopTables().createPanelComponentInfo(ViewEditTopologyTablesPane.AJTableType.SUMMARY,openStackClient).getSecond());



    }
    public void updateView() {

        recomput();

        //ajTables.values().stream().map(t -> t.getFirst()).forEach(t -> t.updateView());
        //ajTables.values().stream().map(t -> t.getSecond()).forEach(t -> t.updateHeader());




    }

}
