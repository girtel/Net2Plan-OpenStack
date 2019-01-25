package com.net2plan.gui.plugins.utils;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.swing.ImageIcon;

import com.net2plan.gui.plugins.networkDesign.FileChooserNetworkDesign;
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
    public Graficos(String title, String yName, double [] data){
        setTitle(title);
        setSize(800,500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
        this.title=title;
        if(yName !="null") {
            this.yName = yName;
            System.out.println("not null please"+this.yName);
        }
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
        panel.add(chartPanel);

    }


}