package model.crypto;
import model.role.*;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import model.crypto.*;

public class Vente {
    private int idVente;
    private Timestamp dateVente;
    private int quantite;
    private double prixUnitaire;
    private Utilisateur utilisateur;
    private int idCryptomonaie;
    private String nomCrypto;
    private double prixWCommission;

    // Default constructor
    public Vente() {}

    // Constructor with parameters
    public Vente(int idVente, Timestamp dateVente, int quantite, double prixUnitaire,  Utilisateur utilisateur, int idCryptomonaie, String nomCrypto) {
        this.idVente = idVente;
        this.dateVente = dateVente;
        this.quantite = quantite;
        this.prixUnitaire = prixUnitaire;
        this.utilisateur = utilisateur;
        this.idCryptomonaie = idCryptomonaie;
        this.nomCrypto = nomCrypto;

    }

    public Vente(int idVente, Timestamp dateVente, int quantite, double prixUnitaire,  Utilisateur utilisateur, int idCryptomonaie, String nomCrypto, double prixWCommission) {
        this.idVente = idVente;
        this.dateVente = dateVente;
        this.quantite = quantite;
        this.prixUnitaire = prixUnitaire;
        this.utilisateur = utilisateur;
        this.idCryptomonaie = idCryptomonaie;
        this.nomCrypto = nomCrypto;
        this.prixWCommission = prixWCommission;


    }

    // public Vente(int idVente, Timestamp dateVente, int quantite, double prixUnitaire, int idUtilisateur, int idCryptomonaie) {
    //     this.idVente = idVente;
    //     this.dateVente = dateVente;
    //     this.quantite = quantite;
    //     this.prixUnitaire = prixUnitaire;
    //     this.idUtilisateur = idUtilisateur;
    //     this.idCryptomonaie = idCryptomonaie;
      

    // }

    public double getPrixWCommission() { 
        return prixWCommission; 
    }
    public void setPrixWCommission(double prixWCommission) { 
        this.prixWCommission = prixWCommission; 
    }


    // Getters and Setters
    public int getIdVente() {
        return idVente;
    }

    public void setIdVente(int idVente) {
        this.idVente = idVente;
    }

    public Timestamp getDateVente() {
        return dateVente;
    }

    public void setDateVente(Timestamp dateVente) {
        this.dateVente = dateVente;
    }

    public int getQuantite() {
        return quantite;
    }

    public void setQuantite(int quantite) {
        this.quantite = quantite;
    }

    public double getPrixUnitaire() {
        return prixUnitaire;
    }

