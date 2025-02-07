using System;
using System.IO; // Pour la manipulation des fichiers
using MailKit.Net.Smtp;
using MimeKit;
using user.Models;

namespace email.Models
{
    public class EmailSenderModel
    {

        public static string GetHTMLStringForPIN(string pin){
            string htmlContent = $@"
            <!DOCTYPE html>
            <html lang='fr'>
            <head>
                <meta charset='UTF-8'>
                <meta name='viewport' content='width=device-width, initial-scale=1.0'>
                <title>Authentification deux facteurs</title>
                <style>
                body {{
                    font-family: Arial, sans-serif;
                    margin: 0;
                    padding: 0;
                    background-color: #f9f9f9;
                    color: #202124;
                }}
                .container {{
                    max-width: 600px;
                    margin: 40px auto;
                    background-color: #fff;
                    box-shadow: 0 4px 10px rgba(0, 0, 0, 0.2);
                    border-radius: 8px;
                    overflow: hidden;
                }}
                .header {{
                    background-color: #4285F4;
                    color: #fff;
                    text-align: center;
                    padding: 20px;
                }}
                .header h1 {{
                    margin: 0;
                    font-size: 24px;
                }}
                .content {{
                    padding: 30px 20px;
                    text-align: left;
                }}
                .content h2 {{
                    font-size: 20px;
                    color: #202124;
                    margin-bottom: 10px;
                }}
                .content p {{
                    font-size: 16px;
                    line-height: 1.5;
                    margin: 15px 0;
                }}
                .highlight {{
                    background-color: #f1f3f4;
                    padding: 15px;
                    margin: 20px 0;
                    text-align: center;
                    font-size: 18px;
                    font-weight: bold;
                    border: 1px solid #d1d1d1;
                    border-radius: 4px;
                    color: #202124;
                }}
                a.button {{
                    display: inline-block;
                    margin-top: 20px;
                    padding: 12px 20px;
                    background-color: #4285F4;
                    color: #ffffff;
                    text-decoration: none;
                    font-weight: bold;
                    border-radius: 4px;
                    font-size: 16px;
                }}
                a.button:hover {{
                    background-color: #3367D6;
                }}
                .footer {{
                    text-align: center;
                    font-size: 12px;
                    color: #5f6368;
                    border-top: 1px solid #ddd;
                    padding: 10px 0;
                    background-color: #f1f3f4;
                }}
                </style>
            </head>
            <body>
                <div class='container'>
                <div class='header'>
                    <h1>Validation de Votre Compte</h1>
                </div>
                <div class='content'>
                    <h2>Confirmez Votre tentative de connection</h2>
                    <p>Bonjour,</p>
                    <p>Pour vous connecter saisissez le pin si dessous</p>
                    <div class='highlight'>
                    <div class='highlight'>{pin}</div>
                    </div>
                    <p>Si vous n'êtes pas à l'origine de cette demande, veuillez ignorer ce message.</p>
                    <p>Merci de votre confiance.</p>
                </div>
                <div class='footer'>
                    <p>&copy; 2024 Votre Entreprise | Sécurité et Confidentialité</p>
                </div>
                </div>
            </body>
            </html>";
            return htmlContent;
        }


        
        public static string GetHTMLStringForResetCountError(string id)
        {
            // Retrieve the actual port used by the application
            var appPort = TokenGeneratorModel.GetApplicationPort();

            string htmlContent = $@"
            <!DOCTYPE html>
            <html lang='fr'>
            <head>
                <meta charset='UTF-8'>
                <meta name='viewport' content='width=device-width, initial-scale=1.0'>
                <title>Validation de Compte</title>
                <style>
                    body {{
                        font-family: Arial, sans-serif;
                        margin: 0;
                        padding: 0;
                        background-color: #f9f9f9;
                        color: #202124;
                    }}
                    .container {{
                        max-width: 600px;
                        margin: 40px auto;
                        background-color: #fff;
                        box-shadow: 0 4px 10px rgba(0, 0, 0, 0.2);
                        border-radius: 8px;
                        overflow: hidden;
                    }}
                    .header {{
                        background-color: #4285F4;
                        color: #fff;
                        text-align: center;
                        padding: 20px;
                    }}
                    .header h1 {{
                        margin: 0;
                        font-size: 24px;
                    }}
                    .content {{
                        padding: 30px 20px;
                        text-align: left;
                    }}
                    .content h2 {{
                        font-size: 20px;
                        color: #202124;
                        margin-bottom: 10px;
                    }}
                    .content p {{
                        font-size: 16px;
                        line-height: 1.5;
                        margin: 15px 0;
                    }}
                    .highlight {{
                        background-color: #f1f3f4;
                        padding: 15px;
                        margin: 20px 0;
                        text-align: center;
                        font-size: 18px;
                        font-weight: bold;
                        border: 1px solid #d1d1d1;
                        border-radius: 4px;
                        color: #202124;
                    }}
                    a.button {{
                        display: inline-block;
                        margin-top: 20px;
                        padding: 12px 20px;
                        background-color: #4285F4;
                        color: #ffffff;
                        text-decoration: none;
                        font-weight: bold;
                        border-radius: 4px;
                        font-size: 16px;
                    }}
                    a.button:hover {{
                        background-color: #3367D6;
                    }}
                    .footer {{
                        text-align: center;
                        font-size: 12px;
                        color: #5f6368;
                        border-top: 1px solid #ddd;
                        padding: 10px 0;
                        background-color: #f1f3f4;
                    }}
                </style>
            </head>
            <body>
                <div class='container'>
                    <div class='header'>
                        <h1>Validation de Votre Compte</h1>
                    </div>
                    <div class='content'>
                        <h2>Confirmez Votre Adresse Email</h2>
                        <p>Bonjour,</p>
                        <p>Pour reinitialiser le nombre de tentative de votr email :</p>
                        <a href='http://localhost:{appPort}/api/Authentification/restartfailedattempts?id={id}' class='button'>Valider mon compte</a>
                        <p>Si vous n'êtes pas à l'origine de cette demande, veuillez ignorer ce message.</p>
                        <p>Merci de votre confiance.</p>
                    </div>
                    <div class='footer'>
                        <p>&copy; 2024 Votre Entreprise | Sécurité et Confidentialité</p>
                    </div>
                </div>
            </body>
            </html>";
            return htmlContent;
        }



        public static string GetHTMLStringForValidation(string token, string id)
        {
            // Retrieve the actual port used by the application
            var appPort = TokenGeneratorModel.GetApplicationPort();

            string htmlContent = $@"
            <!DOCTYPE html>
            <html lang='fr'>
            <head>
                <meta charset='UTF-8'>
                <meta name='viewport' content='width=device-width, initial-scale=1.0'>
                <title>Validation de Compte</title>
                <style>
                    body {{
                        font-family: Arial, sans-serif;
                        margin: 0;
                        padding: 0;
                        background-color: #f9f9f9;
                        color: #202124;
                    }}
                    .container {{
                        max-width: 600px;
                        margin: 40px auto;
                        background-color: #fff;
                        box-shadow: 0 4px 10px rgba(0, 0, 0, 0.2);
                        border-radius: 8px;
                        overflow: hidden;
                    }}
                    .header {{
                        background-color: #4285F4;
                        color: #fff;
                        text-align: center;
                        padding: 20px;
                    }}
                    .header h1 {{
                        margin: 0;
                        font-size: 24px;
                    }}
                    .content {{
                        padding: 30px 20px;
                        text-align: left;
                    }}
                    .content h2 {{
                        font-size: 20px;
                        color: #202124;
                        margin-bottom: 10px;
                    }}
                    .content p {{
                        font-size: 16px;
                        line-height: 1.5;
                        margin: 15px 0;
                    }}
                    .highlight {{
                        background-color: #f1f3f4;
                        padding: 15px;
                        margin: 20px 0;
                        text-align: center;
                        font-size: 18px;
                        font-weight: bold;
                        border: 1px solid #d1d1d1;
                        border-radius: 4px;
                        color: #202124;
                    }}
                    a.button {{
                        display: inline-block;
                        margin-top: 20px;
                        padding: 12px 20px;
                        background-color: #4285F4;
                        color: #ffffff;
                        text-decoration: none;
                        font-weight: bold;
                        border-radius: 4px;
                        font-size: 16px;
                    }}
                    a.button:hover {{
                        background-color: #3367D6;
                    }}
                    .footer {{
                        text-align: center;
                        font-size: 12px;
                        color: #5f6368;
                        border-top: 1px solid #ddd;
                        padding: 10px 0;
                        background-color: #f1f3f4;
                    }}
                </style>
            </head>
            <body>
                <div class='container'>
                    <div class='header'>
                        <h1>Validation de Votre Compte</h1>
                    </div>
                    <div class='content'>
                        <h2>Confirmez Votre Adresse Email</h2>
                        <p>Bonjour,</p>
                        <p>Pour finaliser la validation de votre compte, veuillez cliquer pour valider votre email :</p>
                        <a href='http://localhost:5005/api/inscription/validationInscription?token={token}&id={id}' class='button'>Valider mon compte</a>
                        <p>Si vous n'êtes pas à l'origine de cette demande, veuillez ignorer ce message.</p>
                        <p>Merci de votre confiance.</p>
                    </div>
                    <div class='footer'>
                        <p>&copy; 2024 Votre Entreprise | Sécurité et Confidentialité</p>
                    </div>
                </div>
            </body>
            </html>";
            return htmlContent;
        }

        // Method to get the current application port
        



        // Fonction pour envoyer un email avec un lien contenant un token
        public static void SendEmailWithToken(string emailSender, string emailReceiver, string token,string id,string apppwd)
        {
            try
            {
            // Générer le contenu HTML avec le lien contenant le token
            string htmlContent = EmailSenderModel.GetHTMLStringForValidation(token,id);

            // Création du message
            var message = new MimeMessage();
            message.From.Add(new MailboxAddress("Expéditeur", emailSender));
            message.To.Add(new MailboxAddress("Récepteur", emailReceiver));
            message.Subject = "Validation de Compte";

            // Ajouter le contenu HTML au corps du message
            message.Body = new TextPart("html")
            {
                Text = htmlContent
            };

            // Connexion et envoi
            using (var client = new SmtpClient())
            {
                Console.WriteLine("Connexion au serveur SMTP...");
                client.Connect("smtp.gmail.com", 587, MailKit.Security.SecureSocketOptions.StartTls);
                Console.WriteLine("Authentification...");
                client.Authenticate(emailSender, apppwd);

                Console.WriteLine("Envoi de l'email...");
                client.Send(message);

                Console.WriteLine("Email envoyé avec succès !");
                client.Disconnect(true);
            }
            }
            catch (Exception ex)
            {
            Console.WriteLine($"Erreur lors de l'envoi de l'email : {ex.Message}");
            }
        }



        public static void SendEmailReset(string emailSender, string emailReceiver,string id,string apppwd)
        {
            try
            {
            // Générer le contenu HTML avec le lien contenant le token
            string htmlContent = EmailSenderModel.GetHTMLStringForResetCountError(id);

            // Création du message
            var message = new MimeMessage();
            message.From.Add(new MailboxAddress("Expéditeur", emailSender));
            message.To.Add(new MailboxAddress("Récepteur", emailReceiver));
            message.Subject = "Validation de Compte";

            // Ajouter le contenu HTML au corps du message
            message.Body = new TextPart("html")
            {
                Text = htmlContent
            };

            // Connexion et envoi
            using (var client = new SmtpClient())
            {
                Console.WriteLine("Connexion au serveur SMTP...");
                client.Connect("smtp.gmail.com", 587, MailKit.Security.SecureSocketOptions.StartTls);
                Console.WriteLine("Authentification...");
                
                client.Authenticate(emailSender, apppwd);

                Console.WriteLine("Envoi de l'email...");
                client.Send(message);

                Console.WriteLine("Email envoyé avec succès !");
                client.Disconnect(true);
            }
            }
            catch (Exception ex)
            {
            Console.WriteLine($"Erreur lors de l'envoi de l'email : {ex.Message}");
            }
        }


        public static void SendEmailWithPIN(string emailSender, string emailReceiver, string pin,string apppwd)
        {
            try
            {
            // Générer le contenu HTML avec le lien contenant le token
            string htmlContent = EmailSenderModel.GetHTMLStringForPIN(pin);

            // Création du message
            var message = new MimeMessage();
            message.From.Add(new MailboxAddress("Expéditeur", emailSender));
            message.To.Add(new MailboxAddress("Récepteur", emailReceiver));
            message.Subject = "Validation de Compte";

            // Ajouter le contenu HTML au corps du message
            message.Body = new TextPart("html")
            {
                Text = htmlContent
            };

            // Connexion et envoi
            using (var client = new SmtpClient())
            {
                Console.WriteLine("Connexion au serveur SMTP...");
                client.Connect("smtp.gmail.com", 587, MailKit.Security.SecureSocketOptions.StartTls);
                Console.WriteLine("Authentification...");
                client.Authenticate(emailSender, apppwd);

                Console.WriteLine("Envoi de l'email...");
                client.Send(message);

                Console.WriteLine("Email envoyé avec succès !");
                client.Disconnect(true);
            }
            }
            catch (Exception ex)
            {
            Console.WriteLine($"Erreur lors de l'envoi de l'email : {ex.Message}");
            }
        }
        
        

    }
}
