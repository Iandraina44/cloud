package servlet.authentifiacation;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.role.Utilisateur;

@WebServlet("/login")
public class LoginSessionServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int idUser=Integer.parseInt(req.getParameter("user_id"));
        Utilisateur utilisateur=Utilisateur.read(idUser);
        System.out.println(utilisateur.getEmail());
        req.getSession().setAttribute("user", utilisateur);

        // req.getRequestDispatcher("/WEB-INF/views/fontoffice/crypto_chart.jsp").forward(req, resp);
        resp.sendRedirect(req.getContextPath()+"/graphcrypto");
    }
}
