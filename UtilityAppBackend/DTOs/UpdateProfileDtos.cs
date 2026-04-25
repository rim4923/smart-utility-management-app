public class UpdateProfileRequest
{
    public string FirstName { get; set; }
    public string LastName { get; set; }
    public string Email { get; set; }
    public string Phone { get; set; }
}

public class UpdateNotificationRequest
{
    public bool Enabled { get; set; }
}