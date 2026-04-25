public class BillDetailsResponse
{
    public required string Id { get; set; }

    public string? Type { get; set; }
    public string? Status { get; set; }

    public double? Cost { get; set; }
    public string? Currency { get; set; }

    public DateTime? NextBillDate { get; set; }

    public double? Consumption { get; set; }
    public string? ConsumptionUnit { get; set; }

    public string? PricePerUnit { get; set; }

    public List<int>? WeeklyConsumption { get; set; }

    public string? ProviderName { get; set; }
    public string? ProviderPhone { get; set; }
    public string? ProviderWebsite { get; set; }
}