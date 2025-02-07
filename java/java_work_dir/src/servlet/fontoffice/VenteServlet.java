package servlet.fontoffice;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.*;
import java.sql.*;
import java.util.List;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

import model.crypto.Cryptomonaie;
import model.crypto.Vente;
import model.role.*;
import model.transaction.Commission;
import model.transaction.Fond;
import servlet.authentifiacation.SessionServlet;
import connexion.ConnexionMySQL; // Importer la classe de connexion

import java.time.Instant;

@WebServlet("/vente")
public class VenteServlet extends SessionServlet {

    // Gérer la demande GET pour afficher le formulaire
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
       super.checkSession(request, response);

        // Créer une instance de ConnexionMySQL pour se connecter à la base de données
        ConnexionMySQL connexionMySQL = new ConnexionMySQL();

        Utilisateur utilisateur = (Utilisateur) request.getSession().getAttribute("user");
        int idUtilisateur = utilisateur.getIdUtilisateur();
        
        try (Connection connection = connexionMySQL.getConnectionMySQL()) {
            // Récupérer les cryptomonnaies depuis la base de données
            List<Cryptomonaie> cryptomonaies = Cryptomonaie.readAll(connection);
            // Définir les attributs pour la vue JSP
            request.setAttribute("cryptomonaies", cryptomonaies);

            // Calculer le fond total pour l'utilisateur (idUtilisateur = 1)
            double fond = Cryptomonaie.calculateTotalFondByUser(connection, idUtilisateur);
            request.setAttribute("fond", fond);

            // Récupérer les informations du portefeuille pour l'utilisateur (idUtilisateur = 1)
            List<Vente> ventes = Vente.getPortefeuilleByUtilisateur(connection, idUtilisateur);
            request.setAttribute("ventes", ventes);  // Passer les ventes à la JSP

            // Forward vers le formulaire de vente
            RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/fontoffice/vente.jsp");
            dispatcher.forward(request, response);
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Erreur de base de données");
        }
    }

    // Gérer la soumission du formulaire (POST)
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {


       super.checkSession(request, response);

        Utilisateur utilisateur = (Utilisateur) request.getSession().getAttribute("user");
        int idUtilisateur = utilisateur.getIdUtilisateur();

        // Récupérer la cryptomonnaie sélectionnée et la quantité à vendre
        int idCryptomonaie = Integer.parseInt(request.getParameter("cryptomonaie"));
        int quantite = Integer.parseInt(request.getParameter("quantite"));

        // Créer une instance de ConnexionMySQL pour se connecter à la base de données
        ConnexionMySQL connexionMySQL = new ConnexionMySQL();
        
        // Créer un objet Vente et insérer la vente dans la base de données
        try (Connection connection = connexionMySQL.getConnectionMySQL()) {
            Vente vente = new Vente();
            vente.setIdCryptomonaie(idCryptomonaie);
            vente.setQuantite(quantite);
            vente.setDateVente(new Timestamp(System.currentTimeMillis()));
            vente.setUtilisateur(new Utilisateur(idUtilisateur,"","","")); // Utilisateur ID est supposé être 1 pour l'instant

            // Vérifier si l'utilisateur dispose de fonds suffisants pour effectuer la vente
            if (vente.hasSufficientFunds(connection)) {
                // Obtenir le dernier prix de la cryptomonnaie sélectionnée
                double prixUnitaire = vente.getLatestCryptoPrice(connection);
                vente.setPrixUnitaire(prixUnitaire);
               
                double totalVente = quantite * prixUnitaire;
                Commission commissionVente= Commission.getClosestCommissionToNow(connection,"vente");

                double valeurCommission =  totalVente * (commissionVente.getValeur()/100);
                double total =  totalVente - valeurCommission;

                vente.setPrixWCommission(valeurCommission);
                 
                vente.create(connection);

                Timestamp dateFond = Timestamp.from(Instant.now());
                Fond f = new Fond(total, dateFond, vente.getUtilisateur().getIdUtilisateur());
                f.createvalide(connection);

                List<Cryptomonaie> cryptomonaies = Cryptomonaie.readAll(connection);
                request.setAttribute("cryptomonaies", cryptomonaies);
                
                double fond = Cryptomonaie.calculateTotalFondByUser(connection, idUtilisateur);
                request.setAttribute("fond", fond);

                // Récupérer les informations du portefeuille pour l'utilisateur (idUtilisateur = 1)
                List<Vente> ventes = Vente.getPortefeuilleByUtilisateur(connection, idUtilisateur);
                request.setAttribute("ventes", ventes);

                response.sendRedirect(request.getContextPath() + "/vente");

            } else {
                boolean ampy= vente.hasSufficientFunds(connection);
                request.setAttribute("ampy", ampy);
                request.setAttribute("qt", quantite);
                // Rediriger vers une page d'erreur si les fonds sont insuffisants
                RequestDispatcher dispatcher = request.getRequestDispatcher("error.jsp");
                dispatcher.forward(request, response);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Erreur de base de données");
        }
    }
}
