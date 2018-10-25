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
        if(!yName.equals(null))
            this.yName = yName;
        panel = new JPanel();
        getContentPane().add(panel);
        init(data);


    }

    private void init(double [] data) {

        XYSeries series = new XYSeries("Measures");
        for (int i = 0; i < data.length; i++) {
            series.add(i, data[i]);
        }
        XYSeriesCollection xydata = new XYSeriesCollection(series);
        final JFreeChart chart = ChartFactory.createXYLineChart("Measures of " + this.title,
                "Time/Date", yName, xydata, PlotOrientation.VERTICAL, true, true, false);
        XYPlot plot = (XYPlot) chart.getPlot();
        final NumberAxis xAxis = (NumberAxis) plot.getDomainAxis();
        xAxis.setTickUnit(new NumberTickUnit(data.length*0.25));
        ChartPanel chartPanel = new ChartPanel(chart);
        panel.add(chartPanel);
        JButton jButton = new JButton("Save as PNG");
        jButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                FileChooserNetworkDesign fc_netPlan = new FileChooserNetworkDesign(SystemUtils.getCurrentDir(), Constants.DialogType.NETWORK_DESIGN);;

                assert fc_netPlan != null;

                int rc = fc_netPlan.showOpenDialog(null);
                if (rc != JFileChooser.APPROVE_OPTION) return;

                try  {
                    ChartUtilities.saveChartAsPNG(new File(fc_netPlan.getSelectedFile().getAbsolutePath()+".png"), chart, 400, 300);

                }catch(Exception ex){
                    System.out.println(ex.toString());

                }

            }
        });
        panel.add(jButton);

    }


}