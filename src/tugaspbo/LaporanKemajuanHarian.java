package tugaspbo;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import java.awt.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalTime;

public class LaporanKemajuanHarian extends JDialog {
    private Produktivitas app;
    private Pengguna pengguna;

    public LaporanKemajuanHarian(
            Produktivitas app,
            Pengguna pengguna
    ) {
        this.app = app;
        this.pengguna = pengguna;

        setTitle("Laporan Kemajuan Harian");
        setSize(800, 300);
        setLocationRelativeTo(app);

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
        panel.setPreferredSize(new Dimension(960, 420));

        setContentPane(panel);
        setVisible(true);
    }

    private DefaultCategoryDataset createDataset() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        try {
            ResultSet rs = Database.query(
                    "SELECT tingkat_keproduktifan, SUBTIME(selesai, mulai) AS durasi, DATE_FORMAT(tanggal, '%d/%m/%y') AS tanggal " +
                    "FROM catatan WHERE id_pengguna = " + pengguna.getId()
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
