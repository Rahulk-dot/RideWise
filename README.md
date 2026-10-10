# RideWise — Modular Ride-Sharing System

A dependency-free Java console application demonstrating OOP, SOLID principles, and Strategy-pattern-based ride matching and fare calculation.

## Requirements
- JDK 17 or later
- Git (for publishing to GitHub)

## Run
From the project root:

```bash
javac -d out $(find src/main/java -name "*.java")
java -cp out com.ridewise.app.Main
```

On Windows PowerShell, compile with:

```powershell
Get-ChildItem -Recurse src/main/java -Filter *.java | ForEach-Object { $_.FullName } | Set-Content sources.txt
javac -d out @sources.txt
java -cp out com.ridewise.app.Main
```

## Menu
1. Add Rider
2. Add Driver
3. View Available Drivers
4. Request Ride
5. Complete Ride
6. View Rides / Fare Receipts
7. Change Matching and Fare Strategies
8. Exit

Locations are represented as simple X/Y coordinates so the nearest-driver strategy can calculate Euclidean distance. Trip distance is entered in kilometres.

## Design and packages
- `model/`: Rider, Driver, Ride, Location, FareReceipt, RideStatus, VehicleType
- `service/`: RiderService, DriverService, RideService
- `strategy/`: RideMatchingStrategy, NearestDriverStrategy, LeastActiveDriverStrategy, FareStrategy, DefaultFareStrategy, PeakHourFareStrategy
- `app/`: Console menu and input handling (`Main`)

## SOLID and design principles
- **SRP:** Entities hold domain state; services manage riders, drivers, and rides; strategies calculate matching or fares.
- **OCP:** New matching/fare algorithms can be added through the strategy interfaces without rewriting ride lifecycle logic.
- **LSP:** Concrete strategies can replace their interfaces while honoring their contracts.
- **ISP:** Separate, focused `RideMatchingStrategy` and `FareStrategy` interfaces.
- **DIP:** `RideService` receives strategy interfaces through its constructor.
- **DRY:** Driver availability and ride allocation are handled centrally in services.
- **KISS/YAGNI:** In-memory collections and a simple console UI keep the MVP small.
- **Law of Demeter:** Services use direct collaborators and domain methods rather than long chains for orchestration.

## Pricing
- Default fare: ₹50 base fare + ₹12 per kilometre.
- Peak-hour strategy applies a 1.5x multiplier during 07:00–10:00 and 17:00–20:00; outside those windows it uses the base fare.

## Notes / scope
- Data is held in memory and resets when the application exits; no database is required for this MVP.
- If no driver is available, a ride remains `REQUESTED`.
- A fare receipt is generated when an assigned ride is completed.
- Strategy selection affects rides requested/completed after the new `RideService` configuration is applied.
