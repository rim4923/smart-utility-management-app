using UtilityAppBackend.Models;

public class PaymentCard
{
    public int Id { get; set; }
    public int UserId { get; set; }

    public string HolderName { get; set; }
    public string Last4 { get; set; }
    public int ExpMonth { get; set; }
    public int ExpYear { get; set; }
    public string Brand { get; set; } = "VISA";
    public bool IsDefault { get; set; }

    public User User { get; set; }
}