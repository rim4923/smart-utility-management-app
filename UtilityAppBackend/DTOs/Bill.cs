public class Bill
{
    public int Id { get; set; }
    public int UserId { get; set; }

    public string Name { get; set; }
    public string Type { get; set; }

    public double Cost { get; set; }
    public string Currency { get; set; }

    public bool IsPaid { get; set; }

    public DateTime NextBillDate { get; set; }

    public double Consumption { get; set; }
    public string ConsumptionUnit { get; set; }

    public string PricePerUnit { get; set; }
    public DateTime StartDate { get; set; }

    public double? PastMonthChange { get; set; }
    public double? ForecastChange { get; set; }

    public string ProviderName { get; set; }
    public string ProviderPhone { get; set; }
    public string ProviderWebsite { get; set; }
}