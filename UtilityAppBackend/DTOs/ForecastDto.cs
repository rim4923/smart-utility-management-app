public class ForecastDto
{
    public double NextMonthAmount { get; set; }
    public double PercentageChange { get; set; }
}
public class MonthlyForecastItemDto
{
    public string Month { get; set; }     
    public double Amount { get; set; }
    public bool IsForecast { get; set; }  
    public double ChangePercentage { get; set; }
}

public class ForecastDetailsDto
{
    public List<MonthlyForecastItemDto> Items { get; set; }
}