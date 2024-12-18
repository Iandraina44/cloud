using Microsoft.AspNetCore.Mvc;
using MySql.Data.MySqlClient;
using connexion.Models; // Importez la classe ConnexionMysql
using auth.Models; // Importez le modèle Authentification
using System;

namespace check.Controllers
{
    [ApiController]
    [Route("api/[controller]")]
    public class AuthentificationController : ControllerBase
    {
        [HttpGet("authentification/{id}")]
        public IActionResult GetAuthentificationById(int id)
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
                            error = "Impossible de se connecter à la base de donnees.",
                            data = (object)null
                        });
                    }

                    // Appel de la méthode SelectById pour récupérer les données
                    var authentification = Authentification.SelectById(id, connection);

                    if (authentification == null)
                    {
                        return NotFound(new
                        {
                            status = "error",
                            error = "Aucune authentification trouvee pour cet ID.",
                            data = (object)null
                        });
                    }

                    // Retourner le JSON en cas de succès
                    return Ok(new
                    {
                        status = "success",
                        error = (string)null,
                        data = new
                        {
                            authentification.IdAuthentification,
                            authentification.Pin,
                            authentification.DateDebut,
                            authentification.DateFin,
                            authentification.IdUtilisateur
                        }
                    });
                }
            }
            catch (Exception ex)
            {
                // Gestion des erreurs générales
                return StatusCode(500, new
                {
                    status = "error",
                    error = ex.Message,
                    data = (object)null
                });
            }
        }



         [HttpPost]
        public IActionResult InsertTokenHistorique([FromBody] TokenHistorique tokenHistorique)
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

                    // Appel de la fonction Insert pour ajouter un token historique
                    bool insertSuccess = TokenHistorique.Insert(tokenHistorique, connection);

                    if (insertSuccess)
                    {
                        return Ok(new
                        {
                            status = "success",
                            error = (string)null,
                            data = new
                            {
                                message = "Token historique ajouté avec succès."
                            }
                        });
                    }
                    else
                    {
                        return BadRequest(new
                        {
                            status = "error",
                            error = "Échec de l'insertion du token historique.",
                            data = (object)null
                        });
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

        [HttpPost("add")]
        public IActionResult InsertAuthentification([FromBody] Authentification authentification)
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

                    // Définir les valeurs dynamiques pour DateDebut et DateFin
                    authentification.DateDebut = DateTime.Now;
                    authentification.DateFin = authentification.DateDebut.AddSeconds(90);

                    // Appel de la méthode Insert pour ajouter une authentification
                    authentification.Insert(connection);

                    return Ok(new
                    {
                        status = "success",
                        error = (string)null,
                        data = new
                        {
                            message = "Authentification ajoutée avec succès.",
                            authentification.Pin,
                            authentification.DateDebut,
                            authentification.DateFin,
                            authentification.IdUtilisateur
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

        [HttpPost("check-pin")]
        public IActionResult CheckPin([FromBody] Authentification authentification)
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

                    // Récupérer l'authentification la plus récente pour cet utilisateur avec le PIN donné
                    var lastAuthentification = Authentification.SelectLatestByPinAndUser(authentification.Pin, authentification.IdUtilisateur, connection);

                    if (lastAuthentification == null)
                    {
                        return NotFound(new
                        {
                            status = "error",
                            error = "Aucun PIN valide trouvé pour cet utilisateur.",
                            data = (object)null
                        });
                    }

                    // Vérifier si le PIN est toujours valable
                    if (DateTime.Now > lastAuthentification.DateFin)
                    {
                        return BadRequest(new
                        {
                            status = "error",
                            error = "Le PIN a expiré.",
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
                            lastAuthentification.Pin,
                            lastAuthentification.DateDebut,
                            lastAuthentification.DateFin,
                            lastAuthentification.IdUtilisateur
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
