package tugaspbo;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Register extends JFrame {
    private JPanel mainPanel;
    private JTextField kolomNama;
    private JPasswordField kolomPassword;
    private JPasswordField kolomKonfirmasiPassword;
    private JButton tombolDaftar;
    private JLabel labelMasuk;
    private Login login;
    private PenggunaMasuk masuk;

    public Register(Login login, PenggunaMasuk masuk) {
        this.login = login;
        this.masuk = masuk;

        setTitle("Aplikasi Produktivitas");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(960, 420);
        setLocationRelativeTo(login);
        setContentPane(mainPanel);

        aturRegister();
    }

    private void aturRegister() {
        labelMasuk.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                login.setVisible(true);
                setVisible(false);
            }
        });

        tombolDaftar.addActionListener(e -> {
            String nama = kolomNama.getText();
            if (nama.isEmpty()) {
                login.error("Nama tidak boleh kosong");

                return;
            }

            Pengguna pengguna = Pengguna.cari(nama);
            if (pengguna != null) {
                login.error("Pengguna dengan nama ini sudah ada.");

                return;
            }

            String password = String.valueOf(kolomPassword.getPassword());
            if (password.isEmpty()) {
                login.error("Password tidak boleh kosong");

                return;
            }

            String konfirmasiPassword = String.valueOf(kolomKonfirmasiPassword.getPassword());
            if (!password.equals(konfirmasiPassword)) {
                login.error("Konfirmasi password salah");

                return;
            }

            pengguna = Pengguna.baru(nama, password);

            masuk.run(login, pengguna);
        });
    }
}
