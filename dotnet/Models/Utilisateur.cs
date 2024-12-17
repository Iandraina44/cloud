using MySql.Data.MySqlClient;
using System;
using System.Text;
using BCrypt.Net;
namespace user.Models
{
    public class Utilisateur
    {
        // Propriétés
        public int IdUtilisateur { get; set; }
        public string Email { get; set; }
        public string Mdp { get; set; }

        // Constructeur avec paramètres
        public Utilisateur(int idUtilisateur, string email, string mdp)
        {
            IdUtilisateur = idUtilisateur;
            Email = email;
            Mdp = mdp;
        }

         public Utilisateur() { }


     

    // Fonction pour hacher un mot de passe
        public static string HashPassword(string password)
        {
            // Génère un hash sécurisé avec un "salt" intégré
            return BCrypt.Net.BCrypt.HashPassword(password);
        }

     
       
        public void Insert(MySqlConnection connection)
        {
            try
            {
                string mdp_hache= HashPassword(Mdp);
                string query = "INSERT INTO utilisateur (email, mdp) VALUES (@Email, @Mdp)";

                using (var command = new MySqlCommand(query, connection))
                {
                    command.Parameters.AddWithValue("@Email", Email);
                    command.Parameters.AddWithValue("@Mdp", mdp_hache);

                    command.ExecuteNonQuery();
                }
            }
            catch (Exception ex)
            {
                Console.WriteLine("Erreur lors de l'insertion de l'utilisateur: " + ex.Message);
            }
        }

      
        public static Utilisateur SelectById(int id, MySqlConnection connection)
        {
            try
            {
                string query = "SELECT * FROM utilisateur WHERE id_utilisateur = @Id";

                using (var command = new MySqlCommand(query, connection))
                {
                    command.Parameters.AddWithValue("@Id", id);

                    using (var reader = command.ExecuteReader())
                    {
                        if (reader.Read())
                        {
                            return new Utilisateur(
                                reader.GetInt32("id_utilisateur"),
                                reader.GetString("email"),
                                reader.GetString("mdp")
                            );
                        }
                    }
                }
            }
            catch (Exception ex)
            {
                Console.WriteLine("Erreur lors de la récupération de l'utilisateur: " + ex.Message);
            }

            return null; // Retourne null si aucune donnée n'est trouvée
        }



        public static string Login(string email, string password, MySqlConnection connection)
        {
            try
            {
                // Requête pour récupérer le mot de passe haché associé à l'email donné
                string query = "SELECT mdp FROM utilisateur WHERE email = @Email";

                using (var command = new MySqlCommand(query, connection))
                {
                    command.Parameters.AddWithValue("@Email", email);

                    using (var reader = command.ExecuteReader())
                    {
                        if (reader.Read())
                        {
                            // Récupère le mot de passe haché depuis la base de données
                            string hashedPassword = reader.GetString("mdp");

                            // Vérifie si le mot de passe donné correspond au hash
                            if (BCrypt.Net.BCrypt.Verify(password, hashedPassword))
                            {
                                return GenerateToken(16); // Mot de passe correct
                            }
                        }
                    }
                }
            }
            catch (Exception ex)
            {
                Console.WriteLine("Erreur lors de la connexion: " + ex.Message);
            }

            return null; // Retourne false si l'email ou le mot de passe est incorrect
        }


         public static string GenerateToken(int length )
        {
            const string chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
            var random = new Random();
            var token = new StringBuilder();

            for (int i = 0; i < length; i++)
            {
                token.Append(chars[random.Next(chars.Length)]);
            }

            return token.ToString();
        }

    }
}
