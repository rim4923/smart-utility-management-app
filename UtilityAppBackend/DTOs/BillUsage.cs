public class BillUsage
{
    public int Id { get; set; }

    public int BillId { get; set; }
    public Bill Bill { get; set; }

    public DateTime WeekStart { get; set; } // start of the week
    public double Amount { get; set; } // consumption for that week
}