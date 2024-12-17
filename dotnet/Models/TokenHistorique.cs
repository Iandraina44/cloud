using MySql.Data.MySqlClient;
using System;

namespace auth.Models
{
    public class TokenHistorique
    {
        public int IdTokenHistorique { get; set; }
        public string TokenUtilisateur { get; set; }
        public DateTime DateDebut { get; set; }
        public DateTime DateFin { get; set; }
        public int IdUtilisateur { get; set; }

        // Constructeur
        public TokenHistorique(string tokenUtilisateur, DateTime dateDebut, DateTime dateFin, int idUtilisateur)
        {
            TokenUtilisateur = tokenUtilisateur;
            DateDebut = dateDebut;
            DateFin = dateFin;
            IdUtilisateur = idUtilisateur;
        }

        // Fonction Insert
        public static bool Insert(TokenHistorique tokenHistorique, MySqlConnection connection)
        {
            try
            {
                // Créer la commande SQL pour insérer un nouvel enregistrement
                string query = "INSERT INTO token_historique (token_utilisateur, date_debut, date_fin, id_utilisateur) " +
                               "VALUES (@tokenUtilisateur, @dateDebut, @dateFin, @idUtilisateur)";

                using (MySqlCommand cmd = new MySqlCommand(query, connection))
                {
                    // Ajouter les paramètres
                    cmd.Parameters.AddWithValue("@tokenUtilisateur", tokenHistorique.TokenUtilisateur);
                    cmd.Parameters.AddWithValue("@dateDebut", tokenHistorique.DateDebut);
                    cmd.Parameters.AddWithValue("@dateFin", tokenHistorique.DateFin);
                    cmd.Parameters.AddWithValue("@idUtilisateur", tokenHistorique.IdUtilisateur);

                    // Exécuter la commande
                    int result = cmd.ExecuteNonQuery();

                    // Vérifier si l'insertion a réussi
                    return result > 0;
                }
            }
            catch (Exception ex)
            {
                // Gestion des erreurs
                Console.WriteLine("Erreur lors de l'insertion du token historique : " + ex.Message);
                return false;
            }
        }
    }
}
