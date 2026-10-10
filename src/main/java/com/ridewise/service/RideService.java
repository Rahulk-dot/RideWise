package com.ridewise.service;

import com.ridewise.model.*;
import com.ridewise.strategy.FareStrategy;
import com.ridewise.strategy.RideMatchingStrategy;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class RideService {
    private final DriverService driverService;
    private RideMatchingStrategy matchingStrategy;
    private FareStrategy fareStrategy;
    private final Map<Integer, Ride> rides = new LinkedHashMap<>();
    private final Map<Integer, FareReceipt> receipts = new LinkedHashMap<>();
    private int nextId = 1;

    public RideService(DriverService driverService, RideMatchingStrategy matchingStrategy, FareStrategy fareStrategy) {
        this.driverService = driverService; this.matchingStrategy = matchingStrategy; this.fareStrategy = fareStrategy;
    }
    public void setMatchingStrategy(RideMatchingStrategy matchingStrategy) { this.matchingStrategy = matchingStrategy; }
    public void setFareStrategy(FareStrategy fareStrategy) { this.fareStrategy = fareStrategy; }

    public Ride requestRide(Rider rider, double distance) {
        if (distance <= 0) throw new IllegalArgumentException("Distance must be greater than zero.");
        Ride ride = new Ride(nextId++, rider, distance);
        Driver driver = matchingStrategy.findDriver(rider, driverService.getAvailableDrivers());
        if (driver != null) { ride.setDriver(driver); ride.setStatus(RideStatus.ASSIGNED); driver.rideStarted(); }
        rides.put(ride.getId(), ride);
        return ride;
    }
    public FareReceipt completeRide(int rideId) {
        Ride ride = getRideById(rideId);
        if (ride.getStatus() != RideStatus.ASSIGNED || ride.getDriver() == null)
            throw new IllegalStateException("Only an assigned ride can be completed.");
        ride.setStatus(RideStatus.COMPLETED);
        ride.getDriver().rideCompleted();
        FareReceipt receipt = new FareReceipt(ride.getId(), fareStrategy.calculateFare(ride));
        receipts.put(ride.getId(), receipt);
        return receipt;
    }
    public void cancelRide(int rideId) {
        Ride ride = getRideById(rideId);
        if (ride.getStatus() == RideStatus.COMPLETED || ride.getStatus() == RideStatus.CANCELLED)
            throw new IllegalStateException("Ride is already completed or cancelled.");
        if (ride.getDriver() != null) ride.getDriver().rideCompleted();
        ride.setStatus(RideStatus.CANCELLED);
    }
    public Ride getRideById(int id) {
        Ride ride = rides.get(id);
        if (ride == null) throw new IllegalArgumentException("Ride ID not found: " + id);
        return ride;
    }
    public Collection<Ride> getAllRides() { return rides.values(); }
    public Collection<FareReceipt> getAllReceipts() { return receipts.values(); }
}
