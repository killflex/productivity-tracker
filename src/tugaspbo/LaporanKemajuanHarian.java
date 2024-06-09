package tugaspbo;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;

public class LaporanKemajuanHarian extends JDialog {
    public LaporanKemajuanHarian() {
        setTitle("Laporan Kemajuan Harian");
        setSize(800, 300);
        setLocationRelativeTo(null);

        DefaultCategoryDataset dataset = createDataset();

        JFreeChart chart = ChartFactory.createLineChart(
                "Keproduktifan Harian",
                "Hari",
                "",
                dataset
        );

        CategoryPlot plot = (CategoryPlot) chart.getPlot();
        CategoryAxis domainAxis = plot.getDomainAxis();

        domainAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_90);

        ChartPanel panel = new ChartPanel(chart);

        setContentPane(panel);
        setVisible(true);
    }

    private DefaultCategoryDataset createDataset() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        dataset.addValue(27, "Persen", "01/06/24");
        dataset.addValue(32, "Persen", "02/06/24");
        dataset.addValue(40, "Persen", "03/06/24");
        dataset.addValue(42, "Persen", "04/06/24");
        dataset.addValue(37, "Persen", "05/06/24");
        dataset.addValue(30, "Persen", "06/06/24");
        dataset.addValue(25, "Persen", "07/06/24");
        dataset.addValue(27, "Persen", "08/06/24");
        dataset.addValue(32, "Persen", "09/06/24");
        dataset.addValue(40, "Persen", "10/06/24");

        dataset.addValue(6.62, "Jam", "01/06/24");
        dataset.addValue(7.15, "Jam", "02/06/24");
        dataset.addValue(8.2, "Jam", "03/06/24");
        dataset.addValue(7.52, "Jam", "04/06/24");
        dataset.addValue(6.8, "Jam", "05/06/24");
        dataset.addValue(6.3, "Jam", "06/06/24");
        dataset.addValue(5.5, "Jam", "07/06/24");
        dataset.addValue(6.62, "Jam", "08/06/24");
        dataset.addValue(7.15, "Jam", "09/06/24");
        dataset.addValue(8.2, "Jam", "10/06/24");

        return dataset;
    }
}
