package model;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Timestamp;

import connexion.ConnexionMySQL;

public class Fond {

    private int idFond;
    private double valeurFond;
    private Timestamp dateFond;
    private int idUtilisateur;
    private int actionStatus;


    // Constructeur
    public Fond(double valeurFond, Timestamp dateFond, int idUtilisateur,int actionStatus) {
        this.valeurFond = valeurFond;
        this.dateFond = dateFond;
        this.idUtilisateur = idUtilisateur;
        this.actionStatus = actionStatus;

    }


    // Getters et Setters (optionnels)
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


    public int getActionStatus() {
        return actionStatus;
    }

    public void setActionStatus(int actionStatus) {
        this.actionStatus = actionStatus;
    }


    
    // Méthode pour insérer un fond dans la base de données
    public void insererFond(Connection connection) {
        String sql = "INSERT INTO fond (valeur_fond, date_fond, id_utilisateur,action_status) VALUES (?, ?, ?,?)";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            if(valeurFond==0){ //depot
                statement.setDouble(1,valeurFond);

            }
            else{ //retrait
                statement.setDouble(1,-valeurFond);
            }

            // Remplissage des paramètres
            statement.setDouble(2, dateFond);
            statement.setInt(3, idUtilisateur);
            statement.setInt(4, actionStatus);

            statement.executeUpdate();
        
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static double calculerTotalFonds(Connection connection, int idUtilisateur) throws SQLException {
        String sql = "SELECT SUM(valeur_fond) AS total FROM fonds WHERE id_utilisateur = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idUtilisateur);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getDouble("total");
            }
        }
        return 0.0;
    }

    

}