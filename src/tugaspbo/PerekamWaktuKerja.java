/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package tugaspbo;

import java.time.LocalDate;
import java.time.LocalTime;
import javax.swing.*;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import static tugaspbo.SwingHelper.*;

/**
 *
 * @author MSI
 */
public class PerekamWaktuKerja implements BisaDiatur, Mulai {
    private Produktivitas app;
    private Pengguna pengguna;
    private JSpinner targetJamMulai;
    private JSpinner targetMenitMulai;
    private JSpinner targetJamSelesai;
    private JSpinner targetMenitSelesai;
    private JSpinner jamMulai;
    private JSpinner menitMulai;
    private JSpinner jamSelesai;
    private JSpinner menitSelesai;
    private JButton tombolMulaiKerja;
    private JButton tombolSelesaiKerja;
    private JLabel telahBerlalu;
    private JLabel sisa;
    private JSpinner targetProduktifitasJam;
    private JSpinner targetProduktifitasMenit;
    private int detikMulai = 0;

    public PerekamWaktuKerja(
            Produktivitas app,
            Pengguna pengguna,
            JSpinner targetJamMulai,
            JSpinner targetMenitMulai,
            JSpinner targetJamSelesai,
            JSpinner targetMenitSelesai,
            JSpinner jamMulai,
            JSpinner menitMulai,
            JSpinner jamSelesai,
            JSpinner menitSelesai,
            JButton tombolMulaiKerja,
            JButton tombolSelesaiKerja,
            JLabel telahBerlalu,
            JLabel sisa,
            JSpinner targetProduktifitasJam,
            JSpinner targetProduktifitasMenit
    ) {
        this.app = app;
        this.pengguna = pengguna;
        this.targetJamMulai = targetJamMulai;
        this.targetMenitMulai = targetMenitMulai;
        this.targetJamSelesai = targetJamSelesai;
        this.targetMenitSelesai = targetMenitSelesai;
        this.jamMulai = jamMulai;
        this.menitMulai = menitMulai;
        this.jamSelesai = jamSelesai;
        this.menitSelesai = menitSelesai;
        this.tombolMulaiKerja = tombolMulaiKerja;
        this.tombolSelesaiKerja = tombolSelesaiKerja;
        this.telahBerlalu = telahBerlalu;
        this.sisa = sisa;
        this.targetProduktifitasJam = targetProduktifitasJam;
        this.targetProduktifitasMenit = targetProduktifitasMenit;
    }

    @Override
    public void atur() {
        aturTargetMulai();
        aturTargetSelesai();

        aturMulai();
        aturSelesai();

        aturTelahBerlaluDanSisa();

        app.perDetik(() -> {
            LocalTime sekarang = LocalTime.now();

            updateTelahBerlalu(sekarang);
            updateSisa(sekarang);
        });
    }

