public class PaymentBill
{
    public int PaymentId { get; set; }
    public int BillId { get; set; }

    public Payment Payment { get; set; }
    public Bill Bill { get; set; }
}