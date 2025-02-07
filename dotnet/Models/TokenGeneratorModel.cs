using System;
using System.Text;

public class TokenGeneratorModel
{
    private static readonly char[] TokenCharacters = 
        "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789".ToCharArray();

    public static string GenerateToken()
    {
        var random = new Random();
        var token = new StringBuilder(16);

        for (int i = 0; i < 16; i++)
        {
            token.Append(TokenCharacters[random.Next(TokenCharacters.Length)]);
        }

        return token.ToString();
    }

    public static string GenerateFourDigitCode()
        {
            var random = new Random();
            int code = random.Next(0, 10000); // Génère un entier aléatoire entre 0 et 9999
            return code.ToString("D4"); // Formate le nombre en 4 chiffres avec des zéros initiaux si nécessaire
        }

   public static int GetApplicationPort()
    {
        // Récupérer la variable d'environnement ASPNETCORE_URLS qui contient les URLs de l'application
        var urls = Environment.GetEnvironmentVariable("ASPNETCORE_URLS");

        if (!string.IsNullOrEmpty(urls))
        {
            // Extrait le port de l'URL si elle est présente
            var uri = new Uri(urls.Split(';')[0]); // Utilise le premier URL trouvé
            return uri.Port;
        }

        // Si aucune URL n'est trouvée, retourner un port par défaut
        return 5207; // Assumer 5207 comme port par défaut
}



}
