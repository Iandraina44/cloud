package model.transaction;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.sql.*;


public class Commission {
    private int idCommission;
    private String typeCommission; // "achat" ou "vente"
    private double valeur; // Pourcentage de la commission
    private LocalDateTime dateModification;

    // Constructeurs
    public Commission() {}

    public Commission(int idCommission, String typeCommission, double valeur, LocalDateTime dateModification) {
        this.idCommission = idCommission;
        this.typeCommission = typeCommission;
        this.valeur = valeur;
        this.dateModification = dateModification;
    }

    // Getters et Setters
    public int getIdCommission() {
        return idCommission;
    }

    public void setIdCommission(int idCommission) {
        this.idCommission = idCommission;
    }

    public String getTypeCommission() {
        return typeCommission;
    }

    public void setTypeCommission(String typeCommission) {
        this.typeCommission = typeCommission;
    }

    public double getValeur() {
        return valeur;
    }

    public void setValeur(double valeur) {
        this.valeur = valeur;
    }

    public LocalDateTime getDateModification() {
        return dateModification;
    }

    public void setDateModification(LocalDateTime dateModification) {
        this.dateModification = dateModification;
    }

    public void insertCommission(Connection connection) throws SQLException {
        String sql = "INSERT INTO commission (type_commission, valeur, date_modification) VALUES (?, ?, ?)";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, this.typeCommission);
            pstmt.setDouble(2, this.valeur);
            pstmt.setTimestamp(3, Timestamp.valueOf(this.dateModification));
    
            pstmt.executeUpdate();
        }
    }


    public static Commission getClosestCommissionToNow(Connection connection, String typeCommission) throws SQLException {
        String sql = """
            SELECT c.*
            FROM commission c
            WHERE c.type_commission = ?
            ORDER BY ABS(TIMESTAMPDIFF(SECOND, c.date_modification, CONVERT_TZ(NOW(), 'SYSTEM', '+03:00')))
            LIMIT 1
            """;
        

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, typeCommission);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                Commission commission = new Commission();
                commission.setIdCommission(rs.getInt("id_commission"));
                commission.setTypeCommission(rs.getString("type_commission"));
                commission.setValeur(rs.getDouble("valeur"));
                commission.setDateModification(rs.getTimestamp("date_modification").toLocalDateTime());
                return commission;
            }
        }
        return null; 
    }
    


    public static List<Commission> readCommission(Connection connection, String typeCommission) {
        List<Commission> commissions = new ArrayList<>();
        String sql = "SELECT * FROM commission where type_commission=?";
        ResultSet rs=null;
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, typeCommission);


           rs = pstmt.executeQuery();

            while (rs.next()) {
                Commission commission = new Commission();
                commission.setIdCommission(rs.getInt("id_commission"));
                commission.setTypeCommission(rs.getString("type_commission"));
                commission.setValeur(rs.getDouble("valeur"));
                commission.setDateModification(rs.getTimestamp("date_modification").toLocalDateTime());
                commissions.add(commission);
            }
            
        }


           
        catch (SQLException e) {
            System.err.println("erreur " + e.getMessage());
        }

        finally {
            try {
                if (rs != null) rs.close();
            } catch (SQLException e) {
                System.err.println("Error closing resources: " + e.getMessage());
            }
        }
        return commissions;
    }


    public static double calculateTotalCommission(Connection connection, String typeCommission, String crypto, LocalDateTime dateMin, LocalDateTime dateMax) throws SQLException {
        StringBuilder sql = new StringBuilder("SELECT SUM(valeur) AS total FROM commission WHERE type_commission = ?");
        
        // Ajouter condition pour la crypto si nécessaire
        if (crypto != null && !crypto.equals("tous")) {
            sql.append(" AND crypto_monnaie = ?");
        }

        // Ajouter conditions pour les dates
        if (dateMin != null) {
            sql.append(" AND date_modification >= ?");
        }
        if (dateMax != null) {
            sql.append(" AND date_modification <= ?");
        }

        try (PreparedStatement pstmt = connection.prepareStatement(sql.toString())) {
            pstmt.setString(1, typeCommission);

            int paramIndex = 2;
            if (crypto != null && !crypto.equals("tous")) {
                pstmt.setString(paramIndex++, crypto);
            }
            if (dateMin != null) {
                pstmt.setTimestamp(paramIndex++, Timestamp.valueOf(dateMin));
            }
            if (dateMax != null) {
                pstmt.setTimestamp(paramIndex++, Timestamp.valueOf(dateMax));
            }

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getDouble("total");
            }
        }
        return 0;
    }


    public static double calculateAverageCommission(Connection connection, String typeCommission, String crypto, LocalDateTime dateMin, LocalDateTime dateMax) throws SQLException {
        StringBuilder sql = new StringBuilder("SELECT AVG(valeur) AS average FROM commission WHERE type_commission = ?");
        
        // Ajouter condition pour la crypto si nécessaire
        if (crypto != null && !crypto.equals("tous")) {
            sql.append(" AND crypto_monnaie = ?");
        }

        // Ajouter conditions pour les dates
        if (dateMin != null) {
            sql.append(" AND date_modification >= ?");
        }
        if (dateMax != null) {
            sql.append(" AND date_modification <= ?");
        }

        try (PreparedStatement pstmt = connection.prepareStatement(sql.toString())) {
            pstmt.setString(1, typeCommission);

            int paramIndex = 2;
            if (crypto != null && !crypto.equals("tous")) {
                pstmt.setString(paramIndex++, crypto);
            }
            if (dateMin != null) {
                pstmt.setTimestamp(paramIndex++, Timestamp.valueOf(dateMin));
            }
            if (dateMax != null) {
                pstmt.setTimestamp(paramIndex++, Timestamp.valueOf(dateMax));
            }

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getDouble("average");
            }
        }
        return 0;
    }








    

}
