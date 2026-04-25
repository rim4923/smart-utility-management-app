using Microsoft.EntityFrameworkCore;
using UtilityAppBackend.Models;


public class AppDbContext : DbContext
{
    public AppDbContext(DbContextOptions<AppDbContext> options) : base(options) {}

    protected override void OnModelCreating(ModelBuilder modelBuilder)
    {
        modelBuilder.Entity<PaymentBill>()
            .HasKey(pb => new { pb.PaymentId, pb.BillId });
            
        modelBuilder.Entity<PaymentBill>()
            .HasOne(pb => pb.Payment)
            .WithMany()
            .HasForeignKey(pb => pb.PaymentId);

        modelBuilder.Entity<PaymentBill>()
            .HasOne(pb => pb.Bill)
            .WithMany()
            .HasForeignKey(pb => pb.BillId);
    }

    public DbSet<User> Users { get; set; }
    public DbSet<BillUsage> BillUsages { get; set; }
    public DbSet<PaymentCard> PaymentCards { get; set; }
    public DbSet<Payment> Payments { get; set; }
    public DbSet<Bill> Bills { get; set; }
    public DbSet<PaymentBill> PaymentBills { get; set; }
}
