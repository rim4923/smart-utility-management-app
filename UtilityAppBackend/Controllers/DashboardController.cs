using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;
using System.Security.Claims;



namespace UtilityAppBackend.Controllers
{
    public class DashboardResponse
{
    public GlanceCardDto GlanceCard { get; set; }
    public List<UtilityDto> Utilities { get; set; }
    public List<double> ChartData { get; set; }
    public List<ExpenseDto> TopExpenses { get; set; }
    public InsightsDto Insights { get; set; }
    public ForecastDto Forecast { get; set; }

    public required ForecastDetailsDto ForecastDetails { get; set; }
}

       
    [ApiController]
    [Route("api/[controller]")]
    public class DashboardController : ControllerBase
    {
        private readonly AppDbContext _context;

        public DashboardController(AppDbContext context)
        {
            _context = context;
        }
        
    [Authorize]
    [HttpGet]
    public IActionResult GetDashboard()
    {
        var userId = int.Parse(User.FindFirst(ClaimTypes.NameIdentifier)!.Value);

        var now = DateTime.UtcNow;
        var currentMonth = now.Month;
        var currentYear = now.Year;
        var previousMonth = now.AddMonths(-1).Month;
        var previousYear = now.AddMonths(-1).Year;

        var bills = _context.Bills
            .Where(b => b.UserId == userId)
            .ToList();
        var currentBills = bills.Where(b =>
            b.NextBillDate.Month == currentMonth &&
            b.NextBillDate.Year == currentYear
        ).ToList();
        var previousBills = bills.Where(b =>
            b.NextBillDate.Month == previousMonth &&
            b.NextBillDate.Year == previousYear
        ).ToList();

        var currentTotal = currentBills.Sum(b => b.Cost);
        var previousTotal = previousBills.Sum(b => b.Cost);

        double trend;
        if (previousTotal <= 0) trend = 0;
        else trend = ((currentTotal - previousTotal) / previousTotal) * 100;
        if (trend > 100) trend = 100;
        if (trend < -100) trend = -100;
        trend = Math.Round(trend, 1);


        var weeklyRaw = _context.BillUsages
            .Where(u => u.Bill.UserId == userId)
            .OrderBy(u => u.WeekStart)
            .ToList();

        var weekly = weeklyRaw
            .GroupBy(u => u.WeekStart)
            .Select(g => g.Sum(x => x.Amount))
            .ToList();

        if (weekly.Count < 4)weekly = new List<double> { currentTotal / 4, currentTotal / 4, currentTotal / 4, currentTotal / 4 };

        double alpha = 0.5;
        double level = weekly[0];

        for (int i = 1; i < weekly.Count; i++)
        {
            level = alpha * weekly[i] + (1 - alpha) * level;
        }

        int n = weekly.Count;
        double xAvg = Enumerable.Range(0, n).Average();
        double yAvg = weekly.Average();

        double num = 0, den = 0;

        for (int i = 0; i < n; i++)
        {
            num += (i - xAvg) * (weekly[i] - yAvg);
            den += Math.Pow(i - xAvg, 2);
        }

        double trnd = den == 0 ? 0 : num / den;

        Dictionary<int, double> seasonalMap = new()
        {
            {1, 1.05}, {2, 1.04}, {3, 1.00}, {4, 0.98},
            {5, 1.00}, {6, 1.10}, {7, 1.15}, {8, 1.12},
            {9, 1.05}, {10, 1.00}, {11, 0.97}, {12, 1.08}
        };

        var monthlyData = Enumerable.Range(0, 3)
            .Select(i => now.AddMonths(-i))
            .Reverse()
            .Select(m => new
            {
                Date = m,
                Total = bills
                    .Where(b => b.NextBillDate.Month == m.Month && b.NextBillDate.Year == m.Year)
                    .Sum(b => b.Cost)
            })
            .ToList();
        
        
        List<MonthlyForecastItemDto> forecastItems;
        if (monthlyData.Count < 3 || monthlyData.All(x => x.Total == 0))
        {
            forecastItems = new List<MonthlyForecastItemDto>();
        }
        else
        {
            var values = monthlyData.Select(x => x.Total).ToArray();
            int n2 = values.Length;

            double xAvg2 = Enumerable.Range(0, n2).Average();
            double yAvg2 = values.Average();

            double num2 = 0, den2 = 0;
            for (int i = 0; i < n2; i++)
            {
                num2 += (i - xAvg2) * (values[i] - yAvg2);
                den2 += Math.Pow(i - xAvg2, 2);
            }

            double slope = den2 == 0 ? 0 : num2 / den2;

            double momentum = (values[n2 - 1] - values[n2 - 2]);

            forecastItems = new List<MonthlyForecastItemDto>();

            foreach (var m in monthlyData)
            {
                forecastItems.Add(new MonthlyForecastItemDto
                {
                    Month = m.Date.ToString("MMMM"),
                    Amount = Math.Round(m.Total, 2),
                    IsForecast = false,
                    ChangePercentage = 0
                });
            }

            double lastValue = values.Last();

            // 7. Forecast next 3 months
            for (int i = 1; i <= 3; i++)
            {
                var futureDate = now.AddMonths(i);

                double seasonalFactor = seasonalMap.ContainsKey(futureDate.Month)
                    ? seasonalMap[futureDate.Month]
                    : 1.0;

                // Core prediction
                double prediction =
                    lastValue +
                    (slope * i) +          // long-term trend
                    (momentum * 0.3 * i);  // short-term acceleration

                // Apply seasonality
                prediction *= seasonalFactor;

                // Safety clamp (avoid crazy jumps)
                double maxIncrease = lastValue * 1.15;
                double minDecrease = lastValue * 0.90;

                if (prediction > maxIncrease) prediction = maxIncrease;
                if (prediction < minDecrease) prediction = minDecrease;

                double change = lastValue == 0 ? 0 :
                    ((prediction - lastValue) / lastValue) * 100;

                forecastItems.Add(new MonthlyForecastItemDto
                {
                    Month = futureDate.ToString("MMMM"),
                    Amount = Math.Round(prediction, 2),
                    IsForecast = true,
                    ChangePercentage = Math.Round(change, 1)
                });

                lastValue = prediction;
            }
        }
        double weeklyTrend = 0;
        double weeklyAverageValue = 0;

        if (weekly.Any()){
            weeklyAverageValue = weekly.Average();
            if (weekly.Count >= 2 && weekly.First() > 0)
               weeklyTrend = ((weekly.Last() - weekly.First()) / weekly.First()) * 100;
        }

        weeklyTrend = Math.Round(weeklyTrend, 1);

        var insights = new InsightsDto
        {
            Values = weekly.Select(w => (float)w).TakeLast(5).ToList(),
            MonthValue = Math.Round(weeklyAverageValue, 2),
            ChangePercentage = weeklyTrend
        };


        var topExpenses = currentBills
            .GroupBy(b => b.Type)
            .Select(g => new ExpenseDto
            {
                Type = g.Key,
                Amount = Math.Round(g.Sum(x => x.Cost), 2),
                Currency = "USD"
            })
            .OrderByDescending(e => e.Amount)
            .Take(3)
            .ToList();

        

        var lastAct = forecastItems
    .LastOrDefault(x => !x.IsForecast)?.Amount ?? 0;

        var nextMonthForecast = forecastItems
            .FirstOrDefault(x => x.IsForecast);

        double nextAmount = nextMonthForecast?.Amount ?? 0;
        double changep = lastAct == 0 ? 0 :
            ((nextAmount - lastAct) / lastAct) * 100;

        changep = Math.Round(changep, 1);

        var forecastDto = new ForecastDto
        {
            NextMonthAmount = nextAmount,
            PercentageChange = changep
        };

        var user = _context.Users.First(u => u.Id == userId);

        var glance = new GlanceCardDto
        {
            CurrentConsumption = Math.Round(currentTotal, 2),
            ForecastedConsumption = Math.Round(nextAmount, 2),
            ConsumptionTrend = trend,
            ForecastedConsumptionTrend = changep,
            Currency = "USD",
            FirstName = user.FirstName
        };

        var utilities = currentBills.Select(b =>
        {
            var prev = previousBills.FirstOrDefault(x => x.Type == b.Type);

            double pastChange;
            if (prev == null || prev.Cost == 0)pastChange = 0;
            else pastChange = ((b.Cost - prev.Cost) / prev.Cost) * 100;

            if (pastChange > 100) pastChange = 100;
            if (pastChange < -100) pastChange = -100;
            pastChange = Math.Round(pastChange, 1);

            var utilityWeekly = weeklyRaw
                .Where(u => u.BillId == b.Id)
                .GroupBy(u => u.WeekStart)
                .OrderByDescending(g => g.Key)
                .Take(5)
                .OrderBy(g => g.Key)
                .Select(g => g.Sum(x => x.Amount))
                .ToList();

            double utilityTrend = 0;
            if (utilityWeekly.Count >= 2 && utilityWeekly.First() > 0)
                utilityTrend = ((utilityWeekly.Last() - utilityWeekly.First()) / utilityWeekly.First()) * 100;
            utilityTrend = Math.Round(utilityTrend, 1);

            double utilityForecast = b.Cost * (1 + utilityTrend / 100);
            double forecastChange = utilityTrend;
            forecastChange = Math.Round(forecastChange, 1);

            return new UtilityDto
            {
                Id = b.Id.ToString(),
                Name = b.Name,
                Status = b.IsPaid ? "Paid" : "Pending",
                Type = b.Type,
                Currency = b.Currency,
                Cost = Math.Round(b.Cost, 2),

                Consumption = Math.Round(b.Consumption, 2),
                ConsumptionUnit = b.ConsumptionUnit,

                NextBillDate = b.NextBillDate.ToString("o"),
                PricePerUnit = b.PricePerUnit,

                StartDate = b.StartDate,

                PastMonthChange = pastChange,
                ForecastChange = forecastChange
            };
        }).ToList();

        if (!currentBills.Any())
        {
            return Ok(new DashboardResponse
            {
                GlanceCard = new GlanceCardDto
                {
                    CurrentConsumption = 0,
                    ForecastedConsumption = 0,
                    ConsumptionTrend = 0,
                    ForecastedConsumptionTrend = 0,
                    Currency = "USD",
                    FirstName = user.FirstName
                },
                Utilities = new List<UtilityDto>(),
                ChartData = new List<double>(),
                TopExpenses = new List<ExpenseDto>(),
                Insights = new InsightsDto
                {
                    Values = new List<float>(),
                    MonthValue = 0,
                    ChangePercentage = 0
                },
                Forecast = new ForecastDto
                {
                    NextMonthAmount = 0,
                    PercentageChange = 0
                },
                ForecastDetails = new ForecastDetailsDto{Items =  new List<MonthlyForecastItemDto>()}
            });
        }
        return Ok(new DashboardResponse
        {
            GlanceCard = glance,
            Utilities = utilities,
            ChartData = weekly,
            TopExpenses = topExpenses,
            Insights = insights,
            Forecast = forecastDto,
            ForecastDetails = new ForecastDetailsDto{Items = forecastItems}
        });
    }
    }
}