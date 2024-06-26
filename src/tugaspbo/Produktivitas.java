package tugaspbo;

import javax.swing.*;
import javax.swing.plaf.basic.BasicSpinnerUI;
import java.awt.*;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static tugaspbo.SwingHelper.*;

public class Produktivitas extends JFrame implements Mulai, Selesai {
    private JSpinner jamMulai;
    private JSpinner menitMulai;
    private JSpinner jamSelesai;
    private JSpinner menitSelesai;
    private JButton tombolMulaiKerja;
    private JButton tombolSelesaiKerja;
    private JSpinner targetJamMulai;
    private JSpinner targetMenitMulai;
    private JSpinner targetJamSelesai;
    private JSpinner targetMenitSelesai;
    private JSpinner targetProduktifitasJam;
    private JSpinner targetProduktifitasMenit;
    private JLabel persenProduktif;
    private JLabel persenTidakProduktif;
    private JLabel telahBerlalu;
    private JLabel sisa;
    private JButton tombolLaporanKemajuan;
    private JPanel mainPanel;
    private JPanel panelChart;
    private JLabel labelJudul;
    private JPanel panelMulai;
    private JPanel panelSelesai;
    private JPanel panelTargetMulai;
    private JPanel panelTargetSelesai;
    private JPanel panelTargetProduktifitas;
    private Pengguna pengguna;
    private ArrayList<Object> komponen = new ArrayList<>();
    private ScheduledExecutorService scheduler;
    private ArrayList<Runnable> perDetik = new ArrayList<>();
    private ArrayList<Runnable> perTick = new ArrayList<>();

    public Produktivitas(Pengguna pengguna) {
        this.pengguna = pengguna;

        setTitle("Aplikasi Produktivitas");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(960, 420);
        setLocationRelativeTo(null);
        setContentPane(mainPanel);
        labelJudul.setText("Selamat datang " + pengguna.getNama());

        aturTampilan();
        setDataPengguna();
        daftarkanKomponen();

        for (Object k : komponen) {
            if (k instanceof BisaDiatur) {
                ((BisaDiatur) k).atur();
            }
        }

        lanjutkan();

        setVisible(true);
    }

