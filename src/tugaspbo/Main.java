package tugaspbo;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.plaf.FontUIResource;
import java.awt.*;
import java.util.Enumeration;

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
            aturTampilan();

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

    private static void aturTampilan() {
        Color abuMuda = new Color(133, 133, 122);
        Color oranye = new Color(245, 158, 11);
        Color oranyeGelap = new Color(217, 119, 6);
        Color putih = new Color(250, 250, 250);

        Border paddingBorder = BorderFactory.createEmptyBorder(8, 12, 8, 12);
        Border borderAbuMuda = BorderFactory.createLineBorder(abuMuda);
        Border inputBorder = BorderFactory.createCompoundBorder(borderAbuMuda, paddingBorder);
        Border buttonBorder = BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(oranyeGelap), paddingBorder);

        UIManager.put("TextField.border", inputBorder);
        UIManager.put("PasswordField.border", inputBorder);

        UIManager.put("Button.border", buttonBorder);
        UIManager.put("Button.background", oranye);
        UIManager.put("Button.foreground", putih);
        UIManager.put("Button.select", oranyeGelap);
        UIManager.put("Button.disabledText", abuMuda);

        Font font = new Font("Helvetica", Font.BOLD, 14);
        Enumeration<Object> keys = UIManager.getDefaults().keys();
        while (keys.hasMoreElements()) {
            Object key = keys.nextElement();
            Object value = UIManager.get(key);
            if (value instanceof FontUIResource) {
                UIManager.put(key, font);
            }
        }
    }
}
