using Microsoft.AspNetCore.Mvc;
using MySql.Data.MySqlClient;
using connexion.Models; // Classe ConnexionMysql pour la connexion
using user.Models;      // Classe Utilisateur pour le modèle
using System;
using user.Models.DtoClasses;
using email.Models;

namespace check.Controllers
{
    [ApiController]
    [Route("api/[controller]")]
    public class UtilisateurController : ControllerBase
    {

        [HttpPost("login")]
        public IActionResult Login([FromBody] UtilisateurDto loginRequest)
        {


            

            if (loginRequest == null || string.IsNullOrEmpty(loginRequest.Email) || string.IsNullOrEmpty(loginRequest.Mdp))
            {
                return BadRequest(new
                {
                    status = "error",
                    error = "L'email et le mot de passe sont obligatoires.",
                    data = (object)null
                });
            
            }
            

            try
            {
                // Récupération de la connexion
                using (var connection = ConnexionMysql.GetConnexion())
                {


                     int idmail1=Utilisateur.getIdMail(loginRequest.Email,connection);
                        if (idmail1!=-1)
                        {

                            int attempts=FailedConnectionModel.GetFailedAttempts(idmail1,connection);

                                if(attempts>=3){
                                
                                Utilisateur utilisateur=Utilisateur.SelectById(idmail1,connection);

                                EmailSenderModel.SendEmailReset(StaticValueModel.EmailSender,utilisateur.Email,utilisateur.IdUtilisateur+"",StaticValueModel.AppPwd);


                                return Unauthorized(new
                                {
                                    status = "error",
                                    error = "trop de tentativec veuillez les reinitialiser par mail",
                                    data = (object)null
                                }); 
                                }           
                        }










                    if (connection == null || connection.State != System.Data.ConnectionState.Open)
                    {
                        return StatusCode(500, new
                        {
                            status = "error",
                            error = "Impossible de se connecter à la base de données.",
                            data = (object)null
                        });
                    }

                    // Appel de la méthode Login pour vérifier si l'email et le mot de passe sont corrects
                    Utilisateur userinfo = Utilisateur.Login(loginRequest.Email, loginRequest.Mdp, connection);

                    if (userinfo != null)
                    {
                        return Ok(new
                        {
                            status = "success",
                            error = (string)null,
                            data = new
                            {
                                message = "Email de pin envoye a votre mail veuillez verifier",
                                data = userinfo // Retourner le token généré
                            }
                        });
                    }
                    else
                    {
                        int idmail=Utilisateur.getIdMail(loginRequest.Email,connection);
                        if (idmail!=-1)
                        {
                            FailedConnectionModel.InsertFailedLoginAttempt(idmail,connection);
                            FailedConnectionModel.UpdateFailedAttempts(idmail,connection);

                            int attempts=FailedConnectionModel.GetFailedAttempts(idmail,connection);
                            int nb=StaticValueModel.maxloginattemps-attempts;
                            
                                return Unauthorized(new
                                {
                                    status = "error",
                                    error = "mot de passe incorrect il vous reste"+nb+" tentatives",
                                    data = (object)null
                                });                        
                        }
                        else{
                            return Unauthorized(new
                            {
                                status = "error",
                                error = "Email incorrect.",
                                data = (object)null
                            });
                        }
                    }
                }
            }
            catch (Exception ex)
            {
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