    @Override
    public void mulai() {
        for (Object k : komponen) {
            if (k instanceof Mulai) {
                ((Mulai) k).mulai();
            }
        }

        scheduler = Executors.newScheduledThreadPool(1);

        scheduler.scheduleAtFixedRate(() -> {
            for (Runnable schedule : perDetik) {
                try {
                    schedule.run();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, 0, 1, TimeUnit.SECONDS);

        scheduler.scheduleAtFixedRate(() -> {
            for (Runnable schedule : perTick) {
                try {
                    schedule.run();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, 0, 25, TimeUnit.MILLISECONDS);
    }

    @Override
    public void selesai() {
        scheduler.shutdown();

        for (Object k : komponen) {
            if (k instanceof Selesai) {
                ((Selesai) k).selesai();
            }
        }
    }

    public void perbaruiCatatanSekarang() {
        Catatan catatan = pengguna.catatanSekarang();
        if (catatan == null) {
            return;
        }

        catatan
                .setTargetMulai(localTime(targetJamMulai, targetMenitMulai))
                .setTargetSelesai(localTime(targetJamSelesai, targetMenitSelesai))
                .setMulai(localTime(jamMulai, menitMulai))
                .setSelesai(localTime(jamSelesai, menitSelesai))
                .setTargetProduktifitas(localTime(targetProduktifitasJam, targetProduktifitasMenit))
                .update();
    }

    private void daftarkanKomponen() {
        komponen.add(new PerekamWaktuKerja(
                this,
                pengguna,
                targetJamMulai,
                targetMenitMulai,
                targetJamSelesai,
                targetMenitSelesai,
                jamMulai,
                menitMulai,
                jamSelesai,
                menitSelesai,
                tombolMulaiKerja,
                tombolSelesaiKerja,
                telahBerlalu,
                sisa,
                targetProduktifitasJam,
                targetProduktifitasMenit
        ));

        komponen.add(new TingkatKeproduktivitasan(
                this,
                pengguna,
                targetProduktifitasJam,
                targetProduktifitasMenit,
                persenProduktif,
                persenTidakProduktif,
                panelChart,
                jamMulai,
                menitMulai,
                jamSelesai,
                menitSelesai
        ));

        komponen.add(new LaporanKemajuan(
                this,
                pengguna,
                tombolLaporanKemajuan
        ));
    }

    private void setDataPengguna() {
        targetJamMulai.setValue(pengguna.getTargetMulai().getHour());
        targetMenitMulai.setValue(pengguna.getTargetMulai().getMinute());
        targetJamSelesai.setValue(pengguna.getTargetSelesai().getHour());
        targetMenitSelesai.setValue(pengguna.getTargetSelesai().getMinute());
        targetProduktifitasJam.setValue(pengguna.getTargetProduktifitas().getHour());
        targetProduktifitasMenit.setValue(pengguna.getTargetProduktifitas().getMinute());

        Catatan catatan = pengguna.catatanSekarang();
        if (catatan != null) {
            jamMulai.setValue(catatan.getMulai().getHour());
            menitMulai.setValue(catatan.getMulai().getMinute());
            jamSelesai.setValue(catatan.getSelesai().getHour());
            menitSelesai.setValue(catatan.getSelesai().getMinute());
        }
    }

    private void aturTampilan() {
        aturTampilanSpinner(jamMulai, JTextField.RIGHT);
        aturTampilanSpinner(menitMulai, JTextField.LEFT);
        aturTampilanSpinner(jamSelesai, JTextField.RIGHT);
        aturTampilanSpinner(menitSelesai, JTextField.LEFT);
        aturTampilanSpinner(targetJamMulai, JTextField.RIGHT);
        aturTampilanSpinner(targetMenitMulai, JTextField.LEFT);
        aturTampilanSpinner(targetJamSelesai, JTextField.RIGHT);
        aturTampilanSpinner(targetMenitSelesai, JTextField.LEFT);
        aturTampilanSpinner(targetProduktifitasJam, JTextField.RIGHT);
        aturTampilanSpinner(targetProduktifitasMenit, JTextField.LEFT);

        aturTampilanPanelWaktu(panelMulai);
        aturTampilanPanelWaktu(panelSelesai);
        aturTampilanPanelWaktu(panelTargetMulai);
        aturTampilanPanelWaktu(panelTargetSelesai);
        aturTampilanPanelWaktu(panelTargetProduktifitas);
    }

    private void aturTampilanSpinner(JSpinner spinner, int alignment) {
        spinner.setUI(new BasicSpinnerUI() {
            protected Component createNextButton() {
                return null;
            }

            protected Component createPreviousButton() {
                return null;
            }
        });

        JSpinner.NumberEditor editor = new JSpinner.NumberEditor(spinner, "00");

        editor.getTextField().setHorizontalAlignment(alignment);
        spinner.setEditor(editor);
        spinner.setBorder(BorderFactory.createEmptyBorder());
        spinner.setBackground(Color.WHITE);
    }

    private void aturTampilanPanelWaktu(JPanel panel) {
        panel.setBorder(BorderFactory.createLineBorder(new Color(133, 133, 122)));
        panel.setBackground(Color.WHITE);
    }

    private void lanjutkan() {
        Catatan catatan = pengguna.catatanSekarang();
        if (catatan != null) {
            this.mulai();
        }
    }

    public void perDetik(Runnable runnable) {
        perDetik.add(runnable);
    }

    public void perTick(Runnable runnable) {
        perTick.add(runnable);
    }

    public void warning(String text) {
        JOptionPane.showMessageDialog(this, text, "Peringatan", JOptionPane.WARNING_MESSAGE);
    }

    public void error(String text) {
        JOptionPane.showMessageDialog(this, text, "Kesalahan", JOptionPane.ERROR_MESSAGE);
    }
}
