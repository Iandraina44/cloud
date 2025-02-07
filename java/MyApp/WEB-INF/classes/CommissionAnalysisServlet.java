package servlet.backoffice;

import model.crypto.AchatCrypto;
import model.crypto.Cryptomonaie;
import model.crypto.Vente;
import servlet.authentifiacation.AdminSessionServlet;
import connexion.ConnexionMySQL;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.annotation.WebServlet;


import java.io.*;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@WebServlet("/analyseCommission")

public class CommissionAnalysisServlet extends AdminSessionServlet {


    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        checkSession(request, response);

        // Créer une instance de ConnexionMySQL pour se connecter à la base de données
        ConnexionMySQL connexionMySQL = new ConnexionMySQL();
        
        try (Connection connection = connexionMySQL.getConnectionMySQL()) {
            // Récupérer les cryptomonnaies depuis la base de données
            List<Cryptomonaie> cryptomonaies = Cryptomonaie.readAll(connection);
            // Définir les attributs pour la vue JSP
            request.setAttribute("cryptomonaies", cryptomonaies);

          
            RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/backoffice/comission-statistique.jsp");
            dispatcher.forward(request, response);
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Erreur de base de données");
        }
    }

  
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        checkSession(request, response);

        String typeAnalyse = request.getParameter("type_analyse"); // Somme ou Moyenne
        String typeCommission = request.getParameter("type_commission"); // Vente ou Achat
        String cryptoStr = request.getParameter("crypto") ; // Tous ou crypto1...crypto10

        int crypto =-1; // Tous ou crypto1...crypto10
        String dateMinStr = request.getParameter("date_min"); // Date de début
        String dateMaxStr = request.getParameter("date_max"); // Date de fin

        if(cryptoStr!=null && !cryptoStr.isEmpty()){
            crypto = Integer.parseInt(cryptoStr);
        }

        LocalDateTime dateMin = null;
        LocalDateTime dateMax = null;

        // Si des dates sont spécifiées, les convertir en LocalDateTime
        if (dateMinStr != null && !dateMinStr.isEmpty()) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
            dateMin = LocalDateTime.parse(dateMinStr, formatter);
        }
        if (dateMaxStr != null && !dateMaxStr.isEmpty()) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
            dateMax = LocalDateTime.parse(dateMaxStr, formatter);
        }

        double result = 0;
        try (Connection connection = new ConnexionMySQL().getConnectionMySQL()) {
            // Si l'analyse est sur la somme
            if ("Somme".equalsIgnoreCase(typeAnalyse)) {
                if(typeCommission.equals("achat")){
                    result = AchatCrypto.calculateTotalCommission(connection, crypto, dateMin, dateMax);
                }

                if(typeCommission.equals("vente")){
                    result = Vente.calculateTotalCommission(connection, crypto, dateMin, dateMax);
                }
            } 
            // Si l'analyse est sur la moyenne
            else if ("Moyenne".equalsIgnoreCase(typeAnalyse)) {
                if(typeCommission.equals("achat")){
                    result = AchatCrypto.calculateMoyenneCommission(connection, crypto, dateMin, dateMax);
                }
                if(typeCommission.equals("vente")){
                    result = Vente.calculateTotalCommission(connection, crypto, dateMin, dateMax);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("error", "Erreur lors de l'analyse des commissions : " + e.getMessage());
        }

        // Passer le résultat à la page JSP
        request.setAttribute("result", result);
        doGet(request, response);
       // request.getRequestDispatcher("analyse_commission.jsp").forward(request, response);
    }

}
