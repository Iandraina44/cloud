using MySql.Data.MySqlClient;
using System;
using System.Text;
using BCrypt.Net;
using email.Models;

namespace user.Models
{
    public class StaticValueModel
    {
        public static string EmailSender = "mamynyainarazafinjatovo999@gmail.com";
        public static string AppPwd = "dnrh mdci wrup vrot";

        public static int timemtokenvalidity=600;
        public static int timepinvalidity=180;
        public static int maxloginattemps=3;

    }
}
