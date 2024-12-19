using Microsoft.AspNetCore.Mvc;
using MySql.Data.MySqlClient;
using connexion.Models; // Importez la classe ConnexionMysql
using auth.Models; // Importez le modèle Authentification
using System;
using user.Models;
namespace check.Controllers
{
    [ApiController]
    [Route("api/[controller]")]
    public class InscriptionController : ControllerBase
    {
        

        [HttpPost("sinscrire")]
        public IActionResult InsertInscription([FromBody] Utilisateur utilisateur, [FromQuery] string mailSender)
        {
            try
            {
            // Récupération de la connexion via ConnexionMysql
            using (var connection = ConnexionMysql.GetConnexion())
            {
                if (connection == null || connection.State != System.Data.ConnectionState.Open)
                {
                return StatusCode(500, new
                {
                    status = "error",
                    error = "Impossible de se connecter à la base de données.",
                    data = (object)null
                });
                }

                // Appel de la méthode InsertInscription pour ajouter une inscription
                utilisateur.InsertInscription(connection, mailSender);

                return Ok(new
                {
                status = "success",
                error = (string)null,
                data = new
                {
                    message = "Inscription demander,Attente validation mail"
                }
                });
            }
            }
            catch (Exception ex)
            {
            // Gestion des erreurs
            return StatusCode(500, new
            {
                status = "error",
                error = ex.Message,
                data = (object)null
            });
            }
        }



        [HttpGet("validationInscription")]
        public IActionResult validation([FromQuery] string token)
        {
            try
            {
            // Récupération de la connexion via ConnexionMysql
            using (var connection = ConnexionMysql.GetConnexion())
            {
                if (connection == null || connection.State != System.Data.ConnectionState.Open)
                {
                return StatusCode(500, new
                {
                    status = "error",
                    error = "Impossible de se connecter à la base de données.",
                    data = (object)null
                });
                }

                // Appel de la méthode InsertInscription pour ajouter une inscription
                Utilisateur.ValidateAndInsertUser(connection,token);

                return Ok(new
                {
                status = "success",
                error = (string)null,
                data = new
                {
                    message = "UTILISATEUR VALIDER"
                }
                });
            }
            }
            catch (Exception ex)
            {
            // Gestion des erreurs
            return StatusCode(500, new
            {
                status = "error",
                error = ex.Message,
                data = (object)null
            });
            }
        }

        
    
    }
}
