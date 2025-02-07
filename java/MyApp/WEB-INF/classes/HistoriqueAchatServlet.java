package servlet.backoffice;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import java.sql.*;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

import model.crypto.AchatCrypto;
import servlet.authentifiacation.AdminSessionServlet;
import connexion.ConnexionMySQL; // Importer la classe de connexion


@WebServlet("/historiqueAchat")

public class HistoriqueAchatServlet extends AdminSessionServlet {

 
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        checkSession(request, response);
        
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

            // Dispatcher vers la page JSP
            RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/backoffice/liste-achat.jsp");
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
}
