package servlet.backoffice;

import java.io.*;
import java.sql.*;
import java.text.*;
import java.time.*;
import java.time.format.*;
import java.util.List;

import connexion.ConnexionMySQL;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import model.crypto.PortefeuilleAlea;

@WebServlet("/adminportefeuille")
public class PortefeuilleServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Récupérer la date de l'input datetime-local (ex: "2025-02-07T14:30")
        String dateMaxString = request.getParameter("dateMax"); // L'input de type datetime-local dans votre page HTML

        if (dateMaxString != null && !dateMaxString.isEmpty()) {
            // Formater la chaîne "yyyy-MM-dd'T'HH:mm" en "yyyy-MM-dd HH:mm:ss"
            try {
                // Définir le format pour l'input datetime-local
                DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

                LocalDateTime dateMax = LocalDateTime.parse(dateMaxString, inputFormatter);

                // Formater en "yyyy-MM-dd HH:mm:ss" pour MySQL
                DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                String formattedDateMax = dateMax.format(outputFormatter);

                // Connectez-vous à la base de données et appelez la méthode
                Connection conn = ConnexionMySQL.getConnectionMySQL();

                // Appelez la méthode pour récupérer les résultats avec la date formatée
                List<PortefeuilleAlea> portefeuilles = PortefeuilleAlea.getAllByDateEtHeureMax(conn, formattedDateMax);

                // Vous pouvez ensuite transmettre les résultats à la page JSP ou afficher directement
                request.setAttribute("portefeuilles", portefeuilles);
                
                doGet(request, response);

            } catch (DateTimeParseException e) {
                // Si la date n'est pas valide, gérer l'erreur
                response.getWriter().write("Erreur : format de date invalide.");
            }
        } else {
            response.getWriter().write("Erreur : paramètre 'dateMax' manquant.");
        }
    }
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/views/backoffice/portefeuille.jsp").forward(req, resp);
    }
}
