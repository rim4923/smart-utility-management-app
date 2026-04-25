public class UtilityDto
{
    public required string Id { get; set; }
    public required string Name { get; set; }
    public required string Status { get; set; }
    public required string Type { get; set; }
    public required string Currency { get; set; }
    public required double Cost { get; set; }

    public required double Consumption { get; set; }
    public required string ConsumptionUnit { get; set; }
    public required String NextBillDate { get; set; }
    public required string PricePerUnit { get; set; }

    public DateTime StartDate { get; set; }
    public double? PastMonthChange { get; set; }
    public double? ForecastChange { get; set; }
}