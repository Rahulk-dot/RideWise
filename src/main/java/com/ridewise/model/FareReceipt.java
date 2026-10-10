package com.ridewise.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class FareReceipt {
    private final int rideId;
    private final double amount;
    private final LocalDateTime generatedAt;

    public FareReceipt(int rideId, double amount) {
        this.rideId = rideId; this.amount = amount; this.generatedAt = LocalDateTime.now();
    }
    public int getRideId() { return rideId; }
    public double getAmount() { return amount; }
    public LocalDateTime getGeneratedAt() { return generatedAt; }
    @Override public String toString() {
        return String.format("FareReceipt{rideId=%d, amount=₹%.2f, generatedAt=%s}", rideId, amount,
                generatedAt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
    }
}
