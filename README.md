# ZakTrak - Zakat Management App
![ZakTrak Logo](zaktrak-frontend/public/ZakTrak-logo.png)
## Overview
ZakTrak is a comprehensive Zakat management application designed to simplify the process of calculating, tracking, and fulfilling Zakat obligations for modern Muslims. 

🔗 <a href="https://zak-trak.vercel.app/" target="_blank">Visit ZakTrak</a>  
📊 <a href="https://docs.google.com/presentation/d/e/2PACX-1vTjmMoNjGvO321FD4nrA-B7i0n7BQUObUOhSyZeXyItMcTULZeFyYAN8rhprbZI0yAw3qzpSwO46ty9/pub?start=false&loop=false&delayms=60000" target="_blank">View Presentation</a>

## Problem Statement
In today's complex financial landscape, calculating Zakat accurately has become increasingly challenging:
- Multiple asset types and investment vehicles
- Time-consuming manual calculations
- Risk of errors affecting religious obligations
- Need for continuous tracking and management

## Features

### Asset Management
- Track multiple asset categories:
 - Cash and Savings
 - Investments
 - Gold and Silver
 - Business Assets

### Zakat Calculation
- Automated calculations based on current Nisab thresholds
- Support for both Gold and Silver Nisab standards
- Real-time asset evaluation

### Payment Tracking
- Record and monitor Zakat payments
- Track progress towards total obligation
- Payment history and reporting

### User Experience
- Intuitive dashboard interface
- Mobile-responsive design
- Multi-currency support
- Secure authentication

## Technology Stack

### Frontend
- Next.js 14
- React
- Tailwind CSS
- ShadCN UI Components
- Axios for API integration

### Backend
- Spring Boot 3.2.4
- JWT Authentication
- MongoDB Database
- RESTful API Architecture

### Deployment
- Frontend: Vercel
- Backend: Oracle Cloud VM Instance

## Getting Started

### Prerequisites
- Node.js (v18.18.0 or higher)
- npm
- Java 17
- MongoDB

### Installation

1. Clone the repository
```bash
git clone https://github.com/yourusername/ZakTrak.git
```

## Frontend Setup
```
cd zaktrak-frontend
npm install
npm run dev
```

## Backend Setup

```
cd zaktrak-backend
./mvnw spring-boot: run
```

### API Documentation

The backend API provides endpoints for:
 - User Authentication
- Asset Management
- Zakat Calculations
- Payment Tracking

### Future Development

- Mobile App Expansion
- Include Liabilities
- Expanded Asset Coverage
- Stocks and Shares Integration
