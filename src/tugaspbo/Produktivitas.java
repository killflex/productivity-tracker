package tugaspbo;

import javax.swing.*;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

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
    private ArrayList<Object> komponen = new ArrayList<>();
    private ScheduledExecutorService scheduler;
    private ArrayList<Runnable> perDetik = new ArrayList<>();
    private ArrayList<Runnable> perTick = new ArrayList<>();

    public Produktivitas() {
        setTitle("Aplikasi Produktivitas");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(960, 420);
        setLocationRelativeTo(null);
        setContentPane(mainPanel);

        daftarkanKomponen();

        for (Object k : komponen) {
            if (k instanceof BisaDiatur) {
                ((BisaDiatur) k).atur();
            }
        }

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

    private void daftarkanKomponen() {
        //
    }
}
