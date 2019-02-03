package com.net2plan.gui.plugins.utils;

import com.net2plan.gui.plugins.GUINetworkDesign;
import com.net2plan.gui.plugins.networkDesign.openStack.OpenStackClient;
import com.net2plan.gui.plugins.networkDesign.openStack.extra.OpenStackSummary;
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
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.plaf.ColorUIResource;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.Map;

public class AnalisisFrame extends JFrame {
    JPanel firstPanel,graphPanel,informationPanel,jComboBoxPanel,secondPanel,informationPanelAboutMetric,informationPanelAboutMeasures;
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

        jComboBox = new JComboBox(openStackResource.getMetrics().keySet().toArray());
        firstPanel = new JPanel(new BorderLayout());
        secondPanel = new JPanel(new BorderLayout());
        graphPanel = new JPanel();
        informationPanel = new JPanel( new BorderLayout());
        jComboBoxPanel = new JPanel(new MigLayout("fillx, wrap"));

        informationPanelAboutMetric = new JPanel(new MigLayout("fillx, wrap 2"));
        informationPanelAboutMeasures = new JPanel(new MigLayout("fillx, wrap 2"));

        informationPanel.add(informationPanelAboutMetric,BorderLayout.NORTH);
        informationPanel.add(informationPanelAboutMeasures,BorderLayout.CENTER);

        //UIManager.put("ComboBox.selectionBackground", new ColorUIResource(Color.white));
        jComboBoxPanel.add(jComboBox);
        secondPanel.add(jComboBoxPanel,BorderLayout.NORTH);
        secondPanel.add(informationPanel,BorderLayout.SOUTH);

        informationPanelAboutMetric.setBackground(new Color(218,57 ,39));
        informationPanelAboutMeasures.setBackground(new Color(218,57 ,39));
        informationPanel.setBackground(new Color(218,57 ,39));
        secondPanel.setBackground(new Color(218,57 ,39));
        jComboBoxPanel.setBackground(new Color(218,57 ,39));

        graphPanel.setBackground(Color.WHITE);

        firstPanel.add(secondPanel,BorderLayout.WEST);
        firstPanel.add(graphPanel,BorderLayout.EAST);

        add(firstPanel);
        jComboBox.addActionListener(new ActionListener() {//add actionlistner to listen for change
            @Override
            public void actionPerformed(ActionEvent e) {
                recomput();
            }
        });


        ImageIcon img = new ImageIcon(getClass().getResource("/resources/common/openstack_logo.png"));
        setIconImage(img.getImage());

        setResizable(false);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTitle("Analysis window for OpenStack metrics");
        jComboBox.setSelectedIndex(0);

    }


    public void recomput(){
        String s = (String) jComboBox.getSelectedItem();//get the selected item
        String metric_id = openStackResource.getMetrics().get(s);

        graphPanel.removeAll();
        informationPanelAboutMetric.removeAll();
        informationPanelAboutMeasures.removeAll();

        Pair<JPanel,Map<String,Object>> aboutMetric = openStackClient.getAnalisisData(metric_id);

        if(aboutMetric.getFirst()!=null)
            graphPanel.add(aboutMetric.getFirst());

        if(aboutMetric.getSecond()!=null) {

            informationPanelAboutMetric.add(new JLabel("About Metric"),"align label");
            informationPanelAboutMetric.add(new JLabel(""),"growx");

            aboutMetric.getSecond().forEach((n, k) -> {
                JLabel etiqueta = new JLabel(n) ;
                JLabel valor = new JLabel(k.toString());
                etiqueta.setForeground(Color.WHITE);
                valor.setForeground(Color.WHITE);
                informationPanelAboutMetric.add(etiqueta, "align label");
                informationPanelAboutMetric.add(valor, "growx");
            });
        }

        if(openStackClient.openStackSummaries.size()>0){

            OpenStackSummary openStackSummary = openStackClient.openStackSummaries.get(0);

            JLabel etiqueta = new JLabel("About metric`s measures") ;
            JLabel valor = new JLabel("");
            etiqueta.setForeground(Color.black);
            valor.setForeground(Color.black);

            informationPanelAboutMeasures.add(etiqueta,"align label");
            informationPanelAboutMeasures.add(valor,"growx");

            informationPanelAboutMeasures.add(new JLabel("Total"),"align label");
            informationPanelAboutMeasures.add(new JLabel(Integer.toString(openStackClient.openStackMeasures.size())),"growx");

            informationPanelAboutMeasures.add(new JLabel("Max"),"align label");
            informationPanelAboutMeasures.add(new JLabel(Double.toString(openStackSummary.getMax())),"growx");

            informationPanelAboutMeasures.add(new JLabel("Min"),"align label");
            informationPanelAboutMeasures.add(new JLabel(Double.toString(openStackSummary.getMin())),"growx");

        }


        pack();
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation(dim.width/2-getSize().width/2, dim.height/2-getSize().height/2);
        setVisible(true);


    }

}
