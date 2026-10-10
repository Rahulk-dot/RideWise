package com.ridewise.service;

import com.ridewise.model.Driver;
import com.ridewise.model.Location;
import com.ridewise.model.VehicleType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class DriverService {
    private final Map<Integer, Driver> drivers = new LinkedHashMap<>();
    private int nextId = 1;
    public Driver registerDriver(String name, Location location, VehicleType vehicleType) {
        Driver driver = new Driver(nextId++, name, location, vehicleType); drivers.put(driver.getId(), driver); return driver;
    }
    public Driver getDriverById(int id) {
        Driver driver = drivers.get(id);
        if (driver == null) throw new IllegalArgumentException("Driver ID not found: " + id);
        return driver;
    }
    public void updateAvailability(int id, boolean available) { getDriverById(id).setAvailable(available); }
    public List<Driver> getAvailableDrivers() {
        List<Driver> result = new ArrayList<>();
        for (Driver driver : drivers.values()) if (driver.isAvailable()) result.add(driver);
        return result;
    }
    public Collection<Driver> getAllDrivers() { return drivers.values(); }
}
