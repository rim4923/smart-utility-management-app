using System.Net;
using System.Net.Mail;

namespace UtilityAppBackend.Services
{
    public class EmailService
    {
        public void SendEmail(string to, string subject, string body)
        {
            var smtp = new SmtpClient("smtp.gmail.com")
            {
                Port = 587,
                Credentials = new NetworkCredential("reemsarhan130@gmail.com", "zrei qifn oxwe nsmk"),
                EnableSsl = true
            };

            var message = new MailMessage
            {
                From = new MailAddress("reemsarhan130@gmail.com"),
                Subject = subject,
                Body = body,
                IsBodyHtml = false
            };

            message.To.Add(to);

            smtp.Send(message);
        }
    }
}