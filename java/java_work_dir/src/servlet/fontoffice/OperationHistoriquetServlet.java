package servlet.fontoffice;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import java.sql.*;
import java.util.List;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

import model.crypto.AchatCrypto;
import model.crypto.Cryptomonaie;
import model.crypto.Vente;
import model.role.*;
import servlet.authentifiacation.SessionServlet;
import connexion.ConnexionMySQL; // Importer la classe de connexion


@WebServlet("/operationHistorique")

public class OperationHistoriquetServlet extends SessionServlet {

 
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        super.checkSession(request, response);
        
        
        Connection connection = null;
        try {
            connection = new ConnexionMySQL().getConnectionMySQL();
            
            if (connection == null) {
                response.getWriter().println("Connexion non établie.");
                return;
            }

            // Récupérer la liste des achats
            List<AchatCrypto> achats = AchatCrypto.readAll(connection);
            request.setAttribute("achats", achats);

             List<Vente> ventes = Vente.readAll(connection);
             request.setAttribute("ventes", ventes);
           

             List<Cryptomonaie> cryptomonaies = Cryptomonaie.readAll(connection);
             request.setAttribute("cryptomonaies", cryptomonaies);
             

             List<Utilisateur> utilisateurs = Utilisateur.readAll();
             request.setAttribute("utilisateurs", utilisateurs);
 
            // Dispatcher vers la page JSP
            RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/fontoffice/operation.jsp");
            dispatcher.forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().println("Erreur lors de la récupération des achats : " + e.getMessage());
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        super.checkSession(request, response);
        

        Connection connection = null;

        
        String dateMinStr = request.getParameter("date_min"); // Date de début
        String dateMaxStr = request.getParameter("date_max"); // Date de fin


        LocalDateTime dateMin = null;
        LocalDateTime dateMax = null;

        int crypto = Integer.parseInt(request.getParameter("cryptomonaie")); // Tous ou crypto1...crypto10
        int user = Integer.parseInt(request.getParameter("utilisateur")); // Tous ou crypto1...crypto10


        // Si des dates sont spécifiées, les convertir en LocalDateTime
        if (dateMinStr != null && !dateMinStr.isEmpty()) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
            dateMin = LocalDateTime.parse(dateMinStr, formatter);
        }
        if (dateMaxStr != null && !dateMaxStr.isEmpty()) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
            dateMax = LocalDateTime.parse(dateMaxStr, formatter);
        }

        try  {
            connection = new ConnexionMySQL().getConnectionMySQL();
            

            List<AchatCrypto> achats = AchatCrypto.readAllFiltrer(connection,dateMin,dateMax,crypto,user);
            request.setAttribute("achats", achats);

             List<Vente> ventes = Vente.readAllFiltrer(connection,dateMin,dateMax,crypto,user);
             request.setAttribute("ventes", ventes);

             List<Cryptomonaie> cryptomonaies = Cryptomonaie.readAll(connection);
             request.setAttribute("cryptomonaies", cryptomonaies);
             

             List<Utilisateur> utilisateurs = Utilisateur.readAll();
             request.setAttribute("utilisateurs", utilisateurs);
        
     
             RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/fontoffice/operation.jsp");
             dispatcher.forward(request, response);
    }

    catch (Exception e) {
        e.printStackTrace();
        response.getWriter().println("Erreur lors de la récupération des achats : " + e.getMessage());
    } finally {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
}
