package model.crypto;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CoursCrypto {
    private int idCour;
    private Timestamp dateCour;
    private double valeurCour;
    private int idCryptomonaie;

    // Constructeur par défaut
    public CoursCrypto() {}

    // Constructeur avec paramètres
    public CoursCrypto(int idCour, Timestamp dateCour, double valeurCour, int idCryptomonaie) {
        this.idCour = idCour;
        this.dateCour = dateCour;
        this.valeurCour = valeurCour;
        this.idCryptomonaie = idCryptomonaie;
    }

    // Getters et Setters
    public int getIdCour() {
        return idCour;
    }

    public void setIdCour(int idCour) {
        this.idCour = idCour;
    }

    public Timestamp getDateCour() {
        return dateCour;
    }

    public void setDateCour(Timestamp dateCour) {
        this.dateCour = dateCour;
    }

    public double getValeurCour() {
        return valeurCour;
    }

    public void setValeurCour(double valeurCour) {
        this.valeurCour = valeurCour;
    }

    public int getIdCryptomonaie() {
        return idCryptomonaie;
    }

    public void setIdCryptomonaie(int idCryptomonaie) {
        this.idCryptomonaie = idCryptomonaie;
    }

    // CRUD Operations

    // Create (Insertion d'un nouveau cours crypto)
    public void create(Connection connection) {
        String sql = "INSERT INTO cours_crypto (date_cour, valeur_cour, id_cryptomonaie) VALUES (?, ?, ?)";
        PreparedStatement stmt = null;

        try {
            stmt = connection.prepareStatement(sql);
            stmt.setTimestamp(1, this.dateCour);
            stmt.setDouble(2, this.valeurCour);
            stmt.setInt(3, this.idCryptomonaie);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'insertion : " + e.getMessage());
        } finally {
            try {
                if (stmt != null) stmt.close();
            } catch (SQLException e) {
                System.err.println("Erreur lors de la fermeture du PreparedStatement : " + e.getMessage());
            }
        }
    }

    // Read (Récupération d'un cours crypto par ID)
    public static CoursCrypto read(Connection connection, int idCour) {
        String sql = "SELECT * FROM cours_crypto WHERE id_cryptomonaie = ? ORDER BY date_cour DESC limit 10";
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            stmt = connection.prepareStatement(sql);
            stmt.setInt(1, idCour);
            rs = stmt.executeQuery();

            if (rs.next()) {
                return new CoursCrypto(
                    rs.getInt("id_cour"),
                    rs.getTimestamp("date_cour"),
                    rs.getDouble("valeur_cour"),
                    rs.getInt("id_cryptomonaie")
                );
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération du cours crypto : " + e.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
            } catch (SQLException e) {
                System.err.println("Erreur lors de la fermeture des ressources : " + e.getMessage());
            }
        }
        return null;
    }

    // Read (Récupération de tous les cours crypto)
    public static List<CoursCrypto> readAll(Connection connection) {
        List<CoursCrypto> cours = new ArrayList<>();
        String sql = "SELECT * FROM cours_crypto ORDER BY date_cour DESC limit 10";
        Statement stmt = null;
        ResultSet rs = null;

        try {
            stmt = connection.createStatement();
            rs = stmt.executeQuery(sql);

            while (rs.next()) {
                cours.add(new CoursCrypto(
                    rs.getInt("id_cour"),
                    rs.getTimestamp("date_cour"),
                    rs.getDouble("valeur_cour"),
                    rs.getInt("id_cryptomonaie")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des cours crypto : " + e.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
            } catch (SQLException e) {
                System.err.println("Erreur lors de la fermeture des ressources : " + e.getMessage());
            }
        }
        return cours;
    }

    public static List<CoursCrypto> readLatestPerCryptomonnaie(Connection connection) {
        List<CoursCrypto> cours = new ArrayList<>();
        String sql = "SELECT cc1.* FROM cours_crypto cc1 " +
                     "INNER JOIN (" +
                     "    SELECT id_cryptomonaie, MAX(date_cour) AS max_date " +
                     "    FROM cours_crypto " +
                     "    GROUP BY id_cryptomonaie" +
                     ") cc2 " +
                     "ON cc1.id_cryptomonaie = cc2.id_cryptomonaie AND cc1.date_cour = cc2.max_date";
        Statement stmt = null;
        ResultSet rs = null;

        try {
            stmt = connection.createStatement();
            rs = stmt.executeQuery(sql);

            while (rs.next()) {
                cours.add(new CoursCrypto(
                    rs.getInt("id_cour"),
                    rs.getTimestamp("date_cour"),
                    rs.getDouble("valeur_cour"),
                    rs.getInt("id_cryptomonaie")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des cours crypto les plus récents : " + e.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
            } catch (SQLException e) {
                System.err.println("Erreur lors de la fermeture des ressources : " + e.getMessage());
            }
        }
        return cours;
    }

    // Update (Mise à jour des informations d'un cours crypto)
    public void update(Connection connection) {
        String sql = "UPDATE cours_crypto SET date_cour = ?, valeur_cour = ?, id_cryptomonaie = ? WHERE id_cour = ?";
        PreparedStatement stmt = null;

        try {
            stmt = connection.prepareStatement(sql);
            stmt.setTimestamp(1, this.dateCour);
            stmt.setDouble(2, this.valeurCour);
            stmt.setInt(3, this.idCryptomonaie);
            stmt.setInt(4, this.idCour);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Erreur lors de la mise à jour : " + e.getMessage());
        } finally {
            try {
                if (stmt != null) stmt.close();
            } catch (SQLException e) {
                System.err.println("Erreur lors de la fermeture du PreparedStatement : " + e.getMessage());
            }
        }
    }

    // Delete (Suppression d'un cours crypto par ID)
    public static void delete(Connection connection, int idCour) {
        String sql = "DELETE FROM cours_crypto WHERE id_cour = ?";
        PreparedStatement stmt = null;

        try {
            stmt = connection.prepareStatement(sql);
            stmt.setInt(1, idCour);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Erreur lors de la suppression : " + e.getMessage());
        } finally {
            try {
                if (stmt != null) stmt.close();
            } catch (SQLException e) {
                System.err.println("Erreur lors de la fermeture du PreparedStatement : " + e.getMessage());
            }
        }
    }


    public static void generateNewCours(Connection connection) throws SQLException{
        // Récupérer les cours les plus récents pour chaque cryptomonnaie
        List<CoursCrypto> latestCours = readLatestPerCryptomonnaie(connection);
    
        // Générer une seule fois la date actuelle
        Timestamp now = new Timestamp(System.currentTimeMillis());
    
        for (CoursCrypto cours : latestCours) {
            // Générer une variation aléatoire de ±10 %
            double variation = (Math.random() * 0.2) - 0.1; // Variation entre -0.1 et 0.1
            double newValeur = cours.getValeurCour() * (1 + variation);
    
            // Créer un nouvel objet CoursCrypto
            CoursCrypto newCours = new CoursCrypto();
            newCours.setDateCour(now); // Utiliser la même date pour tous
            newCours.setValeurCour(newValeur);
            newCours.setIdCryptomonaie(cours.getIdCryptomonaie());
    
            // Insérer le nouveau cours dans la base de données
            newCours.create(connection);
        }
    }

    public static List<List<CoursCrypto>> getCoursCryptoGroupesParCryptomonnaie(Connection connection) {
        List<List<CoursCrypto>> groupedCours = new ArrayList<>();
        
        // Étape 1 : Récupérer tous les id_cryptomonaie distincts
        String distinctIdsSQL = "SELECT DISTINCT id_cryptomonaie FROM cours_crypto";
        Statement stmt = null;
        ResultSet rs = null;
        
        try {
            stmt = connection.createStatement();
            rs = stmt.executeQuery(distinctIdsSQL);
            
            // Étape 2 : Pour chaque id_cryptomonnaie, récupérer les 10 derniers cours
            while (rs.next()) {
                int cryptomonnaieId = rs.getInt("id_cryptomonaie");
                
                // Récupérer les 10 derniers cours pour cette cryptomonnaie
                List<CoursCrypto> coursList = getCoursForCryptomonnaie(connection, cryptomonnaieId);
                if (!coursList.isEmpty()) {
                    groupedCours.add(coursList);  // Ajouter le tableau de cours à la liste des groupes
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des ids des cryptomonnaies : " + e.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
            } catch (SQLException e) {
                System.err.println("Erreur lors de la fermeture des ressources : " + e.getMessage());
            }
        }
        
        return groupedCours;
    }
    
    // Méthode pour récupérer les 10 derniers cours pour une cryptomonnaie donnée
    private static List<CoursCrypto> getCoursForCryptomonnaie(Connection connection, int cryptomonnaieId) {
        List<CoursCrypto> coursList = new ArrayList<>();
        String sql = "SELECT * FROM cours_crypto WHERE id_cryptomonaie = ? ORDER BY date_cour DESC LIMIT 10";
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            stmt = connection.prepareStatement(sql);
            stmt.setInt(1, cryptomonnaieId);
            rs = stmt.executeQuery();
            
            while (rs.next()) {
                coursList.add(new CoursCrypto(
                    rs.getInt("id_cour"),
                    rs.getTimestamp("date_cour"),
                    rs.getDouble("valeur_cour"),
                    cryptomonnaieId
                ));
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des cours crypto pour id_cryptomonnaie " + cryptomonnaieId + ": " + e.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
            } catch (SQLException e) {
                System.err.println("Erreur lors de la fermeture des ressources : " + e.getMessage());
            }
        }
        
        return coursList;
    }

    public static List<Double> getCryptoValues(Connection connection, int idCrypto, String dateTimeMin, String dateTimeMax) throws SQLException {
        List<Double> values = new ArrayList<>();
        String query = "SELECT valeur_cour FROM cours_crypto WHERE id_cryptomonaie = ? AND date_cour BETWEEN ? AND ? ";
        
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, idCrypto);
            stmt.setString(2, dateTimeMin);
            stmt.setString(3, dateTimeMax);
            
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                values.add(rs.getDouble("valeur_cour"));
            }
        }
        return values;
    }

    public static double getMin(Connection connection, int idCrypto, String dateTimeMin, String dateTimeMax) throws SQLException {
        List<Double> values = getCryptoValues(connection, idCrypto, dateTimeMin, dateTimeMax);
        return values.isEmpty() ? Double.NaN : Collections.min(values);
    }

    public static double getMax(Connection connection, int idCrypto, String dateTimeMin, String dateTimeMax) throws SQLException {
        List<Double> values = getCryptoValues(connection, idCrypto, dateTimeMin, dateTimeMax);
        return values.isEmpty() ? Double.NaN : Collections.max(values);
    }

    public static double getMoyenne(Connection connection, int idCrypto, String dateTimeMin, String dateTimeMax) throws SQLException {
        List<Double> values = getCryptoValues(connection, idCrypto, dateTimeMin, dateTimeMax);
        if (values.isEmpty()) return Double.NaN;

        double sum = values.stream().mapToDouble(Double::doubleValue).sum();
        return sum / values.size();
    }

    public static double getEcartType(Connection connection, int idCrypto, String dateTimeMin, String dateTimeMax) throws SQLException {
        List<Double> values = getCryptoValues(connection, idCrypto, dateTimeMin, dateTimeMax);
        if (values.isEmpty()) return Double.NaN;

        double moyenne = getMoyenne(connection, idCrypto, dateTimeMin, dateTimeMax);
        double variance = values.stream().mapToDouble(v -> Math.pow(v - moyenne, 2)).sum() / values.size();
        return Math.sqrt(variance);
    }

    public static double getPremierQuartile(Connection connection, int idCrypto, String dateTimeMin, String dateTimeMax) throws SQLException {
        List<Double> values = getCryptoValues(connection, idCrypto, dateTimeMin, dateTimeMax);
        if (values.isEmpty()) return Double.NaN;

        Collections.sort(values);
        int index = (int) Math.ceil(0.25 * values.size()) - 1;
        return values.get(Math.max(index, 0)); // Éviter les erreurs d'index négatif
    }
    
    

    // toString() pour affichage
    @Override
    public String toString() {
        return "CoursCrypto{" +
                "idCour=" + idCour +
                ", dateCour=" + dateCour +
                ", valeurCour=" + valeurCour +
                ", idCryptomonaie=" + idCryptomonaie +
                '}';
    }

    public static CoursCrypto prixActuel(Connection connection, int idCour) {
        String sql = "SELECT * FROM cours_crypto WHERE id_cryptomonaie = ? ORDER BY date_cour DESC limit 1";
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            stmt = connection.prepareStatement(sql);
            stmt.setInt(1, idCour);
            rs = stmt.executeQuery();

            if (rs.next()) {
                return new CoursCrypto(
                    rs.getInt("id_cour"),
                    rs.getTimestamp("date_cour"),
                    rs.getDouble("valeur_cour"),
                    rs.getInt("id_cryptomonaie")
                );
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération du cours crypto : " + e.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
            } catch (SQLException e) {
                System.err.println("Erreur lors de la fermeture des ressources : " + e.getMessage());
            }
        }
        return null;
    }
}
