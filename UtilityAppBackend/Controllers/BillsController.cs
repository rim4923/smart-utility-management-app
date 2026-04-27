using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;
using System.Security.Claims;

namespace UtilityAppBackend.Controllers
{
    [ApiController]
    [Route("api/[controller]")]
    public class BillsController : ControllerBase
    {
        private readonly AppDbContext _context;

        public BillsController(AppDbContext context)
        {
            _context = context;
        }
        [Authorize]
        [HttpGet("dashboard")]
        public IActionResult GetBillsDashboard()
        {
            var userId = int.Parse(User.FindFirst(ClaimTypes.NameIdentifier)!.Value);

            var bills = _context.Bills
                .Where(b => b.UserId == userId)
                .OrderByDescending(b => b.NextBillDate)
                .ToList();

            var now = DateTime.UtcNow;

            var monthlyBills = bills.Where(b =>
                b.NextBillDate.Month == now.Month &&
                b.NextBillDate.Year == now.Year
            ).ToList();

            var totalDue = Math.Round(monthlyBills.Sum(b => b.Cost), 2);

            var totalPaid = Math.Round(
                monthlyBills.Where(b => b.IsPaid).Sum(b => b.Cost),
                2
            );

            var amountLeft = Math.Round(totalDue - totalPaid, 2);

            var utilities = bills.Select(b => new UtilityDto
            {
                Id = b.Id.ToString(),
                Name = b.Name,
                Status = b.IsPaid
                    ? "Paid"
                    : (b.NextBillDate < DateTime.UtcNow ? "Overdue" : "Pending"),
                Type = b.Type,
                Currency = b.Currency,
                Cost = Math.Round(b.Cost,2),

                Consumption = Math.Round(b.Consumption,2),
                ConsumptionUnit = b.ConsumptionUnit,
                NextBillDate = b.NextBillDate.ToString("o"),
                PricePerUnit = b.PricePerUnit,

                StartDate = b.StartDate,
                PastMonthChange = b.PastMonthChange != null
                    ? Math.Round(b.PastMonthChange.Value, 1)
                    : null,

                ForecastChange = b.ForecastChange != null
                    ? Math.Round(b.ForecastChange.Value, 1)
                    : null,
            }).ToList();

            var response = new BillsDashboardResponse
            {
                GlanceCard = new BillsGlanceResponse
                {
                    TotalAmountDue = totalDue,
                    AmountPaid = totalPaid,
                    AmountLeft = amountLeft,
                    NextBillDate = bills.FirstOrDefault()?.NextBillDate.ToString("o"),
                    Currency = bills.Any() ? bills.First().Currency : "USD"
                },
                Utilities = utilities
            };

            return Ok(response);
        }
        

        [Authorize]
        [HttpGet("{id}")]
        public IActionResult GetBillById(int id)
        {
            var userId = int.Parse(User.FindFirst(ClaimTypes.NameIdentifier).Value);

            var bill = _context.Bills
                .FirstOrDefault(b => b.Id == id && b.UserId == userId);

            if (bill == null)
                return NotFound(new { message = "Bill not found" });

            var response = new BillDetailsResponse
            {
                Id = bill.Id.ToString(),
                Type = bill.Type,
                Status = bill.IsPaid
                    ? "Paid"
                    : (bill.NextBillDate < DateTime.UtcNow ? "Overdue" : "Pending"),

                Cost = Math.Round(bill.Cost, 2),
                Currency = bill.Currency,

                NextBillDate = bill.NextBillDate,

                Consumption = Math.Round(bill.Consumption,2),
                ConsumptionUnit = bill.ConsumptionUnit,
                PricePerUnit = bill.PricePerUnit,

                WeeklyConsumption = _context.BillUsages
                                            .Where(u => u.BillId == bill.Id)
                                            .OrderByDescending(u => u.WeekStart)
                                            .Take(4)
                                            .OrderBy(u => u.WeekStart)
                                            .Select(u => (int)u.Amount)
                                            .ToList(),
                                            
                ProviderName = bill.ProviderName,
                ProviderPhone = bill.ProviderPhone,
                ProviderWebsite = bill.ProviderWebsite
            };

            return Ok(response);
        }

        [Authorize]
        [HttpPost("{id}/pay")]
        public IActionResult PaySingleBill(int id)
        {
            var userId = int.Parse(User.FindFirst(ClaimTypes.NameIdentifier).Value);

            var bill = _context.Bills
                .FirstOrDefault(b => b.Id == id && b.UserId == userId);

            if (bill == null)
                return NotFound(new { message = "Bill not found" });

            if (bill.IsPaid)
                return BadRequest(new { message = "Bill already paid" });

            bill.IsPaid = true;

            _context.SaveChanges();

            return Ok();
        }
    }
}