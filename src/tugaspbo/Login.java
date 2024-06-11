package tugaspbo;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Login extends JFrame {
    private JPanel mainPanel;
    private JTextField kolomNama;
    private JPasswordField kolomPassword;
    private JButton tombolLogin;
    private JLabel labelDaftar;
    private PenggunaMasuk masuk;
    private Register register;

    public Login(PenggunaMasuk masuk) {
        this.masuk = masuk;
        register = new Register(this, masuk);

        setTitle("Aplikasi Produktivitas");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(960, 420);
        setLocationRelativeTo(null);
        setContentPane(mainPanel);
        setVisible(true);

        aturLogin();
    }

    private void aturLogin() {
        labelDaftar.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                register.setVisible(true);
                setVisible(false);
            }
        });

        tombolLogin.addActionListener(e -> {
            String nama = kolomNama.getText();
            Pengguna pengguna = Pengguna.cari(nama);
            if (pengguna == null) {
                error("Pengguna tidak ditemukan.");

                return;
            }

            String password = String.valueOf(kolomPassword.getPassword());
            if (!pengguna.getPassword().equals(password)) {
                error("Password salah");

                return;
            }

            masuk.run(this, pengguna);
        });
    }

    public void error(String text) {
        JOptionPane.showMessageDialog(this, text, "Kesalahan", JOptionPane.ERROR_MESSAGE);
    }
}
