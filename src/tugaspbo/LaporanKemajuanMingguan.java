package tugaspbo;


import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalTime;

public class LaporanKemajuanMingguan extends JDialog {
    private Produktivitas app;
    private Pengguna pengguna;

    public LaporanKemajuanMingguan(
            Produktivitas app,
            Pengguna pengguna
    ) {
        this.app = app;
        this.pengguna = pengguna;

        setTitle("Laporan Kemajuan Mingguan");
        setSize(800, 300);
        setLocationRelativeTo(app);

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

        try {
            ResultSet rs = Database.query(
                    "SELECT AVG(tingkat_keproduktifan) AS tingkat_keproduktifan, " +
                    "SEC_TO_TIME(AVG(TIME_TO_SEC(SUBTIME(selesai, mulai)))) AS durasi, " +
                    "DATE_FORMAT(tanggal, '%u/%y') AS tanggal " +
                    "FROM catatan " +
                    "WHERE id_pengguna = " + pengguna.getId() + " " +
                    "GROUP BY DATE_FORMAT(tanggal, '%u/%y')"
            );

            while (rs.next()) {
                dataset.addValue(
                        rs.getDouble("tingkat_keproduktifan"),
                        "Persen",
                        rs.getString("tanggal")
                );

                LocalTime durasi = rs.getTime("durasi").toLocalTime();
                double durasiJam = durasi.getHour() + durasi.getMinute() / 60 + durasi.getSecond() / 3600;

                dataset.addValue(
                        durasiJam,
                        "Jam",
                        rs.getString("tanggal")
                );
            }
        } catch (SQLException e) {
            app.error(e.getMessage());
        }

        return dataset;
    }
}
