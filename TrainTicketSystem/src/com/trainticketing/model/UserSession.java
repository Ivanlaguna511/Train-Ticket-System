package com.trainticketing.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Singleton class that acts as the central state/memory of the application.
 * Stores the user's current balance and purchased trips.
 */
public class UserSession {
    
    private static UserSession instance;
    private double balance;
    private List<String> purchasedTrips;

    private UserSession() {
        this.balance = 20.0; // Initial free balance for testing
        this.purchasedTrips = new ArrayList<>();
    }

    public static UserSession getInstance() {
        if (instance == null) {
            instance = new UserSession();
        }
        return instance;
    }

    public double getBalance() {
        return balance;
    }

    public void addBalance(double amount) {
        this.balance += amount;
    }

    public boolean deductBalance(double amount) {
        if (this.balance >= amount) {
            this.balance -= amount;
            return true;
        }
        return false;
    }

    public void addTrip(String tripDetails) {
        this.purchasedTrips.add(tripDetails);
    }

    public List<String> getPurchasedTrips() {
        return purchasedTrips;
    }

    public int getTripsCount() {
        return purchasedTrips.size();
    }

    public void removeTrip(String tripDetails) {
        this.purchasedTrips.remove(tripDetails);
    }
}