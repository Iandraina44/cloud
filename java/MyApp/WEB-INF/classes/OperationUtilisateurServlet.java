package servlet.fontoffice;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import model.crypto.AchatCrypto;
import model.crypto.Vente;

import com.google.gson.Gson;
import java.io.*;
import java.sql.*;
import java.util.*;
import connexion.ConnexionMySQL;

@WebServlet("/operationUtilisateur")
public class OperationUtilisateurServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int idUtilisateur = Integer.parseInt(request.getParameter("id"));

        // Connexion à la base de données
        ConnexionMySQL connexionMySQL = new ConnexionMySQL();
        try (Connection connection = connexionMySQL.getConnectionMySQL()) {

            // Récupérer l'historique des achats et des ventes pour cet utilisateur
            List<AchatCrypto> achats = AchatCrypto.readAllByUser(connection, idUtilisateur);
            List<Vente> ventes = Vente.readAllByUser(connection, idUtilisateur);

            // Créer un objet contenant les deux listes (achats et ventes)
            Map<String, Object> responseMap = new HashMap<>();
            responseMap.put("achats", achats);
            responseMap.put("ventes", ventes);

            // Convertir l'objet en JSON
            Gson gson = new Gson();
            String jsonResponse = gson.toJson(responseMap);

            // Définir le type de contenu de la réponse comme JSON
            response.setContentType("application/json");
            response.getWriter().write(jsonResponse);

        } catch (SQLException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Erreur de base de données");
        }
    }
}