    @Override
    public void mulai() {
        if (pengguna.catatanHariIni() == null) {
            Catatan.baru(
                    pengguna,
                    localTime(targetJamMulai, targetMenitMulai),
                    localTime(targetJamSelesai, targetMenitSelesai),
                    localTime(jamMulai, menitMulai),
                    localTime(jamSelesai, menitSelesai),
                    localTime(targetProduktifitasJam, targetProduktifitasMenit),
                    0,
                    LocalDate.now()
            );
        }
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

    private void validasiTarget(int waktuSelesai) {
        int jamMulai = (int) targetJamMulai.getValue();
        int menitMulai = (int) targetMenitMulai.getValue();
        int waktuMulai = jamMulai * 60 + menitMulai;

        if (waktuMulai > waktuSelesai) {
            app.error("Waktu selesai tidak boleh lebih awal dari waktu mulai");

            targetJamSelesai.setValue(jamMulai + 1);
            targetMenitSelesai.setValue(menitMulai);
        }
    }

    private void aturTargetMulai() {
        AtomicBoolean skip = new AtomicBoolean(false);

        onChange(targetJamMulai, e -> {
            int waktuMulai = validasiSpinnerJam(targetJamMulai, targetMenitMulai);

            updateSisa();

            pengguna.updateTargetMulai(waktuMulai / 60, waktuMulai % 60);
            app.perbaruiCatatanHariIni();
        }, skip);

        onChange(targetMenitMulai, e -> {
            int waktuMulai = validasiSpinnerMenit(targetJamMulai, targetMenitMulai);

            updateSisa();

            pengguna.updateTargetMulai(waktuMulai / 60, waktuMulai % 60);
            app.perbaruiCatatanHariIni();
        }, skip);
    }

    private void aturTargetSelesai() {
        AtomicBoolean skip = new AtomicBoolean(false);

        onChange(targetJamSelesai, e -> {
            int waktuSelesai = validasiSpinnerJam(targetJamSelesai, targetMenitSelesai);

            validasiTarget(waktuSelesai);
            updateSisa();

            pengguna.updateTargetSelesai(waktuSelesai / 60, waktuSelesai % 60);
            app.perbaruiCatatanHariIni();
        }, skip);

        onChange(targetMenitSelesai, e -> {
            int waktuSelesai = validasiSpinnerMenit(targetJamSelesai, targetMenitSelesai);

            validasiTarget(waktuSelesai);
            updateSisa();

            pengguna.updateTargetSelesai(waktuSelesai / 60, waktuSelesai % 60);
            app.perbaruiCatatanHariIni();
        }, skip);
    }

    private void aturMulai() {
        AtomicBoolean skip = new AtomicBoolean(false);

        onClick(tombolMulaiKerja, e -> {
            int targetMulaiJam = (int) targetJamMulai.getValue();
            int targetMulaiMenit = (int) targetMenitMulai.getValue();
            int targetSelesaiJam = (int) targetJamSelesai.getValue();
            int targetSelesaiMenit = (int) targetMenitSelesai.getValue();

            if (targetMulaiJam + targetMulaiMenit + targetSelesaiJam + targetSelesaiMenit == 0) {
                app.warning("Harap isi target waktu kerja terlebih dahulu");

                return;
            }

            int mulaiJam = (int) jamMulai.getValue();
            int mulaiMenit = (int) menitMulai.getValue();

            LocalTime sekarang = LocalTime.now();
            if (mulaiJam + mulaiMenit == 0) {
                jamMulai.setValue(sekarang.getHour());
                menitMulai.setValue(sekarang.getMinute());
            }
            detikMulai = sekarang.getSecond();

            updateSisa();
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

            updateSisa();
        }, skip);

        onChange(menitSelesai, e -> {
            validasiSpinnerMenit(jamSelesai, menitSelesai);

            updateSisa();
        }, skip);
    }

    private void aturTelahBerlaluDanSisa() {
        Catatan catatan = pengguna.catatanHariIni();

        if (catatan == null) {
            telahBerlalu.setText("Belum dimulai");
            sisa.setText("Belum dimulai");
        } else {
            updateTelahBerlalu(LocalTime.now());
            updateSisa();
        }
    }

    private void updateTelahBerlalu(LocalTime sekarang) {
        int mulaiJam = (int) jamMulai.getValue();
        int mulaiMenit = (int) menitMulai.getValue();

        LocalTime mulai = LocalTime.of(mulaiJam, mulaiMenit, detikMulai);

        LocalTime perbandingan = sekarang;

        if (pengguna.catatanHariIni() != null) {
            LocalTime selesai = pengguna.catatanHariIni().getSelesai();

            if (selesai.getHour() + selesai.getMinute() + selesai.getSecond() > 0) {
                perbandingan = selesai;
            }
        }

        long telahBerlaluWaktu = ChronoUnit.SECONDS.between(mulai, perbandingan);

        long telahBerlaluJam = telahBerlaluWaktu / 3600;
        long telahBerlaluMenit = (telahBerlaluWaktu % 3600) / 60;
        long telahBerlaluDetik = telahBerlaluWaktu % 60;

        telahBerlalu.setText(
                String.format("%02d:%02d:%02d", telahBerlaluJam, telahBerlaluMenit, telahBerlaluDetik)
        );
    }

    private void updateSisa() {
        int mulaiJam = (int) jamMulai.getValue();
        int mulaiMenit = (int) menitMulai.getValue();

        if (mulaiJam + mulaiMenit == 0) {
            sisa.setText("Belum dimulai");
        } else {
            updateSisa(LocalTime.now());
        }
    }

    private void updateSisa(LocalTime sekarang) {
        int selesaiJam = (int) targetJamSelesai.getValue();
        int selesaiMenit = (int) targetMenitSelesai.getValue();

        LocalTime selesai = LocalTime.of(selesaiJam, selesaiMenit);

        long sisaWaktu = ChronoUnit.SECONDS.between(sekarang, selesai);

        long sisaJam = sisaWaktu / 3600;
        long sisaMenit = (sisaWaktu % 3600) / 60;
        long sisaDetik = sisaWaktu % 60;

        String format = sisaDetik < 0 ? "-%02d:%02d:%02d" : "%02d:%02d:%02d";

        sisa.setText(String.format(format, Math.abs(sisaJam), Math.abs(sisaMenit), Math.abs(sisaDetik)));
    }
}
