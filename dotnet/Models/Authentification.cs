using MySql.Data.MySqlClient;
using System;
using System.Text;


namespace auth.Models
{
    public class Authentification
    {
        // Propriétés
        public int IdAuthentification { get; set; }
        public string Pin { get; set; }
        public DateTime DateDebut { get; set; }
        public DateTime DateFin { get; set; }
        public int IdUtilisateur { get; set; }

        // Constructeur avec paramètres
        public Authentification(int idAuthentification, string pin, DateTime dateDebut, DateTime dateFin, int idUtilisateur)
        {
            IdAuthentification = idAuthentification;
            Pin = pin;
            DateDebut = dateDebut;
            DateFin = dateFin;
            IdUtilisateur = idUtilisateur;
        }

        // Constructeur sans paramètres
        public Authentification() { }

        // Méthode pour insérer une authentification
        public void Insert(MySqlConnection connection)
        {
            try
            {
                // Obtenez l'heure actuelle
                DateTime dateDebut = DateTime.Now;
                DateTime dateFin = dateDebut.AddSeconds(90); // Ajoutez 90 secondes à la date de début

                string query = "INSERT INTO authentification (pin, date_debut, date_fin, id_utilisateur) " +
                            "VALUES (@Pin, @DateDebut, @DateFin, @IdUtilisateur)";

                using (var command = new MySqlCommand(query, connection))
                {
                    // Utilisez les valeurs calculées pour les paramètres
                    command.Parameters.AddWithValue("@Pin", Pin);
                    command.Parameters.AddWithValue("@DateDebut", dateDebut);
                    command.Parameters.AddWithValue("@DateFin", dateFin);
                    command.Parameters.AddWithValue("@IdUtilisateur", IdUtilisateur);

                    command.ExecuteNonQuery();
                }
            }
            catch (Exception ex)
            {
                Console.WriteLine("Erreur lors de l'insertion: " + ex.Message);
            }
        }


        // Méthode pour récupérer une authentification par ID
        public static Authentification SelectById(int id, MySqlConnection connection)
        {
            try
            {
                string query = "SELECT * FROM authentification WHERE id_authentification = @Id";

                using (var command = new MySqlCommand(query, connection))
                {
                    command.Parameters.AddWithValue("@Id", id);

                    using (var reader = command.ExecuteReader())
                    {
                        if (reader.Read())
                        {
                            return new Authentification(
                                reader.GetInt32("id_authentification"),
                                reader.GetString("pin"),
                                reader.GetDateTime("date_debut"),
                                reader.GetDateTime("date_fin"),
                                reader.GetInt32("id_utilisateur")
                            );
                        }
                    }
                }
            }
            catch (Exception ex)
            {
                Console.WriteLine("Erreur lors de la récupération: " + ex.Message);
            }

            return null; // Retourne null si aucune donnée n'est trouvée
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

        public static Authentification SelectLatestByPinAndUser(string pin, int idUtilisateur, MySqlConnection connection)
        {
            const string query = @"
                SELECT * 
                FROM authentification 
                WHERE Pin = @Pin AND  id_utilisateur = @IdUtilisateur 
                ORDER BY date_debut DESC 
                LIMIT 1";

            using (var command = new MySqlCommand(query, connection))
            {
                command.Parameters.AddWithValue("@Pin", pin);
                command.Parameters.AddWithValue("@IdUtilisateur", idUtilisateur);

                using (var reader = command.ExecuteReader())
                {
                    if (reader.Read())
                    {
                        // Extraire les données du lecteur
                        int idAuthentification = reader.GetInt32("id_authentification");
                        string pinResult = reader.GetString("Pin");
                        DateTime dateDebut = reader.GetDateTime("date_debut");
                        DateTime dateFin = reader.GetDateTime("date_fin");
                        int idUser = reader.GetInt32("id_utilisateur");

                        // Fermer le DataReader avant d'exécuter une autre commande
                        reader.Close();

                        // Générer et insérer le token
                        string token = GenerateToken(16);
                        TokenHistorique tokenHistorique = new TokenHistorique(
                            token,
                            DateTime.Now,
                            DateTime.Now.AddSeconds(600),
                            idUtilisateur
                        );

                        bool insert = TokenHistorique.Insert(tokenHistorique, connection);

                        // Retourner l'objet Authentification
                        return new Authentification
                        {
                            IdAuthentification = idAuthentification,
                            Pin = pinResult,
                            DateDebut = dateDebut,
                            DateFin = dateFin,
                            IdUtilisateur = idUser
                        };
                    }
                }
            }

            return null;
        }

    }

    
}
