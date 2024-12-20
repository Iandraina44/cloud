package main;

import connexion.*;
import model.*;
import java.sql.*;

public class Main {
    public static void main(String[] args) {
        ConnexionMySQL mySQL = new ConnexionMySQL();
        Connection connection = mySQL.getConnectionMySQL();
    
        try {
            // Générer de nouveaux cours pour chaque cryptomonnaie
            CoursCrypto.generateNewCours(connection);
    
            System.out.println("Nouveaux cours générés avec succès !");
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    
}
