package model.crypto;
import connexion.*;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal;
import model.role.*;
import model.transaction.Fond;


public class AchatCrypto {

    private int idAchat;
    private Timestamp dateAchat;
    private int quantite;
    private double prixUnitaire;
    private Utilisateur utilisateur;
    private int idCryptomonaie;
    private String nomCrypto;
    private double prixWCommission;




    // Constructeur
    public AchatCrypto(int idAchat,  Utilisateur utilisateur, int idCryptomonaie, int quantite, Timestamp dateAchat, double prixUnitaire, String nomCrypto, double prixWCommission) {
        this.idAchat = idAchat;
        this.utilisateur = utilisateur;
        this.idCryptomonaie = idCryptomonaie;
        this.quantite = quantite;
        this.dateAchat = dateAchat;
        this.prixUnitaire = prixUnitaire;
        this.nomCrypto = nomCrypto;
        this.prixWCommission = prixWCommission;


    }

    public AchatCrypto() {
  

    }

    // Getters et Setters
    public int getIdAchat() { 
        return idAchat; 
    }
    public void setIdAchat(int idAchat) {
         this.idAchat = idAchat; 
    }
    
    public Utilisateur getUtilisateur() {
        return utilisateur;
    }

    public void setUtilisateur( Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
    }

    
    public int getIdCryptomonaie() { 
        return idCryptomonaie; 
    }
    public void setIdCryptomonaie(int idCryptomonaie) { 
        this.idCryptomonaie = idCryptomonaie; 
    }
    
    public int getQuantite() { 
        return quantite; 
    }
    public void setQuantite(int quantite) {
         this.quantite = quantite;
    }

    public String getNomCrypto() {
        return nomCrypto;
    }

    public void setNomCrypto(String nomCrypto) {
        this.nomCrypto = nomCrypto;
    }

    public double getPrixWCommission() { 
        return prixWCommission; 
    }
    public void setPrixWCommission(double prixWCommission) { 
        this.prixWCommission = prixWCommission; 
    }

    public double getPrixUnitaire() { 
        return prixUnitaire; 
    }
    public void setPrixUnitaire(double prixUnitaire) { 
        this.prixUnitaire = prixUnitaire; 
    }
    
    public Timestamp getDateAchat() {
         return dateAchat; 
    }
    public void setDateAchat(Timestamp dateAchat) {
         this.dateAchat = dateAchat; 
    }

    public boolean hasSufficientFunds(Connection connection) {
        CoursCrypto cours=CoursCrypto.prixActuel(connection,this.idCryptomonaie);
        double total=cours.getValeurCour()*this.quantite;
        double fond= Fond.totalfond(connection,this.utilisateur.getIdUtilisateur());
        System.out.println("FOND"+fond);
        if(total<=fond){
            return true;
        } 
        return false;
      
    }


