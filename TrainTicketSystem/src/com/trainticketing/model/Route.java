package com.trainticketing.model;

import java.util.List;

/**
 * Represents a train route connecting an origin and destination station.
 */
public class Route {
    
    private String routeId;
    private String originStation;
    private String destinationStation;
    private String duration;
    private double price;
    private List<String> weekdaySchedules;
    private List<String> weekendSchedules;

    public Route(String routeId, String originStation, String destinationStation, 
                 String duration, double price, List<String> weekdaySchedules, 
                 List<String> weekendSchedules) {
        this.routeId = routeId;
        this.originStation = originStation;
        this.destinationStation = destinationStation;
        this.duration = duration;
        this.price = price;
        this.weekdaySchedules = weekdaySchedules;
        this.weekendSchedules = weekendSchedules;
    }

    public String getRouteId() {
        return routeId;
    }

    public String getOriginStation() {
        return originStation;
    }

    public String getDestinationStation() {
        return destinationStation;
    }

    public String getDuration() {
        return duration;
    }

    public double getPrice() {
        return price;
    }

    public List<String> getWeekdaySchedules() {
        return weekdaySchedules;
    }

    public List<String> getWeekendSchedules() {
        return weekendSchedules;
    }
}