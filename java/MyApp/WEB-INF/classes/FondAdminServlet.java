package servlet.backoffice;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.transaction.Fond;
import servlet.authentifiacation.AdminSessionServlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import connexion.ConnexionMySQL;

@WebServlet("/fondadmin")
public class FondAdminServlet extends AdminSessionServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Créer une instance de ConnexionMySQL pour se connecter à la base de données
        this.checkSession(request, response);

        ConnexionMySQL connexionMySQL = new ConnexionMySQL();
        
        try (Connection connection = connexionMySQL.getConnectionMySQL()) {
            List<Fond> fonds = Fond.getAllForAdmin(connection);
            request.setAttribute("fonds", fonds);

            RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/backoffice/fond-admin.jsp");
            dispatcher.forward(request, response);
            
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Erreur de base de données");
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        this.checkSession(request, response);
        
        int idfond = Integer.parseInt(request.getParameter("idfond"));
        String operation = request.getParameter("operation");

        // Créer une instance de ConnexionMySQL pour se connecter à la base de données
        ConnexionMySQL connexionMySQL = new ConnexionMySQL();

        try (Connection connection = connexionMySQL.getConnectionMySQL()) {
            if ("accepter".equals(operation)) {
                Fond.updateEtat(connection, idfond, 1);
            } else if ("refuser".equals(operation)) {
                Fond.updateEtat(connection, idfond, 2);
            }

            doGet(request, response);

        } catch (SQLException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Erreur de base de données");
        }
    }
}
