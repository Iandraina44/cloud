package connexion;

import java.sql.*;

public class ConnexionMySQL {
    
    public Connection getConnectionMySQL() {
        Connection c = null;
        try {
            
            Class.forName("com.mysql.cj.jdbc.Driver");
            
            // Créer la connexion avec les paramètres appropriés
            c = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/cloud", 
                "root",                         
                ""           
            );
            
            System.out.println("Connexion à MySQL effectuée avec succès !");
            return c;
        } 
        catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }

        return c;
    }
}
