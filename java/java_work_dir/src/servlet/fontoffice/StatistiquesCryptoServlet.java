package servlet.fontoffice;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import model.crypto.CoursCrypto;
import model.crypto.Cryptomonaie;
import servlet.authentifiacation.SessionServlet;

import java.io.*;
import java.sql.*;
import java.util.*;
import connexion.ConnexionMySQL;

@WebServlet("/statistiquesCrypto")
public class StatistiquesCryptoServlet extends SessionServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        super.checkSession(request, response);

        ConnexionMySQL connexionMySQL = new ConnexionMySQL();
        try (Connection connection = connexionMySQL.getConnectionMySQL()) {
            List<Cryptomonaie> cryptomonaies = Cryptomonaie.readAll(connection);
            request.setAttribute("cryptomonaies", cryptomonaies);
            RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/fontoffice/statistique.jsp");
            dispatcher.forward(request, response);
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Erreur de base de données");
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
       
        super.checkSession(request, response);


        String[] analyses = request.getParameterValues("analyse");
        String[] cryptos = request.getParameterValues("crypto");
        String dateMin = request.getParameter("dateMin");
        String dateMax = request.getParameter("dateMax");

        List<String> resultats = new ArrayList<>();
        ConnexionMySQL connexionMySQL = new ConnexionMySQL();
        
        try (Connection connection = connexionMySQL.getConnectionMySQL()) {
            for (String cryptoId : cryptos) {
                int idCrypto = Integer.parseInt(cryptoId);
                Cryptomonaie crypto = Cryptomonaie.getById(connection,idCrypto);
                String cryptoName = crypto.getNom();
                for (String analyse : analyses) {
                    double resultat = switch (analyse) {
                        case "min" -> CoursCrypto.getMin(connection, idCrypto, dateMin, dateMax);
                        case "max" -> CoursCrypto.getMax(connection, idCrypto, dateMin, dateMax);
                        case "moyenne" -> CoursCrypto.getMoyenne(connection, idCrypto, dateMin, dateMax);
                        case "ecart_type" -> CoursCrypto.getEcartType(connection, idCrypto, dateMin, dateMax);
                        case "q1" -> CoursCrypto.getPremierQuartile(connection, idCrypto, dateMin, dateMax);
                        default -> 0;
                    };
                    resultats.add(analyse + " pour " + cryptoName + " : " + resultat);
                }
            }
            List<Cryptomonaie> cryptomonaies = Cryptomonaie.readAll(connection);
            request.setAttribute("cryptomonaies", cryptomonaies);
            request.setAttribute("resultats", resultats);
            RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/fontoffice/statistique.jsp");
            dispatcher.forward(request, response);
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
