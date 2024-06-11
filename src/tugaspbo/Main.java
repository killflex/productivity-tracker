package tugaspbo;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        try {
            Database.connect(
                "localhost",
                "3306",
                "root",
                "",
                "pbo_e081_kel4"
            );

            new Login((login, pengguna) -> {
                new Produktivitas();

                login.dispose();
            });
        } catch (Exception e) {
            System.err.println("Tidak dapat terhubung ke database: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
