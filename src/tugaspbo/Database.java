package tugaspbo;

import javax.swing.*;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;

public abstract class Database {
    private static Connection connection;
    private static Statement statement;

    public static void connect(
            String host,
            String port,
            String username,
            String password,
            String database
    ) throws SQLException {
        String url = "jdbc:mysql://" + host + ":" + port + "/" + database;

        try {
            connection = DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            switch (e.getErrorCode()) {
                case 0: {
                    throw new RuntimeException("Driver database tidak ditemukan.", e);
                }

                case 1049: {
                    String urlTanpaDB = "jdbc:mysql://" + host + ":" + port + "/";

                    connection = DriverManager.getConnection(urlTanpaDB, username, password);

                    statement = connection.createStatement();
                    statement.execute("CREATE DATABASE " + database);
                    statement.execute("USE " + database);

                    ArrayList<String> sql = schema();

                    for (String q : sql) {
                        if (!q.trim().isEmpty()) {
                            statement.execute(q);
                        }
                    }

                    return;
                }

                case 1045: {
                    throw new RuntimeException("Username atau password database salah.", e);
                }

                default: {
                    throw e;
                }
            }
        }

        statement = connection.createStatement();
    }

    public static ResultSet query(String query) {
        try {
             return statement.executeQuery(query);
        } catch (SQLException e) {
            System.out.println(query);
            e.printStackTrace();

            error(e.getMessage());

            throw new RuntimeException(e);
        }
    }

    public static void execute(String query) {
        try {
            statement.execute(query);
        } catch (SQLException e) {
            System.out.println(query);
            e.printStackTrace();

            error(e.getMessage());
        }
    }

    public static void error(String text) {
        JOptionPane.showMessageDialog(null, text, "Kesalahan Database", JOptionPane.ERROR_MESSAGE);
    }

