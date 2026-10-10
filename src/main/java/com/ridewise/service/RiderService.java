package com.ridewise.service;

import com.ridewise.model.Rider;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

public class RiderService {
    private final Map<Integer, Rider> riders = new LinkedHashMap<>();
    private int nextId = 1;
    public Rider registerRider(String name, com.ridewise.model.Location location) {
        Rider rider = new Rider(nextId++, name, location); riders.put(rider.getId(), rider); return rider;
    }
    public Rider getRiderById(int id) {
        Rider rider = riders.get(id);
        if (rider == null) throw new IllegalArgumentException("Rider ID not found: " + id);
        return rider;
    }
    public Collection<Rider> getAllRiders() { return riders.values(); }
}
