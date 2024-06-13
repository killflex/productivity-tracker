package tugaspbo;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

public class Catatan {
    private int id;
    private LocalTime targetMulai;
    private LocalTime targetSelesai;
    private LocalTime mulai;
    private LocalTime selesai;
    private LocalTime targetProduktifitas;
    private double tingkatKeproduktifan;
    private LocalDate tanggal;

    public Catatan(
            int id,
            LocalTime targetMulai,
            LocalTime targetSelesai,
            LocalTime mulai,
            LocalTime selesai,
            LocalTime targetProduktifitas,
            double tingkatKeproduktifan,
            LocalDate tanggal
    ) {
        this.id = id;
        this.targetMulai = targetMulai;
        this.targetSelesai = targetSelesai;
        this.mulai = mulai;
        this.selesai = selesai;
        this.targetProduktifitas = targetProduktifitas;
        this.tingkatKeproduktifan = tingkatKeproduktifan;
        this.tanggal = tanggal;
    }

    protected static Catatan fromResultSet(ResultSet result) {
        try {
            return new Catatan(
                    result.getInt("id"),
                    result.getTime("target_mulai").toLocalTime(),
                    result.getTime("target_selesai").toLocalTime(),
                    result.getTime("mulai").toLocalTime(),
                    result.getTime("selesai").toLocalTime(),
                    result.getTime("target_produktifitas").toLocalTime(),
                    result.getDouble("tingkat_keproduktifan"),
                    result.getDate("tanggal").toLocalDate()
            );
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void baru(
            Pengguna pengguna,
            LocalTime targetMulai,
            LocalTime targetSelesai,
            LocalTime mulai,
            LocalTime selesai,
            LocalTime targetProduktifitas,
            double tingkatKeproduktifan,
            LocalDate tanggal
    ) {
        Database.execute(
                "INSERT INTO catatan (id_pengguna, target_mulai, target_selesai, mulai, selesai, target_produktifitas, tingkat_keproduktifan, tanggal) VALUES (" +
                pengguna.getId() + ", " +
                "'" + targetMulai + "', " +
                "'" + targetSelesai + "', " +
                "'" + mulai + "', " +
                "'" + selesai + "', " +
                "'" + targetProduktifitas + "', " +
                (tingkatKeproduktifan == 0 ? "null" : tingkatKeproduktifan) + ", " +
                "'" + Date.valueOf(tanggal) + "')"
        );
    }

    public static ArrayList<Catatan> cariAktif(Pengguna pengguna) {
        ResultSet resultSet = Database.query("SELECT * FROM catatan WHERE id_pengguna = " + pengguna.getId());
        ArrayList<Catatan> catatan = new ArrayList<>();

        try {
            while (resultSet.next()) {
                catatan.add(fromResultSet(resultSet));
            }
        } catch (SQLException e) {
            //
        }

        return catatan;
    }

    public static Catatan cariAktif(Pengguna pengguna, LocalDate tanggal) {
        String tgl = tanggal.getYear() + "-" + tanggal.getMonthValue() + "-" + tanggal.getDayOfMonth();

        ResultSet resultSet = Database.query(
                "SELECT * FROM catatan WHERE id_pengguna = " + pengguna.getId() +
                " AND tanggal = '" + tgl + "' AND tingkat_keproduktifan IS NULL ORDER BY tanggal DESC LIMIT 1"
        );

        try {
            if (resultSet.next()) {
                return fromResultSet(resultSet);
            }
        } catch (Exception e) {
            //
        }

        return null;
    }

    public void update() {
        Database.execute(
                "UPDATE catatan SET " +
                "target_mulai = '" + targetMulai + "', " +
                "target_selesai = '" + targetSelesai + "', " +
                "mulai = '" + mulai + "', " +
                "selesai = '" + selesai + "', " +
                "target_produktifitas = '" + targetProduktifitas + "', " +
                "tingkat_keproduktifan = '" + (tingkatKeproduktifan == 0 ? "null" : tingkatKeproduktifan) + "'" +
                "WHERE id = " + id
        );
    }

    public int getId() {
        return id;
    }

    public LocalTime getTargetMulai() {
        return targetMulai;
    }

    public Catatan setTargetMulai(LocalTime targetMulai) {
        this.targetMulai = targetMulai;

        return this;
    }

    public LocalTime getTargetSelesai() {
        return targetSelesai;
    }

    public Catatan setTargetSelesai(LocalTime targetSelesai) {
        this.targetSelesai = targetSelesai;

        return this;
    }

    public LocalTime getMulai() {
        return mulai;
    }

    public Catatan setMulai(LocalTime mulai) {
        this.mulai = mulai;

        return this;
    }

    public LocalTime getSelesai() {
        return selesai;
    }

    public Catatan setSelesai(LocalTime selesai) {
        this.selesai = selesai;

        return this;
    }

    public LocalTime getTargetProduktifitas() {
        return targetProduktifitas;
    }

    public Catatan setTargetProduktifitas(LocalTime targetProduktifitas) {
        this.targetProduktifitas = targetProduktifitas;

        return this;
    }

    public double getTingkatKeproduktifan() {
        return tingkatKeproduktifan;
    }

    public Catatan setTingkatKeproduktifan(double tingkatKeproduktifan) {
        this.tingkatKeproduktifan = tingkatKeproduktifan;

        return this;
    }

    public LocalDate getTanggal() {
        return tanggal;
    }
}
