package model.role;

import java.sql.*;

public class Admin {
    private int idAdmin;
    private String username;

    public Admin(int idAdmin, String username) {
        this.idAdmin = idAdmin;
        this.username = username;
    }

    public int getIdAdmin() {
        return idAdmin;
    }

    public String getUsername() {
        return username;
    }


    public static Admin login(String username, String password,Connection connection) throws Exception {
        try {
            String query = "SELECT id_admin, username FROM admin_t WHERE username = ? AND mdp = SHA1(?)";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();

            if (!rs.next()) {
                throw new Exception("Invalid username or password");
            }
            return new Admin(rs.getInt("id_admin"), rs.getString("username"));
        } catch (SQLException e) {
            e.printStackTrace();
            throw new Exception("Database error");
        }
    }

    public static Admin register(String username, String password,Connection connection) throws Exception {
        try {
            String query = "INSERT INTO admin_t (username, mdp) VALUES (?, SHA1(?))";
            PreparedStatement stmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, username);
            stmt.setString(2, password);
            int rowsInserted = stmt.executeUpdate();

            if (rowsInserted > 0) {
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    return new Admin(rs.getInt(1), username);
                }
            }
            return null;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new Exception("Database error");
        }
    }
}
