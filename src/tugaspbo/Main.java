package tugaspbo;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        boolean terhubung = false;

        try {
            Database.connect(
                "localhost",
                "3306",
                "root",
                "",
                "pbo_e081_kel4"
            );

            Runtime.getRuntime().addShutdownHook(new Thread(Database::disconnect));

            terhubung = true;
        } catch (Exception e) {
            System.err.println("Tidak dapat terhubung ke database: " + e.getMessage());
            e.printStackTrace();

            Database.error(e.getMessage());
        }

        if (terhubung) {
            try {
                new Login((login, pengguna) -> {
                    try {
                        new Produktivitas(pengguna);

                        login.dispose();
                    } catch (Exception e) {
                        System.err.println("Terjadi kesalahan: " + e.getMessage());
                        e.printStackTrace();

                        JOptionPane.showMessageDialog(null, e.getMessage(), "Kesalahan", JOptionPane.ERROR_MESSAGE);
                    }
                });
            } catch (Exception e) {
                System.err.println("Terjadi kesalahan: " + e.getMessage());
                e.printStackTrace();

                JOptionPane.showMessageDialog(null, e.getMessage(), "Kesalahan", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
