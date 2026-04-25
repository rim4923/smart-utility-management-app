public class ProfileDto
{
    public int Id { get; set; }

    public string FirstName { get; set; }
    public string LastName { get; set; }

    public string Email { get; set; }
    public string Phone { get; set; }

    public string? ProfileImageUrl { get; set; }

    public bool NotificationsEnabled { get; set; }
}