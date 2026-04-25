using System;
using Microsoft.EntityFrameworkCore.Migrations;

#nullable disable

namespace UtilityAppBackend.Migrations
{
    /// <inheritdoc />
    public partial class UpdateBillsModel : Migration
    {
        /// <inheritdoc />
        protected override void Up(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.AddColumn<double>(
                name: "Consumption",
                table: "Bills",
                type: "float",
                nullable: false,
                defaultValue: 0.0);

            migrationBuilder.AddColumn<string>(
                name: "ConsumptionUnit",
                table: "Bills",
                type: "nvarchar(max)",
                nullable: false,
                defaultValue: "");

            migrationBuilder.AddColumn<string>(
                name: "Currency",
                table: "Bills",
                type: "nvarchar(max)",
                nullable: false,
                defaultValue: "");

            migrationBuilder.AddColumn<double>(
                name: "ForecastChange",
                table: "Bills",
                type: "float",
                nullable: true);

            migrationBuilder.AddColumn<DateTime>(
                name: "NextBillDate",
                table: "Bills",
                type: "datetime2",
                nullable: false,
                defaultValue: new DateTime(1, 1, 1, 0, 0, 0, 0, DateTimeKind.Unspecified));

            migrationBuilder.AddColumn<double>(
                name: "PastMonthChange",
                table: "Bills",
                type: "float",
                nullable: true);

            migrationBuilder.AddColumn<string>(
                name: "PricePerUnit",
                table: "Bills",
                type: "nvarchar(max)",
                nullable: false,
                defaultValue: "");

            migrationBuilder.AddColumn<string>(
                name: "ProviderName",
                table: "Bills",
                type: "nvarchar(max)",
                nullable: false,
                defaultValue: "");

            migrationBuilder.AddColumn<string>(
                name: "ProviderPhone",
                table: "Bills",
                type: "nvarchar(max)",
                nullable: false,
                defaultValue: "");

            migrationBuilder.AddColumn<string>(
                name: "ProviderWebsite",
                table: "Bills",
                type: "nvarchar(max)",
                nullable: false,
                defaultValue: "");

            migrationBuilder.AddColumn<DateTime>(
                name: "StartDate",
                table: "Bills",
                type: "datetime2",
                nullable: false,
                defaultValue: new DateTime(1, 1, 1, 0, 0, 0, 0, DateTimeKind.Unspecified));

            migrationBuilder.AddColumn<string>(
                name: "Type",
                table: "Bills",
                type: "nvarchar(max)",
                nullable: false,
                defaultValue: "");
        }

        /// <inheritdoc />
        protected override void Down(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.DropColumn(
                name: "Consumption",
                table: "Bills");

            migrationBuilder.DropColumn(
                name: "ConsumptionUnit",
                table: "Bills");

            migrationBuilder.DropColumn(
                name: "Currency",
                table: "Bills");

            migrationBuilder.DropColumn(
                name: "ForecastChange",
                table: "Bills");

            migrationBuilder.DropColumn(
                name: "NextBillDate",
                table: "Bills");

            migrationBuilder.DropColumn(
                name: "PastMonthChange",
                table: "Bills");

            migrationBuilder.DropColumn(
                name: "PricePerUnit",
                table: "Bills");

            migrationBuilder.DropColumn(
                name: "ProviderName",
                table: "Bills");

            migrationBuilder.DropColumn(
                name: "ProviderPhone",
                table: "Bills");

            migrationBuilder.DropColumn(
                name: "ProviderWebsite",
                table: "Bills");

            migrationBuilder.DropColumn(
                name: "StartDate",
                table: "Bills");

            migrationBuilder.DropColumn(
                name: "Type",
                table: "Bills");
        }
    }
}
