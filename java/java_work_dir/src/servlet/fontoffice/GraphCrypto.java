package servlet.fontoffice;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import servlet.authentifiacation.SessionServlet;

@WebServlet("/graphcrypto")
public class GraphCrypto extends SessionServlet{
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        checkSession(req, resp);
        req.getRequestDispatcher("/WEB-INF/views/fontoffice/crypto_chart.jsp").forward(req, resp);
    }
}
