package tugaspbo;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

public class Pengguna {
    private int id;
    private String nama;
    private String password;
    private LocalTime targetMulai;
    private LocalTime targetSelesai;
    private LocalTime targetProduktifitas;
    private ArrayList<Catatan> catatan = null;
    private Catatan catatanSekarang = null;

    public Pengguna(
            int id,
            String nama,
            String password,
            LocalTime targetMulai,
            LocalTime targetSelesai,
            LocalTime targetProduktifitas
    ) {
        this.id = id;
        this.nama = nama;
        this.password = password;
        this.targetMulai = targetMulai;
        this.targetSelesai = targetSelesai;
        this.targetProduktifitas = targetProduktifitas;
    }

    protected static Pengguna fromResultSet(ResultSet result) {
        try {
            return new Pengguna(
                    result.getInt("id"),
                    result.getString("nama"),
                    result.getString("password"),
                    result.getTime("target_mulai").toLocalTime(),
                    result.getTime("target_selesai").toLocalTime(),
                    result.getTime("target_produktifitas").toLocalTime()
            );
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static Pengguna baru(String nama, String password) {
        Database.execute(
                "INSERT INTO pengguna (nama, password, target_mulai, target_selesai, target_produktifitas) VALUES (" +
                        "'" + nama + "', " +
                        "'" + password + "', " +
                        "'00:00:00', " +
                        "'00:00:00', " +
                        "'00:00:00')"
        );

        return cari(nama);
    }

    public static Pengguna cari(String nama) {
        ResultSet resultSet = Database.query("SELECT * FROM pengguna WHERE nama = '" + nama + "'");

        try {
            if (resultSet.next()) {
                return fromResultSet(resultSet);
            }
        } catch (Exception e) {
            //
        }

        return null;
    }

    public Catatan catatanSekarang() {
        if (catatanSekarang == null) {
            catatanSekarang = Catatan.cariAktif(this, LocalDate.now());
        }

        return catatanSekarang;
    }

    public void hapusCacheCatatanSekarang() {
        catatanSekarang = null;
    }

    public void updateTargetMulai(int jam, int menit) {
        Database.execute("UPDATE pengguna SET target_mulai = '" + jam + ":" + menit + ":00' WHERE id = " + id);
    }

    public void updateTargetSelesai(int jam, int menit) {
        Database.execute("UPDATE pengguna SET target_selesai = '" + jam + ":" + menit + ":00' WHERE id = " + id);
    }

    public void updateTargetProduktifitas(int jam, int menit) {
        Database.execute("UPDATE pengguna SET target_produktifitas = '" + jam + ":" + menit + ":00' WHERE id = " + id);
    }

    public int getId() {
        return id;
    }

    public String getNama() {
        return nama;
    }

    public Pengguna setNama(String nama) {
        this.nama = nama;

        return this;
    }

    public String getPassword() {
        return password;
    }

    public Pengguna setPassword(String password) {
        this.password = password;

        return this;
    }

    public LocalTime getTargetMulai() {
        return targetMulai;
    }

    public Pengguna setTargetMulai(LocalTime targetMulai) {
        this.targetMulai = targetMulai;

        return this;
    }

    public LocalTime getTargetSelesai() {
        return targetSelesai;
    }

    public Pengguna setTargetSelesai(LocalTime targetSelesai) {
        this.targetSelesai = targetSelesai;

        return this;
    }

    public LocalTime getTargetProduktifitas() {
        return targetProduktifitas;
    }

    public Pengguna setTargetProduktifitas(LocalTime targetProduktifitas) {
        this.targetProduktifitas = targetProduktifitas;

        return this;
    }
}
