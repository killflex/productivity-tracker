package tugaspbo;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.Plot;
import org.jfree.data.general.DefaultPieDataset;

import javax.swing.*;
import java.awt.*;
import java.time.LocalTime;
import java.util.concurrent.atomic.AtomicBoolean;

import static tugaspbo.SwingHelper.*;

public class TingkatKeproduktivitasan implements BisaDiatur, Mulai, Selesai {
    private Produktivitas app;
    private JSpinner targetProduktifitasJam;
    private JSpinner targetProduktifitasMenit;
    private JLabel persenProduktif;
    private JLabel persenTidakProduktif;
    private JPanel panelChart;
    private JSpinner jamMulai;
    private JSpinner menitMulai;
    private JSpinner jamSelesai;
    private JSpinner menitSelesai;
    private DefaultPieDataset chartDataset;
    private int detikMulai;
    private int milidetikMulai;
    private boolean sedangBejalan = false;

    public TingkatKeproduktivitasan(
            Produktivitas app,
            JSpinner targetProduktifitasJam,
            JSpinner targetProduktifitasMenit,
            JLabel persenProduktif,
            JLabel persenTidakProduktif,
            JPanel panelChart,
            JSpinner jamMulai,
            JSpinner menitMulai,
            JSpinner jamSelesai,
            JSpinner menitSelesai
    ) {
        this.app = app;
        this.targetProduktifitasJam = targetProduktifitasJam;
        this.targetProduktifitasMenit = targetProduktifitasMenit;
        this.persenProduktif = persenProduktif;
        this.persenTidakProduktif = persenTidakProduktif;
        this.panelChart = panelChart;
        this.jamMulai = jamMulai;
        this.menitMulai = menitMulai;
        this.jamSelesai = jamSelesai;
        this.menitSelesai = menitSelesai;
    }

    @Override
    public void atur() {
        aturTarget();
        aturChart();

        app.perTick(this::updateMeteranProduktivitas);
    }

    @Override
    public void mulai() {
        LocalTime sekarang = LocalTime.now();

        detikMulai = sekarang.getSecond();
        milidetikMulai = sekarang.getNano() / 1_000_000;
        sedangBejalan = true;

        updateChart(0, 0);
    }

    @Override
    public void selesai() {
        sedangBejalan = false;
    }

    private void updateMeteranProduktivitas() {
        double targetWaktu = (double) waktu(targetProduktifitasJam, targetProduktifitasMenit) * 60_000;
        double waktuMulai = (double) waktu(jamMulai, menitMulai) * 60_000 + detikMulai * 1000 + milidetikMulai;
        double waktuSelesai = (double) waktu(jamSelesai, menitSelesai) * 60_000;

        if (waktuSelesai == 0) {
            LocalTime sekarang = LocalTime.now();

            waktuSelesai = sekarang.getHour() * 3_600_000 + sekarang.getMinute() * 60_000 + sekarang.getSecond() * 1000 + ((double) sekarang.getNano() / 1_000_000);
        }

        double produktif = targetWaktu / (waktuSelesai - waktuMulai) * 100;

        if (produktif >= 100) {
            persenProduktif.setText("100%");
            persenTidakProduktif.setText("0%");

            updateChart(100, 0);
        } else {
            double tidakProduktif = 100 - produktif;

            persenProduktif.setText(String.format("%.1f", produktif) + "%");
            persenTidakProduktif.setText(String.format("%.1f", tidakProduktif) + "%");

            updateChart(produktif, tidakProduktif);
        }
    }

    private void aturTarget() {
        AtomicBoolean skip = new AtomicBoolean(false);

        onChange(targetProduktifitasJam, e -> {
            validasiSpinnerJam(targetProduktifitasJam, targetProduktifitasMenit);

            if (sedangBejalan) {
                updateMeteranProduktivitas();
            }
        }, skip);

        onChange(targetProduktifitasMenit, e -> {
            validasiSpinnerMenit(targetProduktifitasJam, targetProduktifitasMenit);

            if (sedangBejalan) {
                updateMeteranProduktivitas();
            }
        }, skip);
    }

    private void aturChart() {
        chartDataset = new DefaultPieDataset();

        JFreeChart chart = ChartFactory.createPieChart(null, chartDataset, true, true, false);
        JPanel chartPanel = new ChartPanel(chart);
        Plot chartPlot = chart.getPlot();
        Color abu = new Color(238, 238, 238);

        chartPanel.setPreferredSize(new Dimension(180, 180));
        chartPlot.setBackgroundPaint(abu);
        chartPlot.setOutlinePaint(abu);
        chart.setBackgroundPaint(abu);
        chart.setBorderPaint(abu);

        panelChart.add(chartPanel);
    }

    private void updateChart(double produktif, double tidakProduktif) {
        chartDataset.setValue("Tidak Produktif", tidakProduktif);
        chartDataset.setValue("Produktif", produktif);
    }
}
