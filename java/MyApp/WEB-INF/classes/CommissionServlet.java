package servlet.backoffice;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import model.transaction.Commission;
import servlet.authentifiacation.AdminSessionServlet;

import java.io.*;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import connexion.ConnexionMySQL;

@WebServlet("/commission")
public class CommissionServlet extends AdminSessionServlet {

   protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        this.checkSession(request, response);

        // Récupérer les valeurs du formulaire
        String typeCommission = request.getParameter("typeCommission");
        double valeur = Double.parseDouble(request.getParameter("valeur"));
        String dateStr = request.getParameter("dateModification");

        // Utiliser un format qui correspond à la date reçue
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        LocalDateTime dateModification = LocalDateTime.parse(dateStr, formatter);
        
        // Connexion à la base de données
        ConnexionMySQL connexionMySQL = new ConnexionMySQL();

        try (Connection connection = connexionMySQL.getConnectionMySQL()) {
            // Créer un objet Commission avec les valeurs saisies
            Commission commission = new Commission();
            commission.setTypeCommission(typeCommission);
            commission.setValeur(valeur);
            commission.setDateModification(dateModification);

            // Insérer la commission dans la base de données
            commission.insertCommission(connection);

            // Envoyer un message de succès
            request.setAttribute("message", "Commission ajoutée avec succès !");
            doGet(request, response);
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Erreur de base de données");
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        this.checkSession(request, response);
        

        // Connexion à la base de données
        ConnexionMySQL connexionMySQL = new ConnexionMySQL();
       

        try (Connection connection = connexionMySQL.getConnectionMySQL()) {
            List<Commission> commissionsVente = Commission.readCommission(connection, "vente");
            List<Commission> commissionsAchat = Commission.readCommission(connection, "achat");
            Commission actuelVente= Commission.getClosestCommissionToNow(connection,"vente");
            Commission actuelAchat= Commission.getClosestCommissionToNow(connection,"achat");



            // Envoyer la liste des commissions à la JSP
            request.setAttribute("commissionsVente", commissionsVente);
            request.setAttribute("commissionsAchat", commissionsAchat);

            request.setAttribute("actuelVente", actuelVente);
            request.setAttribute("actuelAchat", actuelAchat);

            // Rediriger vers la page de gestion des commissions
            RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/backoffice/comission.jsp");
            dispatcher.forward(request, response);

        } catch (SQLException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Erreur de base de données");
        }
    }
}