    public void setPrixUnitaire(double prixUnitaire) {
        this.prixUnitaire = prixUnitaire;
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

    public String getNomCrypto() {
        return nomCrypto;
    }

    public void setNomCrypto(String nomCrypto) {
        this.nomCrypto = nomCrypto;
    }

    // CRUD Operations

    // Create (Insert a new sale record)
    public void create(Connection connection) {
        String sql = "INSERT INTO vente (date_vente, quantite, prix_unitaire, id_utilisateur, id_cryptomonaie,commission_appliquee) VALUES (?, ?, ?, ?, ?, ?)";
        PreparedStatement stmt = null;

        try {
            stmt = connection.prepareStatement(sql);
            stmt.setTimestamp(1, this.dateVente);
            stmt.setInt(2, this.quantite);
            stmt.setDouble(3, this.prixUnitaire);
            stmt.setInt(4, this.utilisateur.getIdUtilisateur());
            stmt.setInt(5, this.idCryptomonaie);
            stmt.setDouble(6, this.prixWCommission);
            stmt.executeUpdate();

            PortefeuilleCrypto p = new PortefeuilleCrypto();
            p.setIdUtilisateur(this.utilisateur.getIdUtilisateur());
            p.setIdCryptomonaie(this.idCryptomonaie);
            p.setValeurPortefeuille(this.quantite * -1);
            p.setDatePortefeuille(this.dateVente);
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

    // Read (Retrieve a sale record by ID)
    public static Vente read(Connection connection, int idVente) {
        String sql = "SELECT * FROM vente WHERE id_vente = ?";
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            stmt = connection.prepareStatement(sql);
            stmt.setInt(1, idVente);
            rs = stmt.executeQuery();

            if (rs.next()) {
                return new Vente(
                        rs.getInt("id_vente"),
                        rs.getTimestamp("date_vente"),
                        rs.getInt("quantite"),
                        rs.getDouble("prix_unitaire"),
                        Utilisateur.getById(rs.getInt("id_utilisateur")),
                        rs.getInt("id_cryptomonaie"),
                        Cryptomonaie.getById(connection, rs.getInt("id_cryptomonaie")).getNom()
                );
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving sale record: " + e.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
            } catch (SQLException e) {
                System.err.println("Error closing resources: " + e.getMessage());
            }
        }
        return null;
    }

    // Read (Retrieve all sale records)
    public static List<Vente> readAll(Connection connection) {
        List<Vente> ventes = new ArrayList<>();
        String sql = "SELECT * FROM vente";
        Statement stmt = null;
        ResultSet rs = null;

        try {
            stmt = connection.createStatement();
            rs = stmt.executeQuery(sql);

            while (rs.next()) {
               
                ventes.add(new Vente(
                    rs.getInt("id_vente"),
                    rs.getTimestamp("date_vente"),
                    rs.getInt("quantite"),
                    rs.getDouble("prix_unitaire"),
                    Utilisateur.getById(rs.getInt("id_utilisateur")),
                    rs.getInt("id_cryptomonaie"),
                    Cryptomonaie.getById(connection, rs.getInt("id_cryptomonaie")).getNom(),
                    rs.getDouble("commission_appliquee")
                ));
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
        return ventes;
    }

    public static List<Vente> readAllByUser(Connection connection, int id) {
        List<Vente> ventes = new ArrayList<>();
        String sql = "SELECT * FROM vente WHERE id_utilisateur = ?";
        PreparedStatement pstmt = null;
        ResultSet rs = null;
    
        try {
            // Utilisation de PreparedStatement à la place de Statement
            pstmt = connection.prepareStatement(sql);
            pstmt.setInt(1, id);  // Définir la valeur du paramètre
    
            rs = pstmt.executeQuery(); // Exécution de la requête
    
            while (rs.next()) {
                ventes.add(new Vente(
                    rs.getInt("id_vente"),
                    rs.getTimestamp("date_vente"),
                    rs.getInt("quantite"),
                    rs.getDouble("prix_unitaire"),
                    Utilisateur.getById(rs.getInt("id_utilisateur")),
                    rs.getInt("id_cryptomonaie"),
                    Cryptomonaie.getById(connection, rs.getInt("id_cryptomonaie")).getNom(),
                    rs.getDouble("commission_appliquee")

                ));
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving all sale records: " + e.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close(); // Fermeture de PreparedStatement
            } catch (SQLException e) {
                System.err.println("Error closing resources: " + e.getMessage());
            }
        }
        return ventes;
    }
    
    // Update (Update a sale record)
    public void update(Connection connection) {
        String sql = "UPDATE vente SET date_vente = ?, quantite = ?, prix_unitaire = ?, id_utilisateur = ?, id_cryptomonaie = ? WHERE id_vente = ?";
        PreparedStatement stmt = null;

        try {
            stmt = connection.prepareStatement(sql);
            stmt.setTimestamp(1, this.dateVente);
            stmt.setInt(2, this.quantite);
            stmt.setDouble(3, this.prixUnitaire);
            stmt.setInt(4, this.utilisateur.getIdUtilisateur());
            stmt.setInt(5, this.idCryptomonaie);
            stmt.setInt(6, this.idVente);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error during update: " + e.getMessage());
        } finally {
            try {
                if (stmt != null) stmt.close();
            } catch (SQLException e) {
                System.err.println("Error closing PreparedStatement: " + e.getMessage());
            }
        }
    }

    // Delete (Delete a sale record by ID)
    public static void delete(Connection connection, int idVente) {
        String sql = "DELETE FROM vente WHERE id_vente = ?";
        PreparedStatement stmt = null;

        try {
            stmt = connection.prepareStatement(sql);
            stmt.setInt(1, idVente);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error during deletion: " + e.getMessage());
        } finally {
            try {
                if (stmt != null) stmt.close();
            } catch (SQLException e) {
                System.err.println("Error closing PreparedStatement: " + e.getMessage());
            }
        }
    }

    // Vérifie si le portefeuille a assez de fonds pour effectuer la vente
public boolean hasSufficientFunds(Connection connection) {
    String sql = "SELECT SUM(valeur_portefeuille) FROM portefeuille_crypto WHERE id_utilisateur = ? AND id_cryptomonaie = ?";
    PreparedStatement stmt = null;
    ResultSet rs = null;

    try {
        stmt = connection.prepareStatement(sql);
        stmt.setInt(1, this.utilisateur.getIdUtilisateur());
        stmt.setInt(2, this.idCryptomonaie);
        rs = stmt.executeQuery();

        if (rs.next()) {
            double valeurPortefeuille = rs.getDouble(1); // Correction ici
            return valeurPortefeuille >= this.quantite; // Retourne directement le résultat
        }
    } catch (SQLException e) {
        System.err.println("Erreur lors de la vérification du solde du portefeuille : " + e.getMessage());
    } finally {
        try {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
        } catch (SQLException e) {
            System.err.println("Erreur lors de la fermeture des ressources : " + e.getMessage());
        }
    }

    return false; // Retourne false en cas d'erreur ou de solde insuffisant
}


    // Get the latest price of the cryptocurrency
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

    // Create (Insert a sale record if the wallet has enough funds)
    public void createIfSufficientFunds(Connection connection) {
        if (hasSufficientFunds(connection)) {
            this.prixUnitaire = getLatestCryptoPrice(connection);
            create(connection);
            decreaseWalletQuantity(connection);  // Decrease wallet quantity after sale
        } else {
            System.out.println("Insufficient funds to complete the sale.");
        }
    }

    public static List<Vente> getPortefeuilleByUtilisateur(Connection connection, int idUtilisateur) {
        String sql = "SELECT " +
                     "c.nom_cryptomonaie AS cryptomonaie, " +
                     "SUM(p.valeur_portefeuille) AS quantite, " + // Agrégation de la quantité
                     "(SELECT c1.valeur_cour " +
                     " FROM cours_crypto c1 " +
                     " WHERE c1.id_cryptomonaie = c.id_cryptomonaie " +
                     " ORDER BY c1.date_cour DESC " + // Trier par date décroissante pour récupérer la dernière valeur
                     " LIMIT 1) AS prix_unitaire, " + // Dernière valeur connue du prix unitaire
                     "c.id_cryptomonaie " +
                     "FROM portefeuille_crypto p " +
                     "JOIN cryptomonaie c ON p.id_cryptomonaie = c.id_cryptomonaie " +
                     "WHERE p.id_utilisateur = ? " + // ID utilisateur passé en paramètre
                     "GROUP BY c.id_cryptomonaie, c.nom_cryptomonaie " + // Groupement par ID et nom de la cryptomonnaie
                     "ORDER BY c.nom_cryptomonaie";
        
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Vente> ventes = new ArrayList<>();
        
        try {
            stmt = connection.prepareStatement(sql);
            stmt.setInt(1, idUtilisateur); // Assigner l'ID utilisateur au paramètre
            rs = stmt.executeQuery();
            
            while (rs.next()) {
                // Créer un objet Vente avec les données récupérées
                Vente vente = new Vente();
                vente.setNomCrypto(rs.getString("cryptomonaie")); // Récupérer le nom de la cryptomonnaie
                vente.setQuantite(rs.getInt("quantite")); // Récupérer la quantité
                vente.setPrixUnitaire(rs.getDouble("prix_unitaire")); // Récupérer le dernier prix unitaire
                vente.setIdCryptomonaie(rs.getInt("id_cryptomonaie")); // Récupérer l'ID de la cryptomonnaie
                vente.setUtilisateur(Utilisateur.getById(idUtilisateur)); // Ajouter l'ID de l'utilisateur
                
                ventes.add(vente); // Ajouter l'objet à la liste
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des données du portefeuille : " + e.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
            } catch (SQLException e) {
                System.err.println("Erreur lors de la fermeture des ressources : " + e.getMessage());
            }
        }
        return ventes;
    }
    

    // Method to decrease the quantity in the user's wallet after a sale
public void decreaseWalletQuantity(Connection connection) {
    String sql = "UPDATE portefeuille_crypto " +
                 "SET valeur_portefeuille = valeur_portefeuille - ? " +
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


public static List<Vente> readAllFiltrer(Connection connection, LocalDateTime dateMin, LocalDateTime dateMax, int idCryptomonaie, int idUtilisateur) {
    List<Vente> ventes = new ArrayList<>();
    StringBuilder sql = new StringBuilder("SELECT * FROM vente WHERE 1=1");

    // Ajouter des filtres si nécessaires
    if (dateMin != null) {
        sql.append(" AND date_vente >= ?");
    }
    if (dateMax != null) {
        sql.append(" AND date_vente <= ?");
    }

    if (idCryptomonaie != -1) {
        sql.append(" AND id_cryptomonaie = ?");
    }
    if (idUtilisateur != -1) {
        sql.append(" AND id_utilisateur = ?");
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
        if (idCryptomonaie != -1) {
            pstmt.setInt(paramIndex++, idCryptomonaie);
        }
        if (idUtilisateur != -1) {
            pstmt.setInt(paramIndex++, idUtilisateur);
        }

        ResultSet rs = pstmt.executeQuery();

        while (rs.next()) {
            Vente ac = new Vente();
            ac.setIdVente(rs.getInt("id_vente"));
            ac.setDateVente(rs.getTimestamp("date_vente"));
            ac.setQuantite(rs.getInt("quantite"));
            ac.setPrixUnitaire(rs.getDouble("prix_unitaire"));
            ac.setIdCryptomonaie(rs.getInt("id_cryptomonaie"));
            ac.setUtilisateur(Utilisateur.getById(rs.getInt("id_utilisateur")));
            ac.setPrixWCommission(rs.getDouble("commission_appliquee"));
            
            ac.setNomCrypto(Cryptomonaie.getById(connection, ac.getIdCryptomonaie()).getNom());

            ventes.add(ac);
        }
    } catch (SQLException e) {
        System.err.println("Error retrieving all sale records: " + e.getMessage());
    }
    return ventes;
}



public static double calculateTotalCommission(Connection connection, int crypto, LocalDateTime dateMin, LocalDateTime dateMax) throws SQLException {
        // Construction de la requête SQL
        StringBuilder sql = new StringBuilder("SELECT SUM(commission_appliquee) AS total FROM vente WHERE commission_appliquee > 0");
    
        // Ajouter condition pour la crypto si nécessaire
        if (crypto != -1 ) {
            sql.append(" AND id_cryptomonaie = ?");
        }
    
        // Ajouter conditions pour les dates
        if (dateMin != null) {
            sql.append(" AND date_vente >= ?");
        }
        if (dateMax != null) {
            sql.append(" AND date_vente <= ?");
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
        StringBuilder sql = new StringBuilder("SELECT AVG(commission_appliquee) AS average FROM vente WHERE commission_appliquee > 0");
    
        // Ajouter condition pour la crypto si nécessaire
        if (crypto != -1 ) {
            sql.append(" AND id_cryptomonaie = ?");
        }
    
        // Ajouter conditions pour les dates
        if (dateMin != null) {
            sql.append(" AND date_vente >= ?");
        }
        if (dateMax != null) {
            sql.append(" AND date_vente <= ?");
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
    
    
    


    // toString() for displaying sale information
    // @Override
    // public String toString() {
    //     return "Vente{" +
    //             "idVente=" + idVente +
    //             ", dateVente=" + dateVente +
    //             ", quantite=" + quantite +
    //             ", prixUnitaire=" + prixUnitaire +
    //             ", idUtilisateur=" + idUtilisateur +
    //             ", idCryptomonaie=" + idCryptomonaie +
    //             '}';
    // }
}
