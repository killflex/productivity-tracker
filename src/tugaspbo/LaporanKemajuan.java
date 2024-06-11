package tugaspbo;

import javax.swing.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class LaporanKemajuan extends JDialog implements BisaDiatur {

    private JPanel mainPanel;
    private JButton tombolHarian;
    private JButton tombolMingguan;
    private JLabel hariPalingProduktif;
    private JLabel rataRataKeproduktifan;
    private JLabel palingProduktifHari;
    private Produktivitas app;
    private Pengguna pengguna;
    private JButton tombolLaporanKemajuan;

    public LaporanKemajuan(
            Produktivitas app,
            Pengguna pengguna,
            JButton tombolLaporanKemajuan
    ) {
        this.app = app;
        this.pengguna = pengguna;
        this.tombolLaporanKemajuan = tombolLaporanKemajuan;

        setTitle("Laporan Kemajuan");
        setContentPane(mainPanel);
        setDefaultCloseOperation(HIDE_ON_CLOSE);
        setSize(960, 420);
        setLocationRelativeTo(app);
    }

    @Override
    public void atur() {
        aturTombolLaporanKemajuan();
        aturTombolHarian();
        aturTombolMingguan();
        aturStatistik();
    }

    private void aturTombolLaporanKemajuan() {
        tombolLaporanKemajuan.addActionListener(e -> {
            setVisible(true);
        });
    }

    private void aturTombolHarian() {
        tombolHarian.addActionListener(e -> {
            LaporanKemajuanHarian harian = new LaporanKemajuanHarian(app, pengguna);
        });
    }

    private void aturTombolMingguan() {
        tombolMingguan.addActionListener(e -> {
            LaporanKemajuanMingguan mingguan = new LaporanKemajuanMingguan(app, pengguna);
        });
    }

    private void aturStatistik() {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
            SimpleDateFormat sdfId = new SimpleDateFormat("EEEE, dd MMMM yyyy", new Locale("id", "ID"));

            ResultSet hariPalingProduktifRS = Database.query(
                    "SELECT tanggal " +
                    "FROM catatan " +
                    "WHERE id_pengguna = " + pengguna.getId() + " " +
                    "ORDER BY tingkat_keproduktifan " +
                    "LIMIT 1"
            );

            if (hariPalingProduktifRS.next()) {
                hariPalingProduktif.setText(
                        sdfId.format(sdf.parse(hariPalingProduktifRS.getDate("tanggal").toString()))
                );
            } else {
                hariPalingProduktif.setText("Tidak ada data");
            }

            ResultSet rataRataKeproduktifanRS = Database.query(
                    "SELECT AVG(tingkat_keproduktifan) AS rataRata FROM catatan WHERE id_pengguna = " + pengguna.getId()
            );

            if (rataRataKeproduktifanRS.next()) {
                rataRataKeproduktifan.setText(rataRataKeproduktifanRS.getInt("rataRata") + "%");
            } else {
                rataRataKeproduktifan.setText("Tidak ada data");
            }

            ResultSet palingProduktifHariRS = Database.query(
                    "SELECT DAYNAME(tanggal) AS hari " +
                    "FROM catatan " +
                    "WHERE id_pengguna = " + pengguna.getId() + " " +
                    "GROUP BY DAYNAME(tanggal) " +
                    "ORDER BY AVG(tingkat_keproduktifan) DESC " +
                    "LIMIT 1"
            );

            if (palingProduktifHariRS.next()) {
                String hari = switch (palingProduktifHariRS.getString("hari")) {
                    case "Sunday" -> "Minggu";
                    case "Monday" -> "Senin";
                    case "Tuesday" -> "Selasa";
                    case "Wednesday" -> "Rabu";
                    case "Thursday" -> "Kamis";
                    case "Friday" -> "Jumat";
                    case "Saturday" -> "Sabtu";
                    default -> "Tidak diketahui";
                };

                palingProduktifHari.setText(hari);
            } else {
                palingProduktifHari.setText("Tidak ada data");
            }
        } catch (SQLException | ParseException e) {
            app.error(e.getMessage());
        }
    }
}
