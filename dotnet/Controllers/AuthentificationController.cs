using Microsoft.AspNetCore.Mvc;
using MySql.Data.MySqlClient;
using connexion.Models; // Importez la classe ConnexionMysql
using auth.Models; // Importez le modèle Authentification
using System;
using user.Models.DtoClasses;
using user.Models;
using Mysqlx;
using email.Models;

namespace check.Controllers
{
    [ApiController]
    [Route("api/[controller]")]
    public class AuthentificationController : ControllerBase
    {

        [HttpPost("check-pin")]
        public IActionResult CheckPin([FromBody] AuthentificationDto authentification)
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





                            int attempts=FailedConnectionModel.GetFailedAttempts(authentification.IdUtilisateur,connection);
                            int nbres=StaticValueModel.maxloginattemps-(attempts+1);
                            
                                if(attempts>=3){

                                Utilisateur utilisateur=Utilisateur.SelectById(authentification.IdUtilisateur,connection);

                                EmailSenderModel.SendEmailReset(StaticValueModel.EmailSender,utilisateur.Email,authentification.IdUtilisateur+"",StaticValueModel.AppPwd);


                                return Unauthorized(new
                                {
                                    status = "error",
                                    error = "trop de tentativec veuillez les reinitialiser par mail",
                                    data = (object)null
                                }); 
                                }           


                    // Récupérer l'authentification la plus récente pour cet utilisateur avec le PIN donné
                    var lastAuthentification = Authentification.SelectLatestByPinAndUser(authentification.Pin, authentification.IdUtilisateur, connection);

                    var userconnecte=Utilisateur.SelectById(authentification.IdUtilisateur,connection);
                    userconnecte.Mdp=null;

                    if (lastAuthentification == null)
                    {

                        FailedConnectionModel.InsertFailedLoginAttempt(authentification.IdUtilisateur,connection);
                        FailedConnectionModel.UpdateFailedAttempts(authentification.IdUtilisateur,connection);

                        return NotFound(new
                        {
                            status = "error",
                            error = "Aucun PIN de cette valeur pour cet utilisateur, il vous reste "+nbres+" tentatives",
                            data = (object)null
                        });
                    }

                    // Vérifier si le PIN est toujours valable
                    if (DateTime.Now > lastAuthentification.DateFin)
                    {
                        
                        FailedConnectionModel.InsertFailedLoginAttempt(authentification.IdUtilisateur,connection);
                        FailedConnectionModel.UpdateFailedAttempts(authentification.IdUtilisateur,connection);


                        return BadRequest(new
                        {
                            status = "error",
                            error = "Le PIN a expiré , il vous reste "+nbres+" tentatives",
                            data = (object)null
                        });
                    }


                    return Ok(new
                    {
                        status = "success",
                        error = (string)null,
                        data = new
                        {
                            message = "Le PIN est valide.",
                            utilisateur=userconnecte,
                            token=lastAuthentification.Token
                        }
                    });
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





        [HttpGet("restartfailedattempts")]
        public IActionResult RestartFailedAttempts([FromQuery] string id)
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
                    // Vérifier si le PIN est toujours valable
                    if (id==null)
                    {
                        return BadRequest(new
                        {
                            status = "error",
                            error = "ID field required",
                            data = (object)null
                        });

                    }

                    FailedConnectionModel.ReinitialiserTentativesEchouees(int.Parse(id),connection);
                    
                    return Ok(new{
                        status="success",
                        error="null",
                        data= new{
                            message="Les tentatives de l Utilisateur n "+id+"  ont ete remis a 0"
                        }
                    });

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
