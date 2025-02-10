import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class PorteFeuille {

    private int idPortefeuille;
    private LocalDateTime datePortefeuille;
    private Double valeurPortefeuille;
    private int idUtilisateur;
    private int idCryptomonaie;

    // Getters and Setters
    public int getIdPortefeuille() {
        return idPortefeuille;
    }

    public void setIdPortefeuille(int idPortefeuille) {
        this.idPortefeuille = idPortefeuille;
    }

    public LocalDateTime getDatePortefeuille() {
        return datePortefeuille;
    }

    public void setDatePortefeuille(LocalDateTime datePortefeuille) {
        this.datePortefeuille = datePortefeuille;
    }

    public Double getValeurPortefeuille() {
        return valeurPortefeuille;
    }

    public void setValeurPortefeuille(Double valeurPortefeuille) {
        this.valeurPortefeuille = valeurPortefeuille;
    }

    public int getIdUtilisateur() {
        return idUtilisateur;
    }

    public void setIdUtilisateur(int idUtilisateur) {
        this.idUtilisateur = idUtilisateur;
    }

    public int getIdCryptomonaie() {
        return idCryptomonaie;
    }

    public void setIdCryptomonaie(int idCryptomonaie) {
        this.idCryptomonaie = idCryptomonaie;
    }

    // CRUD operations
    public void createPorteFeuille(Connection connection) throws SQLException {
        String query = "INSERT INTO portefeuille_crypto (date_portefeuille, valeur_portefeuille, id_utilisateur, id_cryptomonaie) VALUES (?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setTimestamp(1, Timestamp.valueOf(datePortefeuille));
            statement.setDouble(2, valeurPortefeuille);
            statement.setInt(3, idUtilisateur);
            statement.setInt(4, idCryptomonaie);
            statement.executeUpdate();
        }
    }

    public static PorteFeuille readPorteFeuille(Connection connection, int idPortefeuille) throws SQLException {
        String query = "SELECT * FROM portefeuille_crypto WHERE id_portefeuille = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, idPortefeuille);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                PorteFeuille portefeuille = new PorteFeuille();
                portefeuille.setIdPortefeuille(resultSet.getInt("id_portefeuille"));
                portefeuille.setDatePortefeuille(resultSet.getTimestamp("date_portefeuille").toLocalDateTime());
                portefeuille.setValeurPortefeuille(resultSet.getDouble("valeur_portefeuille"));
                portefeuille.setIdUtilisateur(resultSet.getInt("id_utilisateur"));
                portefeuille.setIdCryptomonaie(resultSet.getInt("id_cryptomonaie"));
                return portefeuille;
            } else {
                return null;
            }
        }
    }

    public void updatePorteFeuille(Connection connection) throws SQLException {
        String query = "UPDATE portefeuille_crypto SET date_portefeuille = ?, valeur_portefeuille = ?, id_utilisateur = ?, id_cryptomonaie = ? WHERE id_portefeuille = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setTimestamp(1, Timestamp.valueOf(datePortefeuille));
            statement.setDouble(2, valeurPortefeuille);
            statement.setInt(3, idUtilisateur);
            statement.setInt(4, idCryptomonaie);
            statement.setInt(5, idPortefeuille);
            statement.executeUpdate();
        }
    }

    public static void deletePorteFeuille(Connection connection, int idPortefeuille) throws SQLException {
        String query = "DELETE FROM portefeuille_crypto WHERE id_portefeuille = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, idPortefeuille);
            statement.executeUpdate();
        }
    }
    public static Double calculateFondByCrypto(Connection connection, int idUtilisateur, int idCryptomonaie) throws SQLException {
        String query = "SELECT SUM(p.valeur_portefeuille * c.valeur_cour) AS total_fond " +
                       "FROM portefeuille_crypto p " +
                       "JOIN cours_crypto c ON p.id_cryptomonaie = c.id_cryptomonaie " +
                       "WHERE p.id_utilisateur = ? AND p.id_cryptomonaie = ?";
        Double totalFond = 0.0;
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, idUtilisateur);
            statement.setInt(2, idCryptomonaie);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                totalFond = resultSet.getDouble("total_fond");
            }
        }
        return totalFond;
    }
    public static Double calculateTotalFond(Connection connection, int idUtilisateur) throws SQLException {
        String query = "SELECT SUM(p.valeur_portefeuille * c.valeur_cour) AS total_fond " +
                       "FROM portefeuille_crypto p " +
                       "JOIN cours_crypto c ON p.id_cryptomonaie = c.id_cryptomonaie " +
                       "WHERE p.id_utilisateur = ?";
        Double totalFond = 0.0;
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, idUtilisateur);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                totalFond = resultSet.getDouble("total_fond");
            }
        }
        return totalFond;
    }

    public static void insertFond(Connection connection, int idUtilisateur, Double totalFond) throws SQLException {
        String insertFondQuery = "INSERT INTO fond (valeur_fond, date_fond, id_utilisateur) VALUES (?, ?, ?)";
        try (PreparedStatement insertStatement = connection.prepareStatement(insertFondQuery)) {
            insertStatement.setDouble(1, totalFond);
            insertStatement.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
            insertStatement.setInt(3, idUtilisateur);
            insertStatement.executeUpdate();
        }
    }

    public static void convertCryptoToFond(Connection connection, int idUtilisateur) throws SQLException {
        Double totalFond = calculateTotalFond(connection, idUtilisateur);
        insertFond(connection, idUtilisateur, totalFond);
    }

}
