using UtilityAppBackend.Models;

public class Payment
{
    public int Id { get; set; }
    public int UserId { get; set; }

    public double Amount { get; set; }
    public string Status { get; set; } // SUCCESS / FAILED
    public DateTime CreatedAt { get; set; }

    public User User { get; set; }
}