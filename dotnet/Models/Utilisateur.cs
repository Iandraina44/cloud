using MySql.Data.MySqlClient;
using System;
using System.Text;
using BCrypt.Net;
using email.Models;
using auth.Models;

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

        public static string GenerateFourDigitCode()
        {
            var random = new Random();
            int code = random.Next(0, 10000); // Génère un entier aléatoire entre 0 et 9999
            return code.ToString("D4"); // Formate le nombre en 4 chiffres avec des zéros initiaux si nécessaire
        }

        // public static string Login(string email, string password, MySqlConnection connection)
        // {
        //     try
        //     {
        //         // Requête pour récupérer le mot de passe haché associé à l'email donné
        //         string query = "SELECT * FROM utilisateur WHERE email = @Email";

        //         using (var command = new MySqlCommand(query, connection))
        //         {
        //             command.Parameters.AddWithValue("@Email", email);

        //             using (var reader = command.ExecuteReader())
        //             {
        //                 if (reader.Read())
        //                 {
        //                     // Récupère le mot de passe haché depuis la base de données
        //                     string hashedPassword = reader.GetString("mdp");
        //                     int id = reader.GetInt32("id_utilisateur");


        //                     // Vérifie si le mot de passe donné correspond au hash
        //                     if (BCrypt.Net.BCrypt.Verify(password, hashedPassword))
        //                     {   
        //                         string pin=Utilisateur.GenerateFourDigitCode();
        //                         string htmlPath = EmailSenderModel.GenerateEmail(pin, "email_personalise");
        //                         EmailSenderModel.SendEmail("mamynyainarazafinjatovo999@gmail.com",email,htmlPath,pin);

        //                         Authentification authentification = new Authentification();
        //                         authentification.DateDebut = DateTime.Now;
        //                         authentification.DateFin = authentification.DateDebut.AddSeconds(90);
        //                         authentification.Pin = pin;
        //                         authentification.IdUtilisateur = id;
        //                         authentification.Insert(connection);
        //                         // return GenerateToken(16); // Mot de passe correct
        //                         return "true";
        //                     }
        //                 }
        //             }
        //         }
        //     }
        //     catch (Exception ex)
        //     {
        //         Console.WriteLine("Erreur lors de la connexion: " + ex.Message);
        //     }

        //     return null; // Retourne false si l'email ou le mot de passe est incorrect
        // }


            public static string Login(string email, string password, MySqlConnection connection)
            {
                try
                {
                    // Requête pour récupérer les données utilisateur
                    string query = "SELECT * FROM utilisateur WHERE email = @Email";

                    using (var command = new MySqlCommand(query, connection))
                    {
                        command.Parameters.AddWithValue("@Email", email);

                        // Lecture des données
                        using (var reader = command.ExecuteReader())
                        {
                            if (reader.Read())
                            {
                                string hashedPassword = reader.GetString("mdp");
                                int id = reader.GetInt32("id_utilisateur");

                                // Vérification du mot de passe
                                if (BCrypt.Net.BCrypt.Verify(password, hashedPassword))
                                {
                                    string pin = Utilisateur.GenerateFourDigitCode();
                                    string htmlPath = EmailSenderModel.GenerateEmail(pin, "email_personalise");
                                    EmailSenderModel.SendEmail("mamynyainarazafinjatovo999@gmail.com", email, htmlPath, pin);

                                    // Fermeture du reader avant l'insertion
                                    reader.Close();

                                    // Création et insertion de l'objet Authentification
                                    Authentification authentification = new Authentification
                                    {
                                        DateDebut = DateTime.Now,
                                        DateFin = DateTime.Now.AddSeconds(90),
                                        Pin = pin,
                                        IdUtilisateur = id
                                    };

                                    authentification.Insert(connection);
                                    return "true"; // Succès
                                }
                            }
                        }
                    }
                }
                catch (Exception ex)
                {
                    Console.WriteLine("Erreur lors de la connexion: " + ex.Message);
                }

                return null; // Échec
            }

    public void InsertInscription(MySqlConnection connection, string mailSender)
    {
        try
        {
            string mdp_hache = HashPassword(Mdp);
            string randomToken = Authentification.GenerateToken(16);
            string query = "INSERT INTO inscription (email, mdp, date_entree, random_token, date_validation) VALUES (@Email, @Mdp, @DateEntree, @RandomToken, @DateValidation)";

            using (var command = new MySqlCommand(query, connection))
            {
                command.Parameters.AddWithValue("@Email", Email);
                command.Parameters.AddWithValue("@Mdp", mdp_hache);
                command.Parameters.AddWithValue("@DateEntree", DateTime.Now);
                command.Parameters.AddWithValue("@RandomToken", randomToken);
                command.Parameters.AddWithValue("@DateValidation", null); 

                command.ExecuteNonQuery();
            }
            EmailSenderModel.SendEmailWithToken(mailSender, Email, randomToken);

        }
        catch (Exception ex)
        {
            Console.WriteLine("Erreur lors de l'insertion de l'inscription: " + ex.Message);
        }
    }

    public static void ValidateAndInsertUser( MySqlConnection connection, string token)
    {
        try
        {
            string selectQuery = "SELECT * FROM inscription WHERE random_token = @Token";
            using (var selectCommand = new MySqlCommand(selectQuery, connection))
            {
                selectCommand.Parameters.AddWithValue("@Token", token);

                using (var reader = selectCommand.ExecuteReader())
                {
                    if (reader.Read())
                    {
                        string email = reader.GetString("email");
                        string mdp = reader.GetString("mdp");
                        int idInscription = reader.GetInt32("id_inscription");

                        reader.Close();

                        string updateQuery = "UPDATE inscription SET date_validation = @DateValidation WHERE id_inscription = @IdInscription";
                        using (var updateCommand = new MySqlCommand(updateQuery, connection))
                        {
                            updateCommand.Parameters.AddWithValue("@DateValidation", DateTime.Now);
                            updateCommand.Parameters.AddWithValue("@IdInscription", idInscription);
                            updateCommand.ExecuteNonQuery();
                        }

                        string insertQuery = "INSERT INTO utilisateur (email, mdp) VALUES (@Email, @Mdp)";
                        using (var insertCommand = new MySqlCommand(insertQuery, connection))
                        {
                            insertCommand.Parameters.AddWithValue("@Email", email);
                            insertCommand.Parameters.AddWithValue("@Mdp", mdp);
                            insertCommand.ExecuteNonQuery();
                        }
                    }
                }
            }
        }
        catch (Exception ex)
        {
            Console.WriteLine("Erreur lors de la validation et de l'insertion de l'utilisateur: " + ex.Message);
        }
    }
}
}
