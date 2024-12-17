using Microsoft.AspNetCore.Mvc;
using MySql.Data.MySqlClient;
using connexion.Models; // Classe ConnexionMysql pour la connexion
using user.Models;      // Classe Utilisateur pour le modèle
using System;

namespace check.Controllers
{
    [ApiController]
    [Route("api/[controller]")]
    public class UtilisateurController : ControllerBase
    {
        /// <summary>
        /// Récupère un utilisateur par son ID.
        /// </summary>
        [HttpGet("{id}")]
        public IActionResult GetUtilisateurById(int id)
        {
            try
            {
                // Récupération de la connexion
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

                    // Appel de la méthode SelectById pour récupérer les données
                    var utilisateur = Utilisateur.SelectById(id, connection);

                    if (utilisateur == null)
                    {
                        return NotFound(new
                        {
                            status = "error",
                            error = "Aucun utilisateur trouvé pour cet ID.",
                            data = (object)null
                        });
                    }

                    // Retourner les données si succès
                    return Ok(new
                    {
                        status = "success",
                        error = (string)null,
                        data = new
                        {
                            utilisateur.IdUtilisateur,
                            utilisateur.Email,
                            utilisateur.Mdp // Attention, ne renvoyez pas le mot de passe en clair en production
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

        /// <summary>
        /// Insère un nouvel utilisateur.
        /// </summary>
        [HttpPost("insert")]
        public IActionResult InsertUtilisateur([FromBody] Utilisateur utilisateur)
        {
            if (utilisateur == null || string.IsNullOrEmpty(utilisateur.Email) || string.IsNullOrEmpty(utilisateur.Mdp))
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
                    if (connection == null || connection.State != System.Data.ConnectionState.Open)
                    {
                        return StatusCode(500, new
                        {
                            status = "error",
                            error = "Impossible de se connecter à la base de données.",
                            data = (object)null
                        });
                    }

                    // Insertion des données via la méthode Insert
                    utilisateur.Insert(connection);

                    return Ok(new
                    {
                        status = "success",
                        error = (string)null,
                        data = new
                        {
                            message = "Utilisateur inséré avec succès."
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


        [HttpPost("login")]
        public IActionResult Login([FromBody] Utilisateur loginRequest)
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
                    string token = Utilisateur.Login(loginRequest.Email, loginRequest.Mdp, connection);

                    if (token != null)
                    {
                        return Ok(new
                        {
                            status = "success",
                            error = (string)null,
                            data = new
                            {
                                message = "Connexion réussie !",
                                data = token // Retourner le token généré
                            }
                        });
                    }
                    else
                    {
                        return Unauthorized(new
                        {
                            status = "error",
                            error = "Email ou mot de passe incorrect.",
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
    }
}
