using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;
using System.Security.Claims;
using UtilityAppBackend.Services;

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
        private readonly ForecastService _forecastService;

        public DashboardController(AppDbContext context, ForecastService forecastService)
        {
            _context = context;
            _forecastService = forecastService;
        }
        private double SafeDivide(double numerator, double denominator)
        {
            if (denominator == 0) return 0;
            return numerator / denominator;
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
                b.StartDate.Month == currentMonth &&
                b.StartDate.Year == currentYear
            ).ToList();

            var previousBills = bills.Where(b =>
                b.StartDate.Month == previousMonth &&
                b.StartDate.Year == previousYear
            ).ToList();

            var currentTotal = currentBills.Sum(b => b.Cost);
            var previousTotal = previousBills.Sum(b => b.Cost);

            double trend = previousTotal <= 0 ? 0 :
                SafeDivide(currentTotal - previousTotal, previousTotal) * 100;

            trend = Math.Clamp(Math.Round(trend, 1), -100, 100);

            // ======================
            // FORECAST
            // ======================

            var utilityForecastMap = new Dictionary<string, float[]>();

            double totalCurrentMonth = currentTotal;
            double totalNextMonthForecast = 0;

            // GROUP PER UTILITY
            var grouped = bills
                .Where(b => b.Consumption > 0)
                .GroupBy(b => b.Type)
                .ToList();

            foreach (var group in grouped)
            {
                var monthly = group
                    .GroupBy(b => new { b.StartDate.Year, b.StartDate.Month })
                    .Select(g => new
                    {
                        Date = new DateTime(g.Key.Year, g.Key.Month, 1),
                        Total = g.Sum(x => x.Cost)
                    })
                    .OrderBy(x => x.Date)
                    .ToList();

                var values = monthly.Select(x => x.Total).ToList();

                var prediction = _forecastService.ForecastNextMonths(values, group.Key);

                utilityForecastMap[group.Key] = prediction;

                totalNextMonthForecast += prediction[0]; // only next month
            }

            var forecastItems = new List<MonthlyForecastItemDto>();

            var last3Months = bills
                .GroupBy(b => new { b.StartDate.Year, b.StartDate.Month })
                .Select(g => new
                {
                    Date = new DateTime(g.Key.Year, g.Key.Month, 1),
                    Total = g.Sum(x => x.Cost)
                })
                .OrderBy(x => x.Date)
                .TakeLast(3)
                .ToList();
            
            
            double forecastChange = SafeDivide(
                totalNextMonthForecast - totalCurrentMonth,
                totalCurrentMonth
            ) * 100;

            forecastChange = Math.Clamp(Math.Round(forecastChange, 1), -100, 100);

            foreach (var m in last3Months)
            {
                forecastItems.Add(new MonthlyForecastItemDto
                {
                    Month = m.Date.ToString("MMMM"),
                    Amount = Math.Round(m.Total, 2),
                    IsForecast = false,
                    ChangePercentage = 0
                });
            }

            // FORECAST (SUM OF UTILITIES)
            double prev = last3Months.Last().Total;

            for (int i = 0; i < 3; i++)
            {
                double monthForecast = utilityForecastMap.Sum(x => x.Value[i]);

                double change = SafeDivide(monthForecast - prev, prev) * 100;

                forecastItems.Add(new MonthlyForecastItemDto
                {
                    Month = now.AddMonths(i + 1).ToString("MMMM"),
                    Amount = Math.Round(monthForecast, 2),
                    IsForecast = true,
                    ChangePercentage = Math.Round(change, 1)
                });

                prev = monthForecast;
            }

            var forecastDto = new ForecastDto
            {
                NextMonthAmount = Math.Round(totalNextMonthForecast, 2),
                PercentageChange = forecastChange
            };

            var weeklyRaw = _context.BillUsages
                .Where(u => u.Bill.UserId == userId)
                .OrderBy(u => u.WeekStart)
                .ToList();

            var weekly = weeklyRaw
                .GroupBy(u => u.WeekStart.Date)
                .Select(g => new {
                    Date = g.Key,
                    Total = g.Sum(x => x.Amount)
                })
                .OrderBy(x => x.Date) 
                .TakeLast(5)
                .Select(x => x.Total)
                .ToList();

            

            if (weekly.Count < 4)
                weekly = new List<double> { currentTotal / 4, currentTotal / 4, currentTotal / 4, currentTotal / 4 };

            double weeklyTrend = 0;

            if (weekly.Count >= 2 && weekly.First() > 0)
               weeklyTrend = SafeDivide(weekly.Last() - weekly.First(), weekly.First()) * 100;

            weeklyTrend = Math.Round(weeklyTrend, 1);
            weeklyTrend = Math.Clamp(Math.Round(weeklyTrend, 1), -100, 100);

            var insights = new InsightsDto
            {
                Values = weekly.Select(w => (float)w).TakeLast(5).ToList(),
                MonthValue = Math.Round(currentTotal, 2),
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

            var user = _context.Users.First(u => u.Id == userId);

            var glance = new GlanceCardDto
            {
                CurrentConsumption = Math.Round(currentTotal, 2),
                ForecastedConsumption =Math.Round(totalNextMonthForecast, 2),
                ConsumptionTrend = trend,
                ForecastedConsumptionTrend = forecastChange,
                Currency = "USD",
                FirstName = user.FirstName
            };
            
           var utilities = currentBills.Select(b =>
        {
            var previous = previousBills
                .FirstOrDefault(x => x.Type == b.Type);

            double pastChange = 0;

            if (previous != null && previous.Cost > 0)
            {
                pastChange = SafeDivide(b.Cost - previous.Cost, previous.Cost) * 100;
            }

            pastChange = Math.Clamp(Math.Round(pastChange, 1), -100, 100);

            float[] forecast = utilityForecastMap.ContainsKey(b.Type)
                ? utilityForecastMap[b.Type]
                : new float[] { 0, 0, 0 };

            double nextMonthForecast = forecast[0];

            double utilityForecastChange = SafeDivide(
                nextMonthForecast - b.Cost,
                b.Cost
            ) * 100;

            utilityForecastChange = Math.Clamp(Math.Round(utilityForecastChange, 1), -100, 100);

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
                ForecastChange = utilityForecastChange 
            };
        }).ToList();

            if (!currentBills.Any())
            {
                return Ok(new DashboardResponse
                {
                    GlanceCard = glance,
                    Utilities = new List<UtilityDto>(),
                    ChartData = new List<double>(),
                    TopExpenses = new List<ExpenseDto>(),
                    Insights = new InsightsDto
                    {
                        Values = new List<float>(),
                        MonthValue = 0,
                        ChangePercentage = 0
                    },
                    Forecast = forecastDto,
                    ForecastDetails = new ForecastDetailsDto
                {
                    Items = new List<MonthlyForecastItemDto>()
                }
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