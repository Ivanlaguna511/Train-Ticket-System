package com.trainticketing.model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Core Model of the Train Ticketing System.
 * Responsible for loading and providing access to stations, routes, and trips.
 */
public class TrainModel {
    
    private List<String> stations;
    private List<Route> routes;
    
    // Relative paths based on standard VS Code Java project structure
    private static final String STATIONS_FILE_PATH = "src/resources/data/estaciones.csv";
    private static final String ROUTES_FILE_PATH = "src/resources/data/rutas.csv";

    public TrainModel() {
        this.stations = new ArrayList<>();
        this.routes = new ArrayList<>();
        loadStations();
        loadRoutes();
    }

    /**
     * Reads the stations from the CSV file and loads them into memory.
     */
    private void loadStations() {
        try (BufferedReader br = new BufferedReader(new FileReader(STATIONS_FILE_PATH))) {
            String line;
            boolean isFirstLine = true;
            
            while ((line = br.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue; // Skip header
                }
                stations.add(line.trim());
            }
        } catch (IOException e) {
            System.err.println("Error loading stations from CSV: " + e.getMessage());
        }
    }

    /**
     * Reads the routes from the CSV file and parses them into Route objects.
     */
    private void loadRoutes() {
        try (BufferedReader br = new BufferedReader(new FileReader(ROUTES_FILE_PATH))) {
            String line;
            boolean isFirstLine = true;
            
            while ((line = br.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue; // Skip header
                }
                
                String[] values = line.split(";");
                if (values.length < 7) continue; // Basic safety check
                
                List<String> weekdayTimes = Arrays.asList(values[5].split(","));
                List<String> weekendTimes = Arrays.asList(values[6].split(","));
                
                Route route = new Route(
                    values[0],                      // Route ID
                    values[1],                      // Origin
                    values[2],                      // Destination
                    values[3],                      // Duration
                    Double.parseDouble(values[4]),  // Price
                    weekdayTimes,                   
                    weekendTimes                    
                );
                
                routes.add(route);
            }
        } catch (IOException | NumberFormatException e) {
            System.err.println("Error loading routes from CSV: " + e.getMessage());
        }
    }

    public List<String> getStations() {
        return stations;
    }

    public List<Route> getRoutes() {
        return routes;
    }

    /**
     * Finds available trips based on origin, destination, and the day of the week.
     * * @param origin      Origin station name.
     * @param destination Destination station name.
     * @param date        Travel date.
     * @return List of available Trips.
     * @throws IllegalArgumentException if no trips are found for the criteria.
     */
    public List<Trip> getAvailableTrips(String origin, String destination, LocalDate date) {
        List<Trip> availableTrips = new ArrayList<>();
        boolean isWeekend = (date.getDayOfWeek() == DayOfWeek.SATURDAY || date.getDayOfWeek() == DayOfWeek.SUNDAY);

        for (Route route : routes) {
            if (route.getOriginStation().equals(origin) && route.getDestinationStation().equals(destination)) {
                List<String> schedules = isWeekend ? route.getWeekendSchedules() : route.getWeekdaySchedules();
                
                for (String time : schedules) {
                    availableTrips.add(new Trip(
                        route.getRouteId(), 
                        route.getDuration(), 
                        time, 
                        route.getPrice()
                    ));
                }
            }
        }

        if (availableTrips.isEmpty()) {
            throw new IllegalArgumentException("No routes found between " + origin + " and " + destination);
        }

        return availableTrips;
    }
}