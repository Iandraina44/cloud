using MySql.Data.MySqlClient;
using System;

namespace connexion.Models
{
    public class ConnexionMysql
    {
        public static MySqlConnection GetConnexion()
        {
            MySqlConnection connexion = null;
            try
            {
                // Définir la chaîne de connexion MySQL
                string connectionString = "Server=localhost;Port=3306;Database=cloud;User=root;Password=root;";

                // Créer la connexion
                connexion = new MySqlConnection(connectionString);

                // Ouvrir la connexion
                connexion.Open();
            }
            catch (Exception ex)
            {
                Console.WriteLine("Erreur de connexion: " + ex.Message);
            }
            return connexion;
        }
    }
}
