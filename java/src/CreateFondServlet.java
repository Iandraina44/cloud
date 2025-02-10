import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import connexion.ConnexionMySQL;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;

@WebServlet("/createFond")
public class CreateFondServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

            try {
                // Récupérer les paramètres nécessaires pour le dépôt
                double valeurFond = Double.parseDouble(request.getParameter("valeurFond"));
                Timestamp dateFond = Timestamp.valueOf(request.getParameter("dateFond"));
                int idUtilisateur = Integer.parseInt(request.getParameter("idUtilisateur"));
                int statut = Integer.parseInt(request.getParameter("statut"));
                
                // Utiliser ConnexionMySQL pour obtenir une connexion
                ConnexionMySQL connexionMySQL = new ConnexionMySQL();
                try (Connection connection = connexionMySQL.getConnectionMySQL()) {
                                        
                }
            } catch (SQLException e) {
                e.printStackTrace();
                out.write("{\"success\": false, \"message\": \"Database error: " + e.getMessage() + "\"}");
            } catch (Exception e) {
                e.printStackTrace();
                out.write("{\"success\": false, \"message\": \"Invalid input: " + e.getMessage() + "\"}");
            }
    }
}
