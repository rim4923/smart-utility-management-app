public class GlanceCardDto
{
    public required double CurrentConsumption { get; set; }
    public required double ForecastedConsumption { get; set; }
    public required double ConsumptionTrend { get; set; }
    public required double ForecastedConsumptionTrend { get; set; }
    public required string Currency { get; set; }
    public required string FirstName { get; set; }
}