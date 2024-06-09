package tugaspbo;


import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;

public class LaporanKemajuanMingguan extends JDialog {
    public LaporanKemajuanMingguan() {
        setTitle("Laporan Kemajuan Mingguan");
        setSize(800, 300);
        setLocationRelativeTo(null);

        DefaultCategoryDataset dataset = createDataset();

        JFreeChart chart = ChartFactory.createLineChart(
                "Keproduktifan Mingguan",
                "Minggu",
                "",
                dataset
        );

        CategoryPlot plot = (CategoryPlot) chart.getPlot();
        CategoryAxis domainAxis = plot.getDomainAxis();

        domainAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_45);

        ChartPanel panel = new ChartPanel(chart);

        setContentPane(panel);
        setVisible(true);
    }

    private DefaultCategoryDataset createDataset() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        dataset.addValue(27, "Persen", "1/24");
        dataset.addValue(32, "Persen", "2/24");
        dataset.addValue(40, "Persen", "3/24");
        dataset.addValue(42, "Persen", "4/24");
        dataset.addValue(37, "Persen", "5/24");
        dataset.addValue(30, "Persen", "6/24");
        dataset.addValue(25, "Persen", "7/24");

        dataset.addValue(6.62, "Jam", "1/24");
        dataset.addValue(7.15, "Jam", "2/24");
        dataset.addValue(8.2, "Jam", "3/24");
        dataset.addValue(7.52, "Jam", "4/24");
        dataset.addValue(6.8, "Jam", "5/24");
        dataset.addValue(6.3, "Jam", "6/24");
        dataset.addValue(5.5, "Jam", "7/24");

        return dataset;
    }
}
