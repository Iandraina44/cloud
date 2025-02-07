package model.transaction;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Fond {
    private int idFond;
    private double valeurFond;
    private Timestamp dateFond;
    private int idUtilisateur;
    private int etat;

    // Default constructor
    public Fond() {}

    // Constructor with parameters
    public Fond(int idFond, double valeurFond, Timestamp dateFond, int idUtilisateur) {
        this.idFond = idFond;
        this.valeurFond = valeurFond;
        this.dateFond = dateFond;
        this.idUtilisateur = idUtilisateur;
    }

    public Fond(int idFond, double valeurFond, Timestamp dateFond, int idUtilisateur,int etat) {
        this.idFond = idFond;
        this.valeurFond = valeurFond;
        this.dateFond = dateFond;
        this.idUtilisateur = idUtilisateur;
        this.etat = etat;
    }

    public Fond(double valeurFond, Timestamp dateFond, int idUtilisateur) {
        this.valeurFond = valeurFond;
        this.dateFond = dateFond;
        this.idUtilisateur = idUtilisateur;
    }

    // Getters and Setters

    public int getEtat() {
        return etat;
    }

    public void setEtat(int etat) {
        this.etat = etat;
    }
    public int getIdFond() {
        return idFond;
    }

    public void setIdFond(int idFond) {
        this.idFond = idFond;
    }

    public double getValeurFond() {
        return valeurFond;
    }

    public void setValeurFond(double valeurFond) {
        this.valeurFond = valeurFond;
    }

    public Timestamp getDateFond() {
        return dateFond;
    }

    public void setDateFond(Timestamp dateFond) {
        this.dateFond = dateFond;
    }

    public int getIdUtilisateur() {
        return idUtilisateur;
    }

    public void setIdUtilisateur(int idUtilisateur) {
        this.idUtilisateur = idUtilisateur;
    }

    // CRUD Operations

    // Create (Insert a new fond record)
    public void create(Connection connection) {
        String sql = "INSERT INTO fond (valeur_fond, date_fond, id_utilisateur,etat) VALUES (?, ?, ?, 0)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setDouble(1, this.valeurFond);
            stmt.setTimestamp(2, this.dateFond);
            stmt.setInt(3, this.idUtilisateur);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error during insertion: " + e.getMessage());
        }
    }


    public void createvalide(Connection connection) {
        String sql = "INSERT INTO fond (valeur_fond, date_fond, id_utilisateur,etat) VALUES (?, ?, ?, 1)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setDouble(1, this.valeurFond);
            stmt.setTimestamp(2, this.dateFond);
            stmt.setInt(3, this.idUtilisateur);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error during insertion: " + e.getMessage());
        }
    }

    // Read (Retrieve a fond record by ID)
    public static Fond read(Connection connection, int idFond) {
        String sql = "SELECT * FROM fond WHERE id_fond = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idFond);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Fond(
                            rs.getInt("id_fond"),
                            rs.getDouble("valeur_fond"),
                            rs.getTimestamp("date_fond"),
                            rs.getInt("id_utilisateur"),
                            rs.getInt("etat")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving fond record: " + e.getMessage());
        }
        return null;
    }

    public static double totalfond(Connection connection, int iduser) {
        String sql = "SELECT sum(valeur_fond) FROM fond WHERE id_utilisateur = ? and etat=1";
        double total=0;
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, iduser);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    total=rs.getDouble("sum(valeur_fond)");
                }
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving fond record: " + e.getMessage());
        }
        return total;
    }

    public static List<Fond> getByIdUtilisateur(Connection connection, int idUtilisateur) {
        List<Fond> fondsList = new ArrayList<>();
        String sql = "SELECT * FROM fond WHERE id_utilisateur = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idUtilisateur);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {  // 🔹 Boucle pour récupérer tous les fonds
                    Fond fond = new Fond();
                    fond.setDateFond(rs.getTimestamp("date_fond"));
                    fond.setIdFond(rs.getInt("id_fond"));
                    fond.setValeurFond(rs.getDouble("valeur_fond"));
                    fond.setIdUtilisateur(rs.getInt("id_utilisateur"));
                    fond.setEtat(rs.getInt("etat"));
                    
                    fondsList.add(fond); // 🔹 Ajouter chaque fond à la liste
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des fonds par ID utilisateur: " + e.getMessage());
        }
        
        return fondsList;  // 🔹 Retourner la liste complète des fonds
    }

    public static List<Fond> getAllForAdmin(Connection connection) {
        List<Fond> fondsList = new ArrayList<>();
        String sql = "SELECT * FROM fond WHERE etat != 1";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {  // 🔹 Boucle pour récupérer tous les fonds
                    Fond fond = new Fond();
                    fond.setDateFond(rs.getTimestamp("date_fond"));
                    fond.setIdFond(rs.getInt("id_fond"));
                    fond.setValeurFond(rs.getDouble("valeur_fond"));
                    fond.setIdUtilisateur(rs.getInt("id_utilisateur"));
                    fond.setEtat(rs.getInt("etat"));
                    
                    fondsList.add(fond); // 🔹 Ajouter chaque fond à la liste
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des fonds par ID utilisateur: " + e.getMessage());
        }
        
        return fondsList;  // 🔹 Retourner la liste complète des fonds
    }
    
    

    // Update (Update a fond record)
    public void update(Connection connection) {
        String sql = "UPDATE fond SET valeur_fond = ?, date_fond = ?, id_utilisateur = ? WHERE id_fond = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setDouble(1, this.valeurFond);
            stmt.setTimestamp(2, this.dateFond);
            stmt.setInt(3, this.idUtilisateur);
            stmt.setInt(4, this.idFond);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error during update: " + e.getMessage());
        }
    }

    public static void updateEtat(Connection connection, int idfond, int nouvelEtat) {
        String sql = "UPDATE fond SET etat = ? WHERE id_fond = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, nouvelEtat);
            stmt.setInt(2, idfond);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error during updateEtat: " + e.getMessage());
        }
    }
    

    // Delete (Delete a fond record by ID)
    public static void delete(Connection connection, int idFond) {
        String sql = "DELETE FROM fond WHERE id_fond = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idFond);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error during deletion: " + e.getMessage());
        }
    }
}
