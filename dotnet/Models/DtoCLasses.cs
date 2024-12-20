namespace user.Models.DtoClasses
{
    public class UtilisateurDto
    {
        // Propriétés sans ID
        public string Email { get; set; }
        public string Mdp { get; set; }

        // Constructeur avec paramètres
        public UtilisateurDto(string email, string mdp)
        {
            Email = email;
            Mdp = mdp;
        }

        // Constructeur par défaut
        public UtilisateurDto() { }
    }

    public class AuthentificationDto
    {
        // Propriétés sans ID

        public string Pin {get;set;}
        public int IdUtilisateur {get;set;}

        // Constructeur par défaut
        public AuthentificationDto() { }
    }

    


}
