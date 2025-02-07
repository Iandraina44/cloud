package model.transaction;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Notification {
    private int idNotification;
    private Timestamp dateNotification;
    private String message;
    private int idUtilisateur;
    private int etat;

    // Constructeurs
    public Notification() {}

    public Notification(int idNotification, Timestamp dateNotification, String message, int idUtilisateur, int etat) {
        this.idNotification = idNotification;
        this.dateNotification = dateNotification;
        this.message = message;
        this.idUtilisateur = idUtilisateur;
        this.etat = etat;
    }

    public Notification(Timestamp dateNotification, String message, int idUtilisateur, int etat) {
        this.dateNotification = dateNotification;
        this.message = message;
        this.idUtilisateur = idUtilisateur;
        this.etat = etat;
    }

    // Getters et Setters
    public int getIdNotification() {
        return idNotification;
    }

    public void setIdNotification(int idNotification) {
        this.idNotification = idNotification;
    }

    public Timestamp getDateNotification() {
        return dateNotification;
    }

    public void setDateNotification(Timestamp dateNotification) {
        this.dateNotification = dateNotification;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getIdUtilisateur() {
        return idUtilisateur;
    }

    public void setIdUtilisateur(int idUtilisateur) {
        this.idUtilisateur = idUtilisateur;
    }

    public int getEtat() {
        return etat;
    }

    public void setEtat(int etat) {
        this.etat = etat;
    }

    // Méthodes CRUD

    // Création
    public void create(Connection connection) {
        String sql = "INSERT INTO notification (date_notification, message, id_utilisateur, etat) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setTimestamp(1, this.dateNotification);
            stmt.setString(2, this.message);
            stmt.setInt(3, this.idUtilisateur);
            stmt.setInt(4, this.etat);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'insertion : " + e.getMessage());
        }
    }

    // Lecture
    public static Notification read(Connection connection, int idNotification) {
        String sql = "SELECT * FROM notification WHERE id_notification = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idNotification);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Notification(
                        rs.getInt("id_notification"),
                        rs.getTimestamp("date_notification"),
                        rs.getString("message"),
                        rs.getInt("id_utilisateur"),
                        rs.getInt("etat")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération : " + e.getMessage());
        }
        return null;
    }

    // Mise à jour
    public void update(Connection connection) {
        String sql = "UPDATE notification SET date_notification = ?, message = ?, id_utilisateur = ?, etat = ? WHERE id_notification = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setTimestamp(1, this.dateNotification);
            stmt.setString(2, this.message);
            stmt.setInt(3, this.idUtilisateur);
            stmt.setInt(4, this.etat);
            stmt.setInt(5, this.idNotification);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Erreur lors de la mise à jour : " + e.getMessage());
        }
    }

    // Suppression
    public static void delete(Connection connection, int idNotification) {
        String sql = "DELETE FROM notification WHERE id_notification = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idNotification);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Erreur lors de la suppression : " + e.getMessage());
        }
    }

    // Récupérer toutes les notifications d'un utilisateur
    public static List<Notification> getByIdUtilisateur(Connection connection, int idUtilisateur) {
        List<Notification> notificationsList = new ArrayList<>();
        String sql = "SELECT * FROM notification WHERE id_utilisateur = ? and etat = 0";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idUtilisateur);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    notificationsList.add(new Notification(
                        rs.getInt("id_notification"),
                        rs.getTimestamp("date_notification"),
                        rs.getString("message"),
                        rs.getInt("id_utilisateur"),
                        rs.getInt("etat")
                    ));
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des notifications : " + e.getMessage());
        }
        return notificationsList;
    }

    public static List<Notification> getAll(Connection connection) {
        List<Notification> notificationsList = new ArrayList<>();
        String sql = "SELECT * FROM notification";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    notificationsList.add(new Notification(
                        rs.getInt("id_notification"),
                        rs.getTimestamp("date_notification"),
                        rs.getString("message"),
                        rs.getInt("id_utilisateur"),
                        rs.getInt("etat")
                    ));
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des notifications : " + e.getMessage());
        }
        return notificationsList;
    }
}
