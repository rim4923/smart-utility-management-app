public class BillsGlanceResponse
{
    public double? TotalAmountDue { get; set; }
    public double? AmountPaid { get; set; }
    public double? AmountLeft { get; set; }
    public string? NextBillDate { get; set; }
    public string? Currency { get; set; }
}