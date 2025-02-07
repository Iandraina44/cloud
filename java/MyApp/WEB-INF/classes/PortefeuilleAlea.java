package model.crypto;

import java.sql.*;
import java.util.*;

public class PortefeuilleAlea {
    private int idUtilisateur;
    private double totalAchat;
    private double totalVente;
    private double valeurPortefeuille;

    public PortefeuilleAlea(int idUtilisateur, double totalAchat, double totalVente, double valeurPortefeuille) {
        this.idUtilisateur = idUtilisateur;
        this.totalAchat = totalAchat;
        this.totalVente = totalVente;
        this.valeurPortefeuille = valeurPortefeuille;
    }

    public int getIdUtilisateur() {
        return idUtilisateur;
    }

    public double getTotalAchat() {
        return totalAchat;
    }

    public double getTotalVente() {
        return totalVente;
    }

    public double getValeurPortefeuille() {
        return valeurPortefeuille;
    }

    /**
     * Méthode pour récupérer les portefeuilles des utilisateurs selon une date spécifique.
     * 
     * @param conn La connexion à la base de données.
     * @param dateMax La date maximale utilisée pour filtrer les résultats.
     * @return Une liste de PortefeuilleAlea.
     */
    public static List<PortefeuilleAlea> getAllByDateEtHeureMax(Connection conn, String dateMax) {
        List<PortefeuilleAlea> portefeuilles = new ArrayList<>();
        String sql = "CALL get_portefeuille(?)"; // Appel de la procédure stockée

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, dateMax);  // Passage de la date maximale à la procédure stockée
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                portefeuilles.add(new PortefeuilleAlea(
                        rs.getInt("id_utilisateur"),
                        rs.getDouble("total_achat"),
                        rs.getDouble("total_vente"),
                        rs.getDouble("total_fond")  // Assure-toi que c'est le bon alias dans la procédure
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return portefeuilles;
    }
}
