using MySql.Data.MySqlClient;
using System;

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
                string query = "INSERT INTO authentification (pin, date_debut, date_fin, id_utilisateur) " +
                               "VALUES (@Pin, @DateDebut, @DateFin, @IdUtilisateur)";

                using (var command = new MySqlCommand(query, connection))
                {
                    command.Parameters.AddWithValue("@Pin", Pin);
                    command.Parameters.AddWithValue("@DateDebut", DateDebut);
                    command.Parameters.AddWithValue("@DateFin", DateFin);
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
    }
}
