package com.ridewise.app;

import com.ridewise.model.*;
import com.ridewise.service.*;
import com.ridewise.strategy.*;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static final Scanner SCANNER = new Scanner(System.in);
    private static final RiderService RIDER_SERVICE = new RiderService();
    private static final DriverService DRIVER_SERVICE = new DriverService();
    private static RideMatchingStrategy matchingStrategy = new NearestDriverStrategy();
    private static FareStrategy fareStrategy = new DefaultFareStrategy();
    private static RideService rideService = new RideService(DRIVER_SERVICE, matchingStrategy, fareStrategy);

    public static void main(String[] args) {
        System.out.println("=== RideWise: Modular Ride-Sharing System ===");
        boolean running = true;
        while (running) {
            printMenu();
            int choice = readInt("Choose an option: ");
            try {
                switch (choice) {
                    case 1 -> addRider();
                    case 2 -> addDriver();
                    case 3 -> viewAvailableDrivers();
                    case 4 -> requestRide();
                    case 5 -> completeRide();
                    case 6 -> viewRides();
                    case 7 -> chooseStrategies();
                    case 8 -> { running = false; System.out.println("Thank you for using RideWise."); }
                    default -> System.out.println("Invalid option. Choose 1-8.");
                }
            } catch (IllegalArgumentException | IllegalStateException ex) {
                System.out.println("Error: " + ex.getMessage());
            }
            System.out.println();
        }
        SCANNER.close();
    }
    private static void printMenu() {
        System.out.println("\n1. Add Rider\n2. Add Driver\n3. View Available Drivers\n4. Request Ride" +
                "\n5. Complete Ride\n6. View Rides / Fare Receipts\n7. Change Strategies\n8. Exit");
    }
    private static void addRider() {
        String name = readText("Rider name: ");
        Location location = readLocation();
        System.out.println("Registered: " + RIDER_SERVICE.registerRider(name, location));
    }
    private static void addDriver() {
        String name = readText("Driver name: ");
        Location location = readLocation();
        VehicleType type = readVehicleType();
        System.out.println("Registered: " + DRIVER_SERVICE.registerDriver(name, location, type));
    }
    private static void viewAvailableDrivers() {
        List<Driver> drivers = DRIVER_SERVICE.getAvailableDrivers();
        if (drivers.isEmpty()) System.out.println("No available drivers.");
        else drivers.forEach(System.out::println);
    }
    private static void requestRide() {
        int riderId = readInt("Rider ID: ");
        Rider rider = RIDER_SERVICE.getRiderById(riderId);
        double distance = readDouble("Trip distance (km): ");
        Ride ride = rideService.requestRide(rider, distance);
        System.out.println("Ride created: " + ride);
        if (ride.getStatus() == RideStatus.REQUESTED)
            System.out.println("No driver available right now. The ride remains REQUESTED.");
    }
    private static void completeRide() {
        int rideId = readInt("Ride ID to complete: ");
        System.out.println("Ride completed. " + rideService.completeRide(rideId));
    }
    private static void viewRides() {
        if (rideService.getAllRides().isEmpty()) System.out.println("No rides yet.");
        else rideService.getAllRides().forEach(System.out::println);
        if (!rideService.getAllReceipts().isEmpty()) {
            System.out.println("Fare receipts:");
            rideService.getAllReceipts().forEach(System.out::println);
        }
    }
    private static void chooseStrategies() {
        System.out.println("Matching: 1. Nearest driver  2. Least active driver");
        int match = readInt("Choose matching strategy: ");
        matchingStrategy = switch (match) {
            case 1 -> new NearestDriverStrategy();
            case 2 -> new LeastActiveDriverStrategy();
            default -> throw new IllegalArgumentException("Invalid matching strategy.");
        };
        System.out.println("Fare: 1. Default  2. Peak-hour pricing");
        int fare = readInt("Choose fare strategy: ");
        fareStrategy = switch (fare) {
            case 1 -> new DefaultFareStrategy();
            case 2 -> new PeakHourFareStrategy();
            default -> throw new IllegalArgumentException("Invalid fare strategy.");
        };
        rideService.setMatchingStrategy(matchingStrategy);
        rideService.setFareStrategy(fareStrategy);
        System.out.println("Strategies updated. New strategy settings apply to future rides.");
    }
    private static Location readLocation() {
        double x = readDouble("Location X coordinate: ");
        double y = readDouble("Location Y coordinate: ");
        return new Location(x, y);
    }
    private static VehicleType readVehicleType() {
        while (true) {
            String value = readText("Vehicle type (BIKE/AUTO/CAR): ").toUpperCase();
            try { return VehicleType.valueOf(value); }
            catch (IllegalArgumentException ex) { System.out.println("Enter BIKE, AUTO, or CAR."); }
        }
    }
    private static String readText(String prompt) {
        while (true) {
            System.out.print(prompt);
            String value = SCANNER.nextLine().trim();
            if (!value.isEmpty()) return value;
            System.out.println("Input cannot be empty.");
        }
    }
    private static int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            try { return Integer.parseInt(SCANNER.nextLine().trim()); }
            catch (NumberFormatException ex) { System.out.println("Please enter a whole number."); }
        }
    }
    private static double readDouble(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                double value = Double.parseDouble(SCANNER.nextLine().trim());
                if (Double.isFinite(value)) return value;
            } catch (NumberFormatException ignored) { }
            System.out.println("Please enter a valid number.");
        }
    }
}
