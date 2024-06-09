package tugaspbo;

import javax.swing.*;

public class LaporanKemajuan extends JFrame {

    private JPanel mainPanel;
    private JButton tombolHarian;
    private JButton tombolMingguan;
    private JLabel hariPalingProduktif;
    private JLabel rataRataKeproduktifan;
    private JLabel palingProduktifHari;

    public LaporanKemajuan() {
        setTitle("Laporan Kemajuan");
        setContentPane(mainPanel);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(800, 300);
        setLocationRelativeTo(null);

        hariPalingProduktif.setText("Senin, 20 April 2024");
        rataRataKeproduktifan.setText("72%");
        palingProduktifHari.setText("Rabu");

        tombolHarian.addActionListener(e -> {
            LaporanKemajuanHarian harian = new LaporanKemajuanHarian();
        });

        tombolMingguan.addActionListener(e -> {
            LaporanKemajuanMingguan mingguan = new LaporanKemajuanMingguan();
        });

        setVisible(true);
    }

    public static void main(String[] args) {
        new LaporanKemajuan();
    }
}
