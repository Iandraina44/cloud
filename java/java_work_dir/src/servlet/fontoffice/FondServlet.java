package servlet.fontoffice;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;


import model.role.Utilisateur;
import model.transaction.Fond;
import model.transaction.Notification;
import servlet.authentifiacation.SessionServlet;
import connexion.ConnexionMySQL;

@WebServlet("/fond")
public class FondServlet extends SessionServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
       
       super.checkSession(request, response);

        // Créer une instance de ConnexionMySQL pour se connecter à la base de données
        ConnexionMySQL connexionMySQL = new ConnexionMySQL();
        // int idUtilisateur = 1; // Remplacez par l'ID utilisateur réel
        
        Utilisateur utilisateur = (Utilisateur) request.getSession().getAttribute("user");
        int idUtilisateur = utilisateur.getIdUtilisateur();

        try (Connection connection = connexionMySQL.getConnectionMySQL()) {
            double fondTotal= Fond.totalfond(connection,idUtilisateur);
            List<Fond> fonds= Fond.getByIdUtilisateur(connection,idUtilisateur);
            request.setAttribute("fonds", fonds);
            request.setAttribute("fondTotal", fondTotal);

            RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/fontoffice/fond.jsp");
            dispatcher.forward(request, response);
            
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Erreur de base de données");
        }
    }
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        super.checkSession(request, response);

        String type = request.getParameter("operation");
        double valeur = Double.parseDouble(request.getParameter("montant"));

        // int idUtilisateur = 1; // Remplacez par l'ID utilisateur réel

        Utilisateur utilisateur = (Utilisateur) request.getSession().getAttribute("user");
        int idUtilisateur = utilisateur.getIdUtilisateur();
        

        // Récupération de la date à partir du formulaire
        String dateString = request.getParameter("date");

        // Conversion de la chaîne de caractères en objet LocalDate
        LocalDate localDate = LocalDate.parse(dateString);

        // Conversion en Timestamp
        Timestamp timestamp = Timestamp.valueOf(localDate.atStartOfDay());

        // Ou si tu veux obtenir un Timestamp avec l'heure courante
        Timestamp dateFond = Timestamp.from(Instant.now());
      
        String message="votre demande de "+valeur+" Ar du"+dateFond+" est en attente de confirmation";
      
        Notification notif=new Notification(dateFond,message,idUtilisateur,0);
        
        Fond fond = new Fond(valeur, dateFond, idUtilisateur);
        

        ConnexionMySQL connexionMySQL = new ConnexionMySQL();
        try (Connection connection = connexionMySQL.getConnectionMySQL()) {
            // notif.create(connection);
            double fondTotal= Fond.totalfond(connection,idUtilisateur);

            if("retrait".equals(type) && valeur > fondTotal){
                double manque=valeur - fondTotal; 
                notif.setMessage("votre demande de "+valeur+" Ar du"+dateFond+" a ete refuse a cause de manque de fond d une valeur de "+manque);
                notif.create(connection); 
            }

            if ("retrait".equals(type) && valeur <= fondTotal) {
                valeur *= -1;
                fond.setValeurFond(valeur);
                fond.create(connection);
            }

            else if("depot".equals(type)){
                fond.create(connection);
            }
            List<Fond> fonds= Fond.getByIdUtilisateur(connection,idUtilisateur);
            request.setAttribute("fonds", fonds);
            request.setAttribute("fondTotal", fondTotal);

            // RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/fontoffice/fond.jsp");
            // dispatcher.forward(request, response);
            response.sendRedirect(request.getContextPath() + "/fond");

        } catch (SQLException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Erreur de base de données");
        }
    }
}
