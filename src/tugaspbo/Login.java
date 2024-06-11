package tugaspbo;

import javax.swing.*;

public class Login extends JFrame {
    private JPanel mainPanel;
    private JTextField kolomNama;
    private JPasswordField kolomPassword;
    private JButton tombolLogin;
    private PenggunaMasuk masuk;

    public Login(PenggunaMasuk masuk) {
        this.masuk = masuk;

        setTitle("Aplikasi Produktivitas");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(960, 420);
        setLocationRelativeTo(null);
        setContentPane(mainPanel);
        setVisible(true);

        aturLogin();
    }

    private void aturLogin() {
        tombolLogin.addActionListener(e -> {
            String nama = kolomNama.getText();

            Pengguna pengguna = (Pengguna) Pengguna.cari(nama);
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
