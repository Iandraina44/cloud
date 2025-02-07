package servlet.authentifiacation;

import java.io.IOException;

import connexion.ConnexionMySQL;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.role.Admin;

@WebServlet("/admin")
public class AdminLoginServlet extends HttpServlet{
    
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username=req.getParameter("username");
        String mdp=req.getParameter("mdp");

        try {
            Admin admin=Admin.login(username, mdp,ConnexionMySQL.getConnectionMySQL());
            req.getSession().setAttribute("admin",admin );
            System.out.println("logged prrr");
            // req.getRequestDispatcher("fond-admin.jsp").forward(req, resp);
            resp.sendRedirect(req.getContextPath()+"/commission");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            req.setAttribute("error", e.getMessage());
            req.getRequestDispatcher("admin-login.jsp").forward(req, resp);
        }

    }
}