    public double getLatestCryptoPrice(Connection connection) {
        String sql = "SELECT valeur_cour FROM cours_crypto WHERE id_cryptomonaie = ? ORDER BY date_cour DESC LIMIT 1";
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            stmt = connection.prepareStatement(sql);
            stmt.setInt(1, this.idCryptomonaie);
            rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getDouble("valeur_cour");
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving latest crypto price: " + e.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
            } catch (SQLException e) {
                System.err.println("Error closing resources: " + e.getMessage());
            }
        }
        return 0.0;
    }

    public static List<AchatCrypto> readAllByUser(Connection connection, int id) {
        List<AchatCrypto> achats = new ArrayList<>();
        String sql = "SELECT * FROM achat WHERE id_utilisateur = ?";
        PreparedStatement stmt = null;
        ResultSet rs = null;
    
        try {
            stmt = connection.prepareStatement(sql);
            stmt.setInt(1, id); // On assigne le paramètre correctement
            rs = stmt.executeQuery(); // Pas besoin de passer la requête ici
    
            while (rs.next()) {
                AchatCrypto ac = new AchatCrypto();
                ac.setIdAchat(rs.getInt("id_achat"));
                ac.setDateAchat(rs.getTimestamp("date_achat"));
                ac.setQuantite(rs.getInt("quantite"));
                ac.setPrixUnitaire(rs.getDouble("prix_unitaire"));
                ac.setPrixWCommission(rs.getDouble("commission_appliquee"));

                ac.setIdCryptomonaie(rs.getInt("id_cryptomonaie"));
                ac.setUtilisateur(Utilisateur.getById(rs.getInt("id_utilisateur")));
                ac.setNomCrypto(Cryptomonaie.getById(connection, ac.getIdCryptomonaie()).getNom());
                
    
                achats.add(ac);
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving all sale records: " + e.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
            } catch (SQLException e) {
                System.err.println("Error closing resources: " + e.getMessage());
            }
        }
        return achats;
    }
    public static List<AchatCrypto> readAll(Connection connection) {
        List<AchatCrypto> achats = new ArrayList<>();
        String sql = "SELECT * FROM achat";
        Statement stmt = null;
        ResultSet rs = null;

        try {
            stmt = connection.createStatement();
            rs = stmt.executeQuery(sql);

            while (rs.next()) {
                AchatCrypto ac = new AchatCrypto();
                ac.setIdAchat(rs.getInt("id_achat"));
                ac.setDateAchat(rs.getTimestamp("date_achat"));
                ac.setQuantite(rs.getInt("quantite"));
                ac.setPrixUnitaire(rs.getDouble("prix_unitaire"));
                ac.setIdCryptomonaie(rs.getInt("id_cryptomonaie"));
                ac.setUtilisateur(Utilisateur.getById(rs.getInt("id_utilisateur")));
                ac.setPrixWCommission(rs.getDouble("commission_appliquee"));

                ac.setNomCrypto(Cryptomonaie.getById(connection, ac.getIdCryptomonaie()).getNom());
    
                achats.add(ac);
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving all sale records: " + e.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
            } catch (SQLException e) {
                System.err.println("Error closing resources: " + e.getMessage());
            }
        }
        return achats;
    }



    public static List<AchatCrypto> readAllFiltrer(Connection connection, LocalDateTime dateMin, LocalDateTime dateMax) {
        List<AchatCrypto> achats = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM achat WHERE 1=1");
    
        // Ajouter des filtres si nécessaires
        if (dateMin != null) {
            sql.append(" AND date_achat >= ?");
        }
        if (dateMax != null) {
            sql.append(" AND date_achat <= ?");
        }
    
        try (PreparedStatement pstmt = connection.prepareStatement(sql.toString())) {
            int paramIndex = 1;
            
            // Ajout des paramètres si nécessaires
            if (dateMin != null) {
                pstmt.setTimestamp(paramIndex++, Timestamp.valueOf(dateMin));
            }
            if (dateMax != null) {
                pstmt.setTimestamp(paramIndex++, Timestamp.valueOf(dateMax));
            }
    
            ResultSet rs = pstmt.executeQuery();
    
            while (rs.next()) {
                AchatCrypto ac = new AchatCrypto();
                ac.setIdAchat(rs.getInt("id_achat"));
                ac.setDateAchat(rs.getTimestamp("date_achat"));
                ac.setQuantite(rs.getInt("quantite"));
                ac.setPrixUnitaire(rs.getDouble("prix_unitaire"));
                ac.setIdCryptomonaie(rs.getInt("id_cryptomonaie"));
                ac.setUtilisateur(Utilisateur.getById(rs.getInt("id_utilisateur")));
                ac.setPrixWCommission(rs.getDouble("commission_appliquee"));
    
                ac.setNomCrypto(Cryptomonaie.getById(connection, ac.getIdCryptomonaie()).getNom());
    
                achats.add(ac);
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving all sale records: " + e.getMessage());
        }
        return achats;
    }

    

    public static List<AchatCrypto> readAllFiltrer(Connection connection, LocalDateTime dateMin, LocalDateTime dateMax, int idCryptomonaie, int idUtilisateur) {
        List<AchatCrypto> achats = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM achat WHERE 1=1");
    
        // Ajouter des filtres dynamiquement
        if (dateMin != null) {
            sql.append(" AND date_achat >= ?");
        }
        if (dateMax != null) {
            sql.append(" AND date_achat <= ?");
        }
        if (idCryptomonaie != -1) {
            sql.append(" AND id_cryptomonaie = ?");
        }
        if (idUtilisateur != -1) {
            sql.append(" AND id_utilisateur = ?");
        }
    
        try (PreparedStatement pstmt = connection.prepareStatement(sql.toString())) {
            int paramIndex = 1;
    
            // Ajout des paramètres
            if (dateMin != null) {
                pstmt.setTimestamp(paramIndex++, Timestamp.valueOf(dateMin));
            }
            if (dateMax != null) {
                pstmt.setTimestamp(paramIndex++, Timestamp.valueOf(dateMax));
            }
            if (idCryptomonaie != -1) {
                pstmt.setInt(paramIndex++, idCryptomonaie);
            }
            if (idUtilisateur != -1) {
                pstmt.setInt(paramIndex++, idUtilisateur);
            }
    
            ResultSet rs = pstmt.executeQuery();
    
            while (rs.next()) {
                AchatCrypto ac = new AchatCrypto();
                ac.setIdAchat(rs.getInt("id_achat"));
                ac.setDateAchat(rs.getTimestamp("date_achat"));
                ac.setQuantite(rs.getInt("quantite"));
                ac.setPrixUnitaire(rs.getDouble("prix_unitaire"));
                ac.setIdCryptomonaie(rs.getInt("id_cryptomonaie"));
                ac.setUtilisateur(Utilisateur.getById(rs.getInt("id_utilisateur")));
                ac.setPrixWCommission(rs.getDouble("commission_appliquee"));
    
                ac.setNomCrypto(Cryptomonaie.getById(connection, ac.getIdCryptomonaie()).getNom());
    
                achats.add(ac);
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des achats : " + e.getMessage());
        }
        return achats;
    }
    

    
    public void create(Connection connection) {
        String sql = "INSERT INTO achat (date_achat, quantite, prix_unitaire, id_utilisateur, id_cryptomonaie, commission_appliquee) VALUES (?, ?, ?, ?, ?, ?)";
        PreparedStatement stmt = null;

        try {
            stmt = connection.prepareStatement(sql);
            stmt.setTimestamp(1, this.dateAchat);
            stmt.setInt(2, this.quantite);
            stmt.setDouble(3, this.prixUnitaire);
            stmt.setInt(4, this.utilisateur.getIdUtilisateur());
            stmt.setInt(5, this.idCryptomonaie);
            stmt.setDouble(6, this.prixWCommission);

            stmt.executeUpdate();

            PortefeuilleCrypto p = new PortefeuilleCrypto();
            p.setIdUtilisateur(this.utilisateur.getIdUtilisateur());
            p.setIdCryptomonaie(this.idCryptomonaie);
            p.setValeurPortefeuille(this.quantite);
            p.setDatePortefeuille(this.dateAchat);
            p.create(connection);
            
        } catch (SQLException e) {
            System.err.println("Error during insertion: " + e.getMessage());
        } finally {
            try {
                if (stmt != null) stmt.close();
            } catch (SQLException e) {
                System.err.println("Error closing PreparedStatement: " + e.getMessage());
            }
        }
    }



    public static double calculateTotalCommission(Connection connection, int crypto, LocalDateTime dateMin, LocalDateTime dateMax) throws SQLException {
        // Construction de la requête SQL
        StringBuilder sql = new StringBuilder("SELECT SUM(commission_appliquee) AS total FROM achat WHERE commission_appliquee > 0");
    
        // Ajouter condition pour la crypto si nécessaire
        if (crypto != -1 ) {
            sql.append(" AND id_cryptomonaie = ?");
        }
    
        // Ajouter conditions pour les dates
        if (dateMin != null) {
            sql.append(" AND date_achat >= ?");
        }
        if (dateMax != null) {
            sql.append(" AND date_achat <= ?");
        }
    
        try (PreparedStatement pstmt = connection.prepareStatement(sql.toString())) {
            int paramIndex = 1;
    
            // Ajouter le paramètre pour la crypto (ID ou nom)
            if (crypto != -1 ) {
                pstmt.setInt(paramIndex++, crypto); // Si crypto est un nom, change la requête pour l'utiliser correctement
            }
    
            // Ajouter les paramètres de date
            if (dateMin != null) {
                pstmt.setTimestamp(paramIndex++, Timestamp.valueOf(dateMin));
            }
            if (dateMax != null) {
                pstmt.setTimestamp(paramIndex++, Timestamp.valueOf(dateMax));
            }
    
            // Exécution de la requête et récupération du résultat
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getDouble("total");
            }
        }
    
        return 0; // Retourne 0 si aucune donnée n'a été trouvée
    }



    public static double calculateMoyenneCommission(Connection connection, int crypto, LocalDateTime dateMin, LocalDateTime dateMax) throws SQLException {
        // Construction de la requête SQL
        StringBuilder sql = new StringBuilder("SELECT AVG(commission_appliquee) AS average FROM achat WHERE commission_appliquee > 0");
    
        // Ajouter condition pour la crypto si nécessaire
        if (crypto != -1 ) {
            sql.append(" AND id_cryptomonaie = ?");
        }
    
        // Ajouter conditions pour les dates
        if (dateMin != null) {
            sql.append(" AND date_achat >= ?");
        }
        if (dateMax != null) {
            sql.append(" AND date_achat <= ?");
        }
    
        try (PreparedStatement pstmt = connection.prepareStatement(sql.toString())) {
            int paramIndex = 1;
    
            // Ajouter le paramètre pour la crypto (ID ou nom)
            if (crypto != -1 ) {
                pstmt.setInt(paramIndex++, crypto); // Si crypto est un nom, change la requête pour l'utiliser correctement
            }
    
            // Ajouter les paramètres de date
            if (dateMin != null) {
                pstmt.setTimestamp(paramIndex++, Timestamp.valueOf(dateMin));
            }
            if (dateMax != null) {
                pstmt.setTimestamp(paramIndex++, Timestamp.valueOf(dateMax));
            }
    
            // Exécution de la requête et récupération du résultat
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getDouble("average");
            }
        }
    
        return 0; // Retourne 0 si aucune donnée n'a été trouvée
    }
    
    





    
    
    



    public void decreaseWalletQuantity(Connection connection) {
        String sql = "UPDATE portefeuille_crypto " +
                     "SET valeur_portefeuille = valeur_portefeuille + ? " +
                     "WHERE id_utilisateur = ? AND id_cryptomonaie = ?";
        PreparedStatement stmt = null;
    
        try {
            stmt = connection.prepareStatement(sql);
            stmt.setInt(1, this.quantite);  // Decrease the quantity by the amount sold
            stmt.setInt(2, this.utilisateur.getIdUtilisateur());
            stmt.setInt(3, this.idCryptomonaie);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error updating wallet quantity: " + e.getMessage());
        } finally {
            try {
                if (stmt != null) stmt.close();
            } catch (SQLException e) {
                System.err.println("Error closing PreparedStatement: " + e.getMessage());
            }
        }
    }
}
