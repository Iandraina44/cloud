package model.crypto;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PortefeuilleCrypto {
    private int idPortefeuille;
    private double valeurPortefeuille;
    private Timestamp datePortefeuille;
    private int idUtilisateur;
    private int idCryptomonaie;

    // Default constructor
    public PortefeuilleCrypto() {}

    // Constructor with parameters
    public PortefeuilleCrypto(int idPortefeuille, double valeurPortefeuille, Timestamp datePortefeuille,
                              int idUtilisateur, int idCryptomonaie) {
        this.idPortefeuille = idPortefeuille;
        this.valeurPortefeuille = valeurPortefeuille;
        this.datePortefeuille = datePortefeuille;
        this.idUtilisateur = idUtilisateur;
        this.idCryptomonaie = idCryptomonaie;
    }

    // Getters and Setters
    public int getIdPortefeuille() {
        return idPortefeuille;
    }

    public void setIdPortefeuille(int idPortefeuille) {
        this.idPortefeuille = idPortefeuille;
    }

    public double getValeurPortefeuille() {
        return valeurPortefeuille;
    }

    public void setValeurPortefeuille(double valeurPortefeuille) {
        this.valeurPortefeuille = valeurPortefeuille;
    }

    public Timestamp getDatePortefeuille() {
        return datePortefeuille;
    }

    public void setDatePortefeuille(Timestamp datePortefeuille) {
        this.datePortefeuille = datePortefeuille;
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

    // CRUD Operations

    // Create (Insert a new portefeuille_crypto record)
    public void create(Connection connection) {
        String sql = "INSERT INTO portefeuille_crypto (valeur_portefeuille, date_portefeuille, id_utilisateur, id_cryptomonaie) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setDouble(1, this.valeurPortefeuille);
            stmt.setTimestamp(2, this.datePortefeuille);
            stmt.setInt(3, this.idUtilisateur);
            stmt.setInt(4, this.idCryptomonaie);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error during insertion: " + e.getMessage());
        }
    }

    // Read (Retrieve a portefeuille_crypto record by ID)
    public static PortefeuilleCrypto read(Connection connection, int idPortefeuille) {
        String sql = "SELECT * FROM portefeuille_crypto WHERE id_portefeuille = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idPortefeuille);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new PortefeuilleCrypto(
                            rs.getInt("id_portefeuille"),
                            rs.getDouble("valeur_portefeuille"),
                            rs.getTimestamp("date_portefeuille"),
                            rs.getInt("id_utilisateur"),
                            rs.getInt("id_cryptomonaie")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving portefeuille_crypto record: " + e.getMessage());
        }
        return null;
    }

    // Read (Retrieve all portefeuille_crypto records by utilisateur ID)
    public static List<PortefeuilleCrypto> getByIdUtilisateur(Connection connection, int idUtilisateur) {
        List<PortefeuilleCrypto> portefeuilles = new ArrayList<>();
        String sql = "SELECT * FROM portefeuille_crypto WHERE id_utilisateur = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idUtilisateur);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    PortefeuilleCrypto portefeuille = new PortefeuilleCrypto(
                            rs.getInt("id_portefeuille"),
                            rs.getDouble("valeur_portefeuille"),
                            rs.getTimestamp("date_portefeuille"),
                            rs.getInt("id_utilisateur"),
                            rs.getInt("id_cryptomonaie")
                    );
                    portefeuilles.add(portefeuille);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving portefeuille_crypto records by utilisateur ID: " + e.getMessage());
        }
        return portefeuilles;
    }

    // Update (Update a portefeuille_crypto record)
    public void update(Connection connection) {
        String sql = "UPDATE portefeuille_crypto SET valeur_portefeuille = ?, date_portefeuille = ?, id_utilisateur = ?, id_cryptomonaie = ? WHERE id_portefeuille = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setDouble(1, this.valeurPortefeuille);
            stmt.setTimestamp(2, this.datePortefeuille);
            stmt.setInt(3, this.idUtilisateur);
            stmt.setInt(4, this.idCryptomonaie);
            stmt.setInt(5, this.idPortefeuille);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error during update: " + e.getMessage());
        }
    }

    // Delete (Delete a portefeuille_crypto record by ID)
    public static void delete(Connection connection, int idPortefeuille) {
        String sql = "DELETE FROM portefeuille_crypto WHERE id_portefeuille = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idPortefeuille);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error during deletion: " + e.getMessage());
        }
    }
}