    private static ArrayList<String> schema() {
        ArrayList<String> schema = new ArrayList<>();

        schema.add("DROP TABLE IF EXISTS `catatan`");
        schema.add("DROP TABLE IF EXISTS `pengguna`");

        schema.add("CREATE TABLE `pengguna` (\n" +
                "    `id` INT UNSIGNED NOT NULL AUTO_INCREMENT,\n" +
                "    `nama` VARCHAR(255) NOT NULL,\n" +
                "    `password` VARCHAR(255) NOT NULL,\n" +
                "    `target_mulai` TIME NOT NULL,\n" +
                "    `target_selesai` TIME NOT NULL,\n" +
                "    `target_produktifitas` TIME NOT NULL,\n" +
                "    PRIMARY KEY (`id`),\n" +
                "    UNIQUE (`nama`)\n" +
                ")");

        schema.add("CREATE TABLE `catatan` (\n" +
                "    `id` INT UNSIGNED NOT NULL AUTO_INCREMENT,\n" +
                "    `id_pengguna` INT UNSIGNED NOT NULL,\n" +
                "    `target_mulai` TIME NOT NULL,\n" +
                "    `target_selesai` TIME NOT NULL,\n" +
                "    `mulai` TIME NOT NULL,\n" +
                "    `selesai` TIME NOT NULL,\n" +
                "    `target_produktifitas` TIME NOT NULL,\n" +
                "    `tingkat_keproduktifan` DOUBLE,\n" +
                "    `tanggal` DATE NOT NULL,\n" +
                "    PRIMARY KEY (`id`),\n" +
                "    FOREIGN KEY (`id_pengguna`) REFERENCES `pengguna`(`id`)\n" +
                ")");

        schema.add("INSERT INTO `pengguna` (`nama`, `password`, `target_mulai`, `target_selesai`, `target_produktifitas`) VALUES ('nathan', '123', '08:30:00', '16:30:00', '07:00:00')");

        schema.add("INSERT INTO `catatan` (`id_pengguna`, `target_mulai`, `target_selesai`, `mulai`, `selesai`, `target_produktifitas`, `tingkat_keproduktifan`, `tanggal`) VALUES\n" +
                "(1, '08:00:00', '16:00:00', '08:12:00', '16:07:00', '07:00:00', 87.50911553286801, '2024-05-01'),\n" +
                "(1, '08:00:00', '16:00:00', '08:04:00', '15:57:00', '07:00:00', 99.87396856347942, '2024-05-02'),\n" +
                "(1, '08:00:00', '16:00:00', '07:53:00', '16:13:00', '07:00:00', 77.83543365455893, '2024-05-03'),\n" +
                "(1, '08:00:00', '16:00:00', '08:01:00', '16:02:00', '07:00:00', 87.49817712130998, '2024-05-04'),\n" +
                "(1, '08:00:00', '16:00:00', '08:17:00', '16:14:00', '07:00:00', 87.50546909181823, '2024-05-05'),\n" +
                "(1, '08:30:00', '16:30:00', '08:23:00', '16:33:00', '06:30:00', 75.04686523640908, '2024-05-06'),\n" +
                "(1, '08:30:00', '16:30:00', '08:29:00', '16:29:00', '06:30:00', 75.0625, '2024-05-07'),\n" +
                "(1, '08:30:00', '16:30:00', '08:34:00', '16:38:00', '06:30:00', 75.0562453128906, '2024-05-08'),\n" +
                "(1, '08:30:00', '16:30:00', '08:26:00', '16:27:00', '06:30:00', 75.0609362304952, '2024-05-09'),\n" +
                "(1, '08:30:00', '16:30:00', '08:37:00', '16:34:00', '06:30:00', 75.06719169948121, '2024-05-10'),\n" +
                "(1, '09:00:00', '17:00:00', '09:00:00', '17:01:00', '07:30:00', 87.56067581925376, '2024-05-11'),\n" +
                "(1, '09:00:00', '17:00:00', '09:13:00', '17:14:00', '07:30:00', 87.56067581925376, '2024-05-12'),\n" +
                "(1, '09:00:00', '17:00:00', '08:55:00', '16:56:00', '07:30:00', 87.56067581925376, '2024-05-13'),\n" +
                "(1, '09:00:00', '17:00:00', '09:08:00', '17:11:00', '07:30:00', 87.55702768576964, '2024-05-14'),\n" +
                "(1, '09:00:00', '17:00:00', '09:15:00', '17:09:00', '07:30:00', 87.5734466808351, '2024-05-15'),\n" +
                "(1, '08:00:00', '16:00:00', '07:49:00', '16:06:00', '06:45:00', 66.80319513686824, '2024-05-16'),\n" +
                "(1, '08:00:00', '16:00:00', '08:03:00', '16:02:00', '06:45:00', 75.09531448571846, '2024-05-17'),\n" +
                "(1, '08:00:00', '16:00:00', '08:02:00', '16:04:00', '06:45:00', 75.09062122411567, '2024-05-18'),\n" +
                "(1, '08:00:00', '16:00:00', '08:11:00', '16:14:00', '06:45:00', 75.08905693394163, '2024-05-19'),\n" +
                "(1, '08:00:00', '16:00:00', '07:56:00', '15:54:00', '06:45:00', 75.09687903662653, '2024-05-20'),\n" +
                "(1, '08:00:00', '16:00:00', '08:22:00', '16:17:00', '06:45:00', 75.10157308052922, '2024-05-21'),\n" +
                "(1, '08:00:00', '16:00:00', '08:10:00', '16:07:00', '06:45:00', 75.09844365272829, '2024-05-22'),\n" +
                "(1, '08:00:00', '16:00:00', '08:01:00', '16:00:00', '06:45:00', 75.09531448571846, '2024-05-23'),\n" +
                "(1, '08:00:00', '16:00:00', '08:15:00', '16:11:00', '06:45:00', 75.10000833402783, '2024-05-24'),\n" +
                "(1, '08:00:00', '16:00:00', '08:05:00', '16:04:00', '06:45:00', 75.09531448571846, '2024-05-25'),\n" +
                "(1, '08:00:00', '16:00:00', '07:51:00', '15:50:00', '06:45:00', 75.09531448571846, '2024-05-26'),\n" +
                "(1, '08:00:00', '16:00:00', '08:20:00', '16:21:00', '06:45:00', 75.0921855794671, '2024-05-27'),\n" +
                "(1, '08:00:00', '16:00:00', '08:13:00', '16:14:00', '06:45:00', 75.0921855794671, '2024-05-28'),\n" +
                "(1, '08:00:00', '16:00:00', '08:00:00', '15:59:00', '06:45:00', 85.7010390166195, '2024-05-29'),\n" +
                "(1, '08:00:00', '16:00:00', '07:56:00', '15:54:00', '06:45:00', 75.09687903662653, '2024-05-30'),\n" +
                "(1, '08:00:00', '16:00:00', '08:05:00', '16:04:00', '06:45:00', 75.09531448571846, '2024-05-31'),\n" +
                "(1, '08:00:00', '16:00:00', '08:02:00', '16:04:00', '06:45:00', 75.09062122411567, '2024-06-01'),\n" +
                "(1, '08:00:00', '16:00:00', '07:56:00', '15:54:00', '06:45:00', 75.09687903662653, '2024-05-02'),\n" +
                "(1, '08:00:00', '16:00:00', '08:22:00', '16:17:00', '06:45:00', 75.10157308052922, '2024-05-03'),\n" +
                "(1, '08:00:00', '16:00:00', '08:01:00', '16:02:00', '07:00:00', 87.49817712130998, '2024-05-04'),\n" +
                "(1, '08:00:00', '16:00:00', '08:17:00', '16:14:00', '07:00:00', 87.50546909181823, '2024-05-05'),\n" +
                "(1, '08:30:00', '16:30:00', '08:23:00', '16:33:00', '07:00:00', 87.48177463028536, '2024-05-06')");

        return schema;
    }
}
