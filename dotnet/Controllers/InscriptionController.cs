using Microsoft.AspNetCore.Mvc;
using connexion.Models;
using user.Models;
using user.Models.DtoClasses; // Importez la classe ConnexionMysql
namespace check.Controllers
{
    [ApiController]
    [Route("api/[controller]")]
    public class InscriptionController : ControllerBase
    {
        

        [HttpPost("sinscrire")]
        public IActionResult InsertInscription([FromBody] UtilisateurDto utilisateur)
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
                Utilisateur realuser=new Utilisateur();
                realuser.Email=utilisateur.Email;
                realuser.Mdp=utilisateur.Mdp;
                realuser.InsertInscription(connection);

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
        public IActionResult validation([FromQuery] string id,[FromQuery] string token)
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
                Utilisateur.ValidateAndInsertUser(connection,token,id);

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
