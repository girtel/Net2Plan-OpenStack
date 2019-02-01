package com.net2plan.gui.plugins.utils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.swing.ImageIcon;

import com.net2plan.gui.plugins.GUINetworkDesign;
import com.net2plan.gui.plugins.networkDesign.FileChooserNetworkDesign;
import com.net2plan.gui.plugins.networkDesign.openStack.OpenStackClient;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.ViewEditTopologyTablesPane;
import com.net2plan.internal.Constants;
import com.net2plan.internal.SystemUtils;
import org.apache.commons.io.FileUtils;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class Graficos extends JFrame {

    JPanel panel;
    String yName = "No unit available";
    String title;
    GUINetworkDesign callback;
    OpenStackClient openStackClient;

    public Graficos(GUINetworkDesign callback, OpenStackClient openStackClient,String title, String yName, double [] data){
        setTitle(title);
        setSize(800,500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(false);
        this.title=title;
        if(yName !="null") {
            this.yName = yName;
            System.out.println("not null please"+this.yName);
        }
        this.callback = callback;
        this.openStackClient = openStackClient;
        panel = new JPanel();
        getContentPane().add(panel);
        init(data);
        ImageIcon img = new ImageIcon(getClass().getResource("/resources/common/openstack_logo.png"));
        setIconImage(img.getImage());


    }

    private void init(double [] data) {

        XYSeries series = new XYSeries("Measures");
        for (int i = 0; i < data.length; i++) {
            series.add(i, data[i]);
        }
        XYSeriesCollection xydata = new XYSeriesCollection(series);
        final JFreeChart chart = ChartFactory.createXYLineChart( this.title,
                "Timestamp", yName, xydata, PlotOrientation.VERTICAL, true, true, false);
        XYPlot plot = (XYPlot) chart.getPlot();
        final NumberAxis xAxis = (NumberAxis) plot.getDomainAxis();
        xAxis.setTickUnit(new NumberTickUnit(data.length*0.25));
        ChartPanel chartPanel = new ChartPanel(chart);

        JPopupMenu jPopupMenu = chartPanel.getPopupMenu();
         jPopupMenu.add("Get table of measures").addActionListener(new ActionListener() {
             @Override
             public void actionPerformed(ActionEvent actionEvent) {
                 JFrame jFrame = new JFrame("Measures");
                 JPanel jPanel = new JPanel();
                 jPanel.add(callback.getViewEditTopTables().createPanelComponentInfo(ViewEditTopologyTablesPane.AJTableType.MEASURES,openStackClient).getSecond());
                 jFrame.add(jPanel);
                 jFrame.setResizable(false);
                 jFrame.pack();
                 jFrame.setVisible(true);
             }
         });
        panel.add(chartPanel);
        panel.setBackground(Color.WHITE);

    }

    public JPanel getPanel () {return this.panel;}

}