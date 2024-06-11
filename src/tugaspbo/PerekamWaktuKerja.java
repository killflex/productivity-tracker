/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package tugaspbo;

import java.time.LocalTime;
import javax.swing.*;
import java.util.concurrent.atomic.AtomicBoolean;

import static tugaspbo.SwingHelper.*;

/**
 *
 * @author MSI
 */
public class PerekamWaktuKerja implements BisaDiatur {
    private Produktivitas app;
    private JSpinner jamMulai;
    private JSpinner menitMulai;
    private JSpinner jamSelesai;
    private JSpinner menitSelesai;
    private JButton tombolMulaiKerja;
    private JButton tombolSelesaiKerja;

    public PerekamWaktuKerja(
            Produktivitas app,
            JSpinner jamMulai,
            JSpinner menitMulai,
            JSpinner jamSelesai,
            JSpinner menitSelesai,
            JButton tombolMulaiKerja,
            JButton tombolSelesaiKerja
    ) {
        this.app = app;
        this.jamMulai = jamMulai;
        this.menitMulai = menitMulai;
        this.jamSelesai = jamSelesai;
        this.menitSelesai = menitSelesai;
        this.tombolMulaiKerja = tombolMulaiKerja;
        this.tombolSelesaiKerja = tombolSelesaiKerja;
    }

    @Override
    public void atur() {
        aturMulai();
        aturSelesai();
    }

    private void setEnableMulai(boolean kondisi) {
        tombolMulaiKerja.setEnabled(kondisi);
        jamMulai.setEnabled(kondisi);
        menitMulai.setEnabled(kondisi);
    }

    private void setEnableSelesai(boolean kondisi) {
        tombolSelesaiKerja.setEnabled(kondisi);
        jamSelesai.setEnabled(kondisi);
        menitSelesai.setEnabled(kondisi);
    }

    private void aturMulai() {
        AtomicBoolean skip = new AtomicBoolean(false);

        onClick(tombolMulaiKerja, e -> {
            int mulaiJam = (int) jamMulai.getValue();
            int mulaiMenit = (int) menitMulai.getValue();

            if (mulaiJam + mulaiMenit == 0) {
                LocalTime sekarang = LocalTime.now();

                jamMulai.setValue(sekarang.getHour());
                menitMulai.setValue(sekarang.getMinute());
            }

            setEnableMulai(false);
            setEnableSelesai(true);
            app.mulai();
        }, skip);

        onChange(jamMulai, e -> {
            validasiSpinnerJam(jamMulai, menitMulai);
        }, skip);

        onChange(menitMulai, e -> {
            validasiSpinnerMenit(jamMulai, menitMulai);
        }, skip);
    }

    private void aturSelesai() {
        AtomicBoolean skip = new AtomicBoolean(false);

        onClick(tombolSelesaiKerja, e -> {
            int mulaiJam = (int) jamMulai.getValue();
            int mulaiMenit = (int) menitMulai.getValue();

            if (mulaiJam == 0 && mulaiMenit == 0) {
                app.warning("Anda belum mulai bekerja");

                return;
            }

            LocalTime sekarang = LocalTime.now();

            jamSelesai.setValue(sekarang.getHour());
            menitSelesai.setValue(sekarang.getMinute());

            setEnableMulai(true);
            setEnableSelesai(false);
            app.selesai();
        }, skip);

        onChange(jamSelesai, e -> {
            validasiSpinnerJam(jamSelesai, menitSelesai);
        }, skip);

        onChange(menitSelesai, e -> {
            validasiSpinnerMenit(jamSelesai, menitSelesai);
        }, skip);
    }
}
