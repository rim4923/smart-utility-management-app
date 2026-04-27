# UtiliTrack – Smart Utility Management System

## 📌 Overview
UtiliTrack is a mobile application designed to centralize utility management, allowing users to track, pay, and forecast their utility expenses in one place.  
The system integrates real-time insights with machine learning–based forecasting to support smarter financial decisions.

## 🚀 Features
- 🔐 Authentication (Multi-step signup + OCR identity verification)
- 📊 Dashboard with real-time insights
- 💳 Payment system with saved cards
- 📁 Bills management (history, status, filtering)
- 📈 AI-based forecasting of future utility costs
- 📉 Weekly insights and consumption trends

## 🏗️ Architecture
- **Frontend:** Android (Jetpack Compose)
- **Backend:** ASP.NET Core Web API
- **Database:** SQL Server

## 📊 Forecasting
The system uses a real time-series forecasting model based on:
- SSA (Singular Spectrum Analysis) using ML.NET
- Monthly historical data aggregation
- Seasonal pattern detection (12-month cycle)
- Per-utility forecasting (Electricity, Water, Gas, Internet, Phone)
- Aggregated total prediction across utilities
- 3-month forward forecasting
- Percentage change calculations between months

## 📉 Insights
- Weekly consumption tracking
- Trend detection (increase / decrease)
- Data-driven visualization

## 🔒 Security
- JWT Authentication
- Secure API communication
- Input validation

## ▶️ How to Run

### Backend:
```bash
cd UtilityAppBackend
dotnet
