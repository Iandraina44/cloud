package servlet.fontoffice;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import java.io.*;
import java.sql.*;
import java.util.List;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.crypto.AchatCrypto;
import model.crypto.Cryptomonaie;
import model.crypto.Vente;
import model.role.*;
import model.transaction.Commission;
import model.transaction.Fond;
import servlet.authentifiacation.SessionServlet;
import connexion.ConnexionMySQL; // Importer la classe de connexion

import java.time.Instant;

@WebServlet("/achat")
public class AchatCryptoServlet extends SessionServlet {

   protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        super.checkSession(request, response);
        Utilisateur utilisateur = (Utilisateur) request.getSession().getAttribute("user");
        int idUtilisateur = utilisateur.getIdUtilisateur();

        response.getWriter().println("ato mandeha ohhhhh");


        // Récupérer la cryptomonnaie sélectionnée et la quantité à vendre
        int idCryptomonaie = Integer.parseInt(request.getParameter("cryptomonaie"));
        int quantite = Integer.parseInt(request.getParameter("quantite"));

        // Créer une instance de ConnexionMySQL pour se connecter à la base de données
        ConnexionMySQL connexionMySQL = new ConnexionMySQL();
        
        // Créer un objet Vente et insérer la vente dans la base de données
        try (Connection connection = connexionMySQL.getConnectionMySQL()) {
            AchatCrypto achat = new AchatCrypto();
            achat.setIdCryptomonaie(idCryptomonaie);
            achat.setQuantite(quantite);
            achat.setDateAchat(new Timestamp(System.currentTimeMillis()));
            achat.setUtilisateur(new Utilisateur(idUtilisateur,"","","")); // Utilisateur ID est supposé être 1 pour l'instant
          // Utilisateur ID est supposé être 1 pour l'instant
            Commission commissionAchat= Commission.getClosestCommissionToNow(connection,"achat");
           



            // Vérifier si l'utilisateur dispose de fonds suffisants pour effectuer la vente
            if (achat.hasSufficientFunds(connection)) {

                 PrintWriter out = response.getWriter();

                out.println("ato");
               
                // Obtenir le dernier prix de la cryptomonnaie sélectionnée
                double prixUnitaire = achat.getLatestCryptoPrice(connection);
                achat.setPrixUnitaire(prixUnitaire);

                out.println(achat.getPrixUnitaire());



                double totalAchat = (quantite * prixUnitaire);
                double valeurCommission =  totalAchat * (commissionAchat.getValeur()/100);
                out.println(commissionAchat.getValeur());

                double total =  totalAchat + valeurCommission;
                achat.setPrixWCommission(valeurCommission);  
                out.println(achat.getPrixWCommission());

                achat.create(connection);

                out.println(achat.getPrixWCommission());

                Timestamp dateFond = Timestamp.from(Instant.now());
                Fond f = new Fond(-1*total, dateFond, achat.getUtilisateur().getIdUtilisateur());
                f.createvalide(connection);

                out.println("fond inséré");

                request.setAttribute("message", "Achat effectué avec succès. Quantité de crypto-monnaie achetée: " + quantite);               
               
                doGet(request, response);
                // response.sendRedirect(request.getContextPath() + "/achat");
            
            } 
                else {
            
                boolean ampy= achat.hasSufficientFunds(connection);
                request.setAttribute("message", "fond insuffisant");

                doGet(request, response);
                // response.sendRedirect(request.getContextPath() + "/achat");            
    
            }
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Erreur de base de données");

        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        super.checkSession(request, response);
        Utilisateur utilisateur = (Utilisateur) request.getSession().getAttribute("user");
        int idUtilisateur = utilisateur.getIdUtilisateur();

        // Créer une instance de ConnexionMySQL pour se connecter à la base de données
        ConnexionMySQL connexionMySQL = new ConnexionMySQL();
        
        try (Connection connection = connexionMySQL.getConnectionMySQL()) {
            // Récupérer les cryptomonnaies depuis la base de données
            List<Cryptomonaie> cryptomonaies = Cryptomonaie.readAll(connection);
            // Définir les attributs pour la vue JSP
            request.setAttribute("cryptomonaies", cryptomonaies);

            // Calculer le fond total pour l'utilisateur (idUtilisateur = 1)
            double fond = Cryptomonaie.calculateTotalFondByUser(connection, 1);
            request.setAttribute("fond", fond);

            // Récupérer les informations du portefeuille pour l'utilisateur (idUtilisateur = 1)
            List<Vente> ventes = Vente.getPortefeuilleByUtilisateur(connection, 1);
            request.setAttribute("ventes", ventes);  // Passer les ventes à la JSP

            // Forward vers le formulaire de vente
            RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/fontoffice/achat.jsp");
            dispatcher.forward(request, response);

        } catch (SQLException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Erreur de base de données");
        }
    }

}
