
package servlet.backoffice;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.crypto.Vente;
import servlet.authentifiacation.AdminSessionServlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import connexion.ConnexionMySQL;

@WebServlet("/listeventes")
public class ListVenteServlet extends AdminSessionServlet {
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        checkSession(request, response);
        
        ConnexionMySQL connexionMySQL = new ConnexionMySQL();
        
        try (Connection connection = connexionMySQL.getConnectionMySQL()) {
            List<Vente> ventes = Vente.readAll(connection);
            request.setAttribute("ventes", ventes);
            RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/backoffice/liste-vente.jsp");
            dispatcher.forward(request, response);
            
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Erreur de base de données");
        }
    }
}
