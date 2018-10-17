package com.net2plan.gui.plugins.utils;

import javax.swing.*;
import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class Graficos extends JFrame {

    JPanel panel;
    public Graficos(double [] data){
        setTitle("Como Hacer Graficos con Java");
        setSize(800,600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
        init(data);
    }

    private void init(double [] data) {
        panel = new JPanel();
        getContentPane().add(panel);
        // Fuente de Datos
        DefaultCategoryDataset line_chart_dataset = new DefaultCategoryDataset();

        for(int i=0; i<data.length;i++)
        line_chart_dataset.addValue((double)data[i], "medida", String.valueOf(i));

       /* line_chart_dataset.addValue(300, "visitas", "Agosto");
        line_chart_dataset.addValue(600, "visitas", "Septiembre");
        line_chart_dataset.addValue(1200, "visitas", "Octubre");
        line_chart_dataset.addValue(2400, "visitas", "Noviembre");*/

        // Creando el Grafico
        JFreeChart chart=ChartFactory.createLineChart("Measures",
                "Time","Medida",line_chart_dataset,PlotOrientation.VERTICAL,
                true,true,false);

        // Mostrar Grafico
        ChartPanel chartPanel = new ChartPanel(chart);
        panel.add(chartPanel);
    }


}