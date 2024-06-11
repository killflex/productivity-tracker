package tugaspbo;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;

public abstract class Database {
    private static Connection connection;
    private static Statement statement;

    public static void connect(
            String host,
            String port,
            String username,
            String password,
            String database
    ) throws SQLException, IOException {
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

                    String[] sql = Files.readString(Paths.get("src/database.sql")).split(";");

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

            throw new RuntimeException(e);
        }
    }

    public static void execute(String query) {
        try {
            statement.execute(query);
        } catch (SQLException e) {
            System.out.println(query);
            e.printStackTrace();
        }
    }
}
