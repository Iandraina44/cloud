using Microsoft.AspNetCore.Mvc;
using email.Models; // Importez la classe EmailSender
using System;

namespace EmailSenderApp.Controllers
{
    [ApiController]
    [Route("api/[controller]")]
    public class EmailController : ControllerBase
    {
        /// <summary>
        /// Envoie un e-mail avec un fichier HTML généré dynamiquement.
        /// </summary>
        /// <param name="emailRequest">Objet contenant les informations nécessaires pour envoyer un e-mail</param>
        /// <returns>Statut de l'envoi</returns>
        [HttpPost("send-email")]
        public IActionResult SendEmail([FromBody] EmailRequest emailRequest)
        {
            try
            {
                // Validation des paramètres
                if (string.IsNullOrEmpty(emailRequest.EmailSender) ||
                    string.IsNullOrEmpty(emailRequest.EmailReceiver) ||
                    string.IsNullOrEmpty(emailRequest.PinOuLink) ||
                    string.IsNullOrEmpty(emailRequest.FileName))
                {
                    return BadRequest(new
                    {
                        status = "error",
                        error = "Tous les champs requis doivent être remplis.",
                        data = (object)null
                    });
                }

                // Génération du fichier HTML
                string htmlPath = EmailSenderModel.GenerateEmail(emailRequest.PinOuLink, emailRequest.FileName);

                if (string.IsNullOrEmpty(htmlPath))
                {
                    return StatusCode(500, new
                    {
                        status = "error",
                        error = "Erreur lors de la génération du fichier HTML.",
                        data = (object)null
                    });
                }

                // Envoi de l'email
                EmailSenderModel.SendEmail(emailRequest.EmailSender, emailRequest.EmailReceiver, htmlPath, emailRequest.PinOuLink);

                // Retour en cas de succès
                return Ok(new
                {
                    status = "success",
                    error = (string)null,
                    data = new
                    {
                        message = "E-mail envoyé avec succès.",
                        filePath = htmlPath
                    }
                });
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

    /// <summary>
    /// Classe pour le corps de la requête POST
    /// </summary>
    public class EmailRequest
    {
        public string EmailSender { get; set; }
        public string EmailReceiver { get; set; }
        public string PinOuLink { get; set; }
        public string FileName { get; set; }
    }
}
