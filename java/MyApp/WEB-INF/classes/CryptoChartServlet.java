package servlet.fontoffice;

import connexion.ConnexionMySQL;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.crypto.CoursCrypto;
import util.Util;  // Importer la classe Util

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

@WebServlet("/crypto-chart")
public class CryptoChartServlet extends HttpServlet {
    private ConnexionMySQL connexionMySQL;

    @Override
    public void init() throws ServletException {
        // Initialisation de l'objet de connexion
        connexionMySQL = new ConnexionMySQL();

        // Configuration du Timer pour générer des valeurs toutes les 10 secondes
        Timer timer = new Timer(true);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                try (Connection connection = connexionMySQL.getConnectionMySQL()) {
                    CoursCrypto.generateNewCours(connection);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }, 0, 10_000); // Exécution toutes les 10 secondes
    }

   
    @Override
protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    resp.setContentType("application/json");
    try (Connection connection = connexionMySQL.getConnectionMySQL()) {
        List<List<CoursCrypto>> groupedCours = CoursCrypto.getCoursCryptoGroupesParCryptomonnaie(connection);

        StringBuilder jsonBuilder = new StringBuilder("[");
        for (List<CoursCrypto> coursList : groupedCours) {
            jsonBuilder.append("["); // Nouvelle courbe
            for (CoursCrypto cours : coursList) {
                String valeurFormatee = Util.formatDecimal(cours.getValeurCour());
                jsonBuilder.append(String.format(
                    "{\"cryptoId\": %d, \"valeur\": \"%s\", \"date\": \"%s\"},",
                    cours.getIdCryptomonaie(),
                    valeurFormatee,
                    cours.getDateCour()
                ));
            }
            if (jsonBuilder.charAt(jsonBuilder.length() - 1) == ',') {
                jsonBuilder.setLength(jsonBuilder.length() - 1); // Retirer la dernière virgule
            }
            jsonBuilder.append("],"); // Fin de la courbe
        }
        if (jsonBuilder.charAt(jsonBuilder.length() - 1) == ',') {
            jsonBuilder.setLength(jsonBuilder.length() - 1); // Retirer la dernière virgule
        }
        jsonBuilder.append("]");

        resp.getWriter().write(jsonBuilder.toString());
    } catch (SQLException e) {
        resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        resp.getWriter().write("{\"error\": \"Database error\"}");
    }
}

}
