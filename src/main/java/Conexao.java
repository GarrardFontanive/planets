import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

public class Conexao {

    private static final String URL = "jdbc:mysql://localhost:3306/planetas_db?createDatabaseIfNotExist=true";
    private static final String USER = "root";
    private static final String PASSWORD = "root";
    private static final String CREATE_TABLE_SQL = "CREATE TABLE IF NOT EXISTS planeta (" +
            "id INT AUTO_INCREMENT PRIMARY KEY, " +
            "nome VARCHAR(255), " +
            "orbital_period DOUBLE, " +
            "radius_earth DOUBLE, " +
            "mass_earth DOUBLE)";
    private static final String ADD_RADIUS_COLUMN_SQL = "ALTER TABLE planeta ADD COLUMN IF NOT EXISTS radius_earth DOUBLE";
    private static final String ADD_MASS_COLUMN_SQL = "ALTER TABLE planeta ADD COLUMN IF NOT EXISTS mass_earth DOUBLE";
    private static final String INSERT_SQL = "INSERT INTO planeta (nome, orbital_period, radius_earth, mass_earth) VALUES (?, ?, ?, ?)";

    public void salvarNoMySQL(List<Planet> planetas) throws Exception {
        try (Connection conexao = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement stmt = conexao.createStatement();
             PreparedStatement pstmt = conexao.prepareStatement(INSERT_SQL)) {
            conexao.setAutoCommit(false);
            prepararTabela(stmt);
            stmt.execute("TRUNCATE TABLE planeta");

            for (Planet planeta : planetas) {
                pstmt.setString(1, planeta.getName());
                pstmt.setDouble(2, planeta.getOrbitalPeriod());
                pstmt.setDouble(3, planeta.getRadius());
                pstmt.setDouble(4, planeta.getMass());
                pstmt.addBatch();
            }

            pstmt.executeBatch();
            conexao.commit();
        }
    }

    public void limparBaseDeDados() throws Exception {
        try (Connection conexao = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement stmt = conexao.createStatement()) {
            prepararTabela(stmt);
            stmt.execute("TRUNCATE TABLE planeta");
        }
    }

    private static void prepararTabela(Statement stmt) throws Exception {
        stmt.execute(CREATE_TABLE_SQL);
        stmt.execute(ADD_RADIUS_COLUMN_SQL);
        stmt.execute(ADD_MASS_COLUMN_SQL);
    }
}