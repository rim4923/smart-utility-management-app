# UtiliTrack – Smart Utility Management System

## 📌 Overview
UtiliTrack is a mobile application designed to centralize utility management, allowing users to track, pay, and forecast their utility expenses in one place.

## 🚀 Features
- 🔐 Authentication (Multi-step signup + OCR identity verification)
- 📊 Dashboard with real-time insights
- 💳 Payment system with saved cards
- 📁 Bills management (history, status, filtering)
- 📈 Forecasting of future utility costs

## 🏗️ Architecture
- **Frontend:** Android (Jetpack Compose)
- **Backend:** ASP.NET Core Web API
- **Database:** SQL Server

## 📊 Forecasting
The system uses a hybrid forecasting model combining:
- Trend analysis (linear regression)
- Momentum (recent changes)
- Seasonal adjustments

## 🔒 Security
- JWT Authentication
- Secure API communication
- Input validation

## ▶️ How to Run

### Backend:
```bash
cd UtilityAppBackend
dotnet run
