/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.piechartt;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;

import javax.swing.*;
import java.awt.*;

public class PieChartt extends JFrame {

    public PieChartt() {
        // Create dataset
        DefaultPieDataset dataset = createDataset();

        // Create chart
        JFreeChart chart = ChartFactory.createPieChart(
                "Sample Pie Chart",   // chart title
                dataset,          // dataset
                true,             // include legend
                true,
                false);

        // Customize the chart
        PiePlot plot = (PiePlot) chart.getPlot();
        plot.setSectionPaint("Category A", new Color(255, 0, 0));
        plot.setSectionPaint("Category B", new Color(0, 255, 0));
        plot.setSectionPaint("Category C", new Color(0, 0, 255));
        

        // Create Panel
        ChartPanel panel = new ChartPanel(chart);
        setContentPane(panel);
    }

    private DefaultPieDataset createDataset() {
        DefaultPieDataset dataset = new DefaultPieDataset();
        dataset.setValue("A", 45);
        dataset.setValue("B", 25);
        dataset.setValue("C", 30);
        return dataset;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            PieChartt example = new PieChartt();
            example.setSize(800, 600);
            example.setLocationRelativeTo(null);
            example.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            example.setVisible(true);
        });
    }
}

