package model.role;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;


import connexion.ConnexionMySQL;

public class Utilisateur {
    private int idUtilisateur;
    private String email;
    private String mdp;
    private String image;


    // Constructeurs
    public Utilisateur() {}

    public Utilisateur(int idUtilisateur, String email, String mdp) {
        this.idUtilisateur = idUtilisateur;
        this.email = email;
        this.mdp = mdp;
    }

    public Utilisateur(int idUtilisateur, String email, String mdp,String image) {
        this.idUtilisateur = idUtilisateur;
        this.email = email;
        this.mdp = mdp;
        this.image = image;

    }

    // Getters et Setters
    public int getIdUtilisateur() {
        return idUtilisateur;
    }

    public void setIdUtilisateur(int idUtilisateur) {
        this.idUtilisateur = idUtilisateur;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMdp() {
        return mdp;
    }

    public void setMdp(String mdp) {
        this.mdp = mdp;
    }

    
    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    // Méthode pour créer un utilisateur
    public void create() {
        String query = "INSERT INTO utilisateur (id_utilisateur, email, mdp) VALUES (?, ?, ?)";
        try (Connection connexion = new ConnexionMySQL().getConnectionMySQL();
             PreparedStatement ps = connexion.prepareStatement(query)) {
            ps.setInt(1, this.idUtilisateur);
            ps.setString(2, this.email);
            ps.setString(3, this.mdp);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Méthode pour lire un utilisateur par son ID
    public static Utilisateur read(int idUtilisateur) {
        String query = "SELECT * FROM utilisateur WHERE id_utilisateur = ?";
        Utilisateur utilisateur = null;
        try (Connection connexion = new ConnexionMySQL().getConnectionMySQL();
             PreparedStatement ps = connexion.prepareStatement(query)) {
            ps.setInt(1, idUtilisateur);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                utilisateur = new Utilisateur(
                    rs.getInt("id_utilisateur"),
                    rs.getString("email"),
                    rs.getString("mdp")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return utilisateur;
    }

    // Méthode pour mettre à jour un utilisateur
    public void update() {
        String query = "UPDATE utilisateur SET email = ?, mdp = ? WHERE id_utilisateur = ?";
        try (Connection connexion = new ConnexionMySQL().getConnectionMySQL();
             PreparedStatement ps = connexion.prepareStatement(query)) {
            ps.setString(1, this.email);
            ps.setString(2, this.mdp);
            ps.setInt(3, this.idUtilisateur);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



public static List<Utilisateur> readAll() {
    List<Utilisateur> utilisateurs = new ArrayList<>();
    String query = "SELECT * FROM utilisateur";

    try (Connection connexion = new ConnexionMySQL().getConnectionMySQL();
         PreparedStatement ps = connexion.prepareStatement(query);
         ResultSet rs = ps.executeQuery()) {

        while (rs.next()) {
            Utilisateur utilisateur = new Utilisateur(
                rs.getInt("id_utilisateur"),
                rs.getString("email"),
                rs.getString("mdp"),
                rs.getString("url") // Ajout de l'image
            );
            utilisateurs.add(utilisateur);
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }

    return utilisateurs;
}


    // Méthode pour supprimer un utilisateur par son ID
    public void delete() {
        String query = "DELETE FROM utilisateur WHERE id_utilisateur = ?";
        try (Connection connexion = new ConnexionMySQL().getConnectionMySQL();
             PreparedStatement ps = connexion.prepareStatement(query)) {
            ps.setInt(1, this.idUtilisateur);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    public static Utilisateur getById(int idUtilisateur) {
        String query = "SELECT * FROM utilisateur WHERE id_utilisateur = ?";
        Utilisateur utilisateur = null;
    
        try (Connection connexion = new ConnexionMySQL().getConnectionMySQL();
             PreparedStatement ps = connexion.prepareStatement(query)) {
            ps.setInt(1, idUtilisateur);
            ResultSet rs = ps.executeQuery();
    
            if (rs.next()) {
                utilisateur = new Utilisateur(
                    rs.getInt("id_utilisateur"),
                    rs.getString("email"),
                    rs.getString("mdp"),
                    rs.getString("url") // Ajout de l'image
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return utilisateur;
    }
    
}