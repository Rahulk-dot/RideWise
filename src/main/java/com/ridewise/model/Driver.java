package com.ridewise.model;

public class Driver {
    private final int id;
    private final String name;
    private Location currentLocation;
    private boolean available;
    private int completedRides;
    private int activeRides;
    private final VehicleType vehicleType;

    public Driver(int id, String name, Location location, VehicleType vehicleType) {
        this.id = id; this.name = name; this.currentLocation = location;
        this.vehicleType = vehicleType; this.available = true;
    }
    public int getId() { return id; }
    public String getName() { return name; }
    public Location getCurrentLocation() { return currentLocation; }
    public void setCurrentLocation(Location location) { this.currentLocation = location; }
    public boolean isAvailable() { return available; }
    public void setAvailable(boolean available) { this.available = available; }
    public int getCompletedRides() { return completedRides; }
    public int getActiveRides() { return activeRides; }
    public void rideStarted() { activeRides++; available = false; }
    public void rideCompleted() { if (activeRides > 0) activeRides--; completedRides++; available = true; }
    public VehicleType getVehicleType() { return vehicleType; }
    @Override public String toString() {
        return "Driver{id=" + id + ", name='" + name + "', vehicle=" + vehicleType +
                ", location=" + currentLocation + ", available=" + available + ", completedRides=" + completedRides + "}";
    }
}
