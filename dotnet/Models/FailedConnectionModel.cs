using MySql.Data.MySqlClient;
using System;

public class FailedConnectionModel
{
    public static void InsertFailedLoginAttempt(int idUtilisateur, MySqlConnection mysqlConnection)
    {
        try
        {
            string query = "INSERT INTO historique_connexion (date_tentative, id_utilisateur) VALUES (@date_tentative, @id_utilisateur)";
            using (var cmd = new MySqlCommand(query, mysqlConnection))
            {
                cmd.Parameters.AddWithValue("@date_tentative", DateTime.Now);
                cmd.Parameters.AddWithValue("@id_utilisateur", idUtilisateur);

                cmd.ExecuteNonQuery();
            }
        }
        catch (Exception ex)
        {
            Console.WriteLine($"Erreur lors de l'insertion de la tentative de connexion : {ex.Message}");
        }
    }

    public static void UpdateFailedAttempts(int idUtilisateur, MySqlConnection mysqlConnection)
    {
        try
        {
            string checkQuery = "SELECT COUNT(*) FROM connexion_echoue WHERE id_utilisateur = @id_utilisateur";
            string updateQuery = "UPDATE connexion_echoue SET nombre = nombre + 1 WHERE id_utilisateur = @id_utilisateur";
            string insertQuery = "INSERT INTO connexion_echoue (nombre, id_utilisateur) VALUES (1, @id_utilisateur)";

            using (var checkCmd = new MySqlCommand(checkQuery, mysqlConnection))
            {
                checkCmd.Parameters.AddWithValue("@id_utilisateur", idUtilisateur);

                int count = Convert.ToInt32(checkCmd.ExecuteScalar());

                if (count > 0)
                {
                    using (var updateCmd = new MySqlCommand(updateQuery, mysqlConnection))
                    {
                        updateCmd.Parameters.AddWithValue("@id_utilisateur", idUtilisateur);
                        updateCmd.ExecuteNonQuery();
                    }
                }
                else
                {
                    using (var insertCmd = new MySqlCommand(insertQuery, mysqlConnection))
                    {
                        insertCmd.Parameters.AddWithValue("@id_utilisateur", idUtilisateur);
                        insertCmd.ExecuteNonQuery();
                    }
                }
            }
        }
        catch (Exception ex)
        {
            Console.WriteLine($"Erreur lors de la mise à jour du nombre de tentatives échouées : {ex.Message}");
        }
    }

    public static void ReinitialiserTentativesEchouees(int idUtilisateur, MySqlConnection mysqlConnection)
    {
        try
        {
            string query = "UPDATE connexion_echoue SET nombre = 0 WHERE id_utilisateur = @id_utilisateur";

            using (var cmd = new MySqlCommand(query, mysqlConnection))
            {
                cmd.Parameters.AddWithValue("@id_utilisateur", idUtilisateur);

                cmd.ExecuteNonQuery();
            }
        }
        catch (Exception ex)
        {
            Console.WriteLine($"Erreur lors de la réinitialisation du nombre de tentatives échouées : {ex.Message}");
        }
    }

    public static int GetFailedAttempts(int idUtilisateur, MySqlConnection mysqlConnection)
    {
        try
        {
            string query = "SELECT nombre FROM connexion_echoue WHERE id_utilisateur = @id_utilisateur";
            using (var cmd = new MySqlCommand(query, mysqlConnection))
            {
                cmd.Parameters.AddWithValue("@id_utilisateur", idUtilisateur);

                object result = cmd.ExecuteScalar();

                if (result != null && result != DBNull.Value)
                {
                    return Convert.ToInt32(result);
                }
                else
                {
                    return 0; // Aucune tentative échouée pour cet utilisateur
                }
            }
        }
        catch (Exception ex)
        {
            Console.WriteLine($"Erreur lors de la récupération du nombre de tentatives échouées : {ex.Message}");
            return -1; // Retourne -1 pour signaler une erreur dans la récupération
        }
    }
}
