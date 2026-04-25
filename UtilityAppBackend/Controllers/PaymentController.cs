using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;
using System.Security.Claims;

[ApiController]
[Route("api/payments")]
public class PaymentsController : ControllerBase
{
    private readonly AppDbContext _context;

    public PaymentsController(AppDbContext context)
    {
        _context = context;
    }

    // 🔹 GET CARDS
    [Authorize]
    [HttpGet("methods")]
    public IActionResult GetCards()
    {
        var userId = int.Parse(User.FindFirst(ClaimTypes.NameIdentifier)!.Value);

        var cards = _context.PaymentCards
            .Where(c => c.UserId == userId)
            .Select(c => new
            {
                id = c.Id,
                holderName = c.HolderName,
                last4 = c.Last4,
                expMonth = c.ExpMonth,
                expYear = c.ExpYear,
                brand = c.Brand,
                isDefault = c.IsDefault
            })
            .ToList();

        return Ok(cards);
    }

    [Authorize]
    [HttpPost("methods")]
    public IActionResult AddCard([FromBody] AddCardRequest request)
    {
        var userId = int.Parse(User.FindFirst(ClaimTypes.NameIdentifier)!.Value);

        if (request.CardNumber.Length < 4)
            return BadRequest(new { message = "Invalid card" });
        
        if (string.IsNullOrWhiteSpace(request.HolderName))
            return BadRequest("Card holder name is required");

        if (request.CardNumber.Length < 12 || request.CardNumber.Length > 19)
            return BadRequest("Invalid card number");

        if (request.ExpMonth < 1 || request.ExpMonth > 12)
            return BadRequest("Invalid expiration month");

        var now = DateTime.UtcNow;
        var expDate = new DateTime(request.ExpYear, request.ExpMonth, 1).AddMonths(1);

        if (expDate < now)
            return BadRequest("Card is expired");

        var card = new PaymentCard
        {
            UserId = userId,
            HolderName = request.HolderName,
            Last4 = request.CardNumber[^4..],
            ExpMonth = request.ExpMonth,
            ExpYear = request.ExpYear,
            IsDefault = !_context.PaymentCards.Any(c => c.UserId == userId)
        };

        _context.PaymentCards.Add(card);
        _context.SaveChanges();

        return Ok(new { message = "Card added" });
    }

    // 🔹 PAY BILLS
    [Authorize]
    [HttpPost("pay")]
    public IActionResult PayBills([FromBody] PayRequest request)
    {
        var userId = int.Parse(User.FindFirst(ClaimTypes.NameIdentifier)!.Value);

        var bills = _context.Bills
            .Where(b => request.BillIds.Contains(b.Id) && b.UserId == userId && !b.IsPaid)
            .ToList();

        if (!bills.Any())
            return BadRequest(new { message = "No valid bills" });
        
        if (bills.Any(b => b.IsPaid))
            return BadRequest("Some bills are already paid");

        using var transaction = _context.Database.BeginTransaction();

        var total = bills.Sum(b => b.Cost);
        try {

        
            var payment = new Payment
            {
                UserId = userId,
                Amount = total,
                Status = "SUCCESS",
                CreatedAt = DateTime.UtcNow
            };

            _context.Payments.Add(payment);
            _context.SaveChanges();

            foreach (var bill in bills)
            {
                bill.IsPaid = true;

                _context.PaymentBills.Add(new PaymentBill
                {
                    PaymentId = payment.Id,
                    BillId = bill.Id
                });
            }

            _context.SaveChanges();

            transaction.Commit();
        }
       catch (Exception ex)
    {
        transaction.Rollback();

        return BadRequest(new
        {
            message = "Payment failed",
            error = ex.InnerException?.Message ?? ex.Message
        });
    }


        return Ok(new
        {
            totalAmount = total,
            billCount = bills.Count,
            status = "SUCCESS"
        });
    }

    [Authorize]
    [HttpPost("default")]
    public IActionResult SetDefault([FromBody] SetDefaultRequest request)
    {
        var userId = int.Parse(User.FindFirst(ClaimTypes.NameIdentifier)!.Value);

        var cards = _context.PaymentCards.Where(c => c.UserId == userId).ToList();

        foreach (var c in cards)
            c.IsDefault = false;

        var selected = cards.FirstOrDefault(c => c.Id == request.CardId);

        if (selected == null)
            return NotFound("Card not found");

        selected.IsDefault = true;

        _context.SaveChanges();

        return Ok();
    }
}