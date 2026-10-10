package com.ridewise.strategy;

import com.ridewise.model.Ride;
import java.time.LocalTime;

public class PeakHourFareStrategy implements FareStrategy {
    private final FareStrategy baseStrategy;
    private final double multiplier;
    public PeakHourFareStrategy() { this(new DefaultFareStrategy(), 1.5); }
    public PeakHourFareStrategy(FareStrategy baseStrategy, double multiplier) {
        if (multiplier < 1.0) throw new IllegalArgumentException("Multiplier must be at least 1.0");
        this.baseStrategy = baseStrategy; this.multiplier = multiplier;
    }
    @Override public double calculateFare(Ride ride) {
        LocalTime now = LocalTime.now();
        boolean peak = (!now.isBefore(LocalTime.of(7, 0)) && now.isBefore(LocalTime.of(10, 0))) ||
                (!now.isBefore(LocalTime.of(17, 0)) && now.isBefore(LocalTime.of(20, 0)));
        double baseFare = baseStrategy.calculateFare(ride);
        return peak ? baseFare * multiplier : baseFare;
    }
}
