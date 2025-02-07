package model.crypto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Cryptomonaie {
    private int id;
    private String nom;

    public Cryptomonaie(int id, String nom) {
        this.id = id;
        this.nom = nom;
    }


    public Cryptomonaie() {
   
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }


    public static Cryptomonaie getById(Connection connection, int id) {
        String sql = "SELECT * FROM cryptomonaie WHERE id_cryptomonaie = ?";
        Cryptomonaie cryptomonaie = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
    
        try {
            stmt = connection.prepareStatement(sql);
            stmt.setInt(1, id);
            rs = stmt.executeQuery();
    
            if (rs.next()) {
                cryptomonaie = new Cryptomonaie();
                cryptomonaie.setId(rs.getInt("id_cryptomonaie"));
                cryptomonaie.setNom(rs.getString("nom_cryptomonaie"));
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving cryptomonaie by ID: " + e.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
            } catch (SQLException e) {
                System.err.println("Error closing resources: " + e.getMessage());
            }
        }
        return cryptomonaie;
    }
    

    public static void create(Cryptomonaie cryptomonaie, Connection conn) throws SQLException {
        String sql = "INSERT INTO cryptomonaie (nom_cryptomonaie) VALUES (?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, cryptomonaie.getNom());
            pstmt.executeUpdate();
        }
    }

    public static Cryptomonaie read(int id, Connection conn) throws SQLException {
        String sql = "SELECT * FROM cryptomonaie WHERE id_cryptomonaie = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new Cryptomonaie(rs.getInt("id_cryptomonaie"), rs.getString("nom_cryptomonaie"));
            }
        }
        return null;
    }

    public static List<Cryptomonaie> readAll(Connection conn) throws SQLException {
        String sql = "SELECT * FROM cryptomonaie";
        List<Cryptomonaie> cryptomonaies = new ArrayList<>();
    
        try (PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
    
            while (rs.next()) {
                // Ajouter chaque cryptomonnaie à la liste
                cryptomonaies.add(new Cryptomonaie(
                    rs.getInt("id_cryptomonaie"), 
                    rs.getString("nom_cryptomonaie")
                ));
            }
        }
    
        return cryptomonaies; // Retourner la liste complète
    }


    public void update(Connection conn) throws SQLException {
        String sql = "UPDATE cryptomonaie SET nom_cryptomonaie = ? WHERE id_cryptomonaie = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, this.getNom());
            pstmt.setInt(2, this.getId());
            pstmt.executeUpdate();
        }
    }

    public void delete(Connection conn) throws SQLException {
        String sql = "DELETE FROM cryptomonaie WHERE id_cryptomonaie = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, this.getId());
            pstmt.executeUpdate();
        }
    }

    public static void update(Cryptomonaie cryptomonaie, Connection conn) throws SQLException {
        String sql = "UPDATE cryptomonaie SET nom_cryptomonaie = ? WHERE id_cryptomonaie = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, cryptomonaie.getNom());
            pstmt.setInt(2, cryptomonaie.getId());
            pstmt.executeUpdate();
        }
    }

    // Calculate total funds by cryptocurrency and user
    public static double calculateFondByCryptoAndUser(Connection conn, int idUtilisateur, int idCryptomonaie) throws SQLException {
        String sql = "SELECT SUM(valeur_portefeuille * valeur_cour) AS total_fond " +
                     "FROM portefeuille_crypto p " +
                     "JOIN cours_crypto c ON p.id_cryptomonaie = c.id_cryptomonaie " +
                     "WHERE p.id_utilisateur = ? AND p.id_cryptomonaie = ?";
        double totalFond = 0;
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idUtilisateur);
            pstmt.setInt(2, idCryptomonaie);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                totalFond = rs.getDouble("total_fond");
            }
        }
        return totalFond;
    }

    // Calculate total funds by user
    public static double calculateTotalFondByUser(Connection connection, int idUtilisateur) throws SQLException {
        String sql = """
            SELECT SUM(p.valeur_portefeuille * c.valeur_cour) AS total_fond
        FROM portefeuille_crypto p
        JOIN (
            SELECT id_cryptomonaie, MAX(date_cour) AS max_date
            FROM cours_crypto
            GROUP BY id_cryptomonaie
        ) latest ON p.id_cryptomonaie = latest.id_cryptomonaie
        JOIN cours_crypto c ON c.id_cryptomonaie = latest.id_cryptomonaie AND c.date_cour = latest.max_date
        WHERE p.id_utilisateur = ?
        """;
        double totalFond = 0;
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, idUtilisateur);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                totalFond = rs.getDouble("total_fond");
            }
        }
        return totalFond;
    }
    

    // Convert cryptocurrency to fund
    public static void transformCryptoToFond(Connection conn, int idUtilisateur, int idCryptomonaie, double amount) throws SQLException {
        String insertFondSql = "INSERT INTO fond (valeur_fond, date_fond, id_utilisateur) VALUES (?, NOW(), ?)";
        String updatePortefeuilleSql = "UPDATE portefeuille_crypto SET valeur_portefeuille = valeur_portefeuille - ? " +
                                       "WHERE id_utilisateur = ? AND id_cryptomonaie = ? AND valeur_portefeuille >= ?";
        try (PreparedStatement updateStmt = conn.prepareStatement(updatePortefeuilleSql); 
             PreparedStatement insertStmt = conn.prepareStatement(insertFondSql)) {
            conn.setAutoCommit(false);

            // Update portfolio
            updateStmt.setDouble(1, amount);
            updateStmt.setInt(2, idUtilisateur);
            updateStmt.setInt(3, idCryptomonaie);
            updateStmt.setDouble(4, amount);
            int updatedRows = updateStmt.executeUpdate();

            if (updatedRows == 0) {
                conn.rollback();
                throw new SQLException("Insufficient funds in the portfolio.");
            }

            // Insert into fund
            insertStmt.setDouble(1, amount);
            insertStmt.setInt(2, idUtilisateur);
            insertStmt.executeUpdate();

            conn.commit();
        } catch (SQLException e) {
            conn.rollback();
            throw e;
        } finally {
            conn.setAutoCommit(true);
        }
    }

    // Calculate sum of crypto grouped by ID
    public static ResultSet calculateSumCryptoGroupById(Connection conn) throws SQLException {
        String sql = "SELECT id_cryptomonaie, SUM(valeur_portefeuille) AS total_value " +
                     "FROM portefeuille_crypto GROUP BY id_cryptomonaie";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        return pstmt.executeQuery();
    }

    // Calculate total sum of funds
    public static double calculateTotalSumFond(Connection conn) throws SQLException {
        String sql = "SELECT SUM(valeur_fond) AS total_fond FROM fond";
        double totalFond = 0;
        try (PreparedStatement pstmt = conn.prepareStatement(sql); ResultSet rs = pstmt.executeQuery()) {
            if (rs.next()) {
                totalFond = rs.getDouble("total_fond");
            }
        }
        return totalFond;
    }
}
