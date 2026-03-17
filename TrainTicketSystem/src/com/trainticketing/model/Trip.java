package com.trainticketing.model;

/**
 * Represents a specific train trip with its duration, departure time, and price.
 */
public class Trip {
    
    private String tripId;
    private String duration;
    private String departureTime;
    private double price;

    public Trip(String tripId, String duration, String departureTime, double price) {
        this.tripId = tripId;
        this.duration = duration;
        this.departureTime = departureTime;
        this.price = price;
    }

    public String getTripId() {
        return tripId;
    }

    public String getDuration() {
        return duration;
    }

    public String getDepartureTime() {
        return departureTime;
    }

    public double getPrice() {
        return price;
    }

    @Override
    public String toString() {
        return "Trip ID: " + tripId + " | Departure: " + departureTime + 
               " | Duration: " + duration + " min | Price: " + price + "€";
    }
}