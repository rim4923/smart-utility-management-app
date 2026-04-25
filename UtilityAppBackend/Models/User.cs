namespace UtilityAppBackend.Models
{
    public class User
    {
        public int Id { get; set; }

        public required string Email { get; set; }
        public required string PasswordHash { get; set; }

        public required string FirstName { get; set; }
        public required string LastName { get; set; }
        public required string Phone { get; set; }
        public required string Nationality { get; set; }

        public bool IsVerified { get; set; } = false;
        public string? ProfileImageUrl { get; set; }
        public bool NotificationsEnabled { get; set; } = true;

        public string? ResetCode { get; set; }
        public DateTime? ResetCodeExpiry { get; set; }
    }
}