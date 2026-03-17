package com.trainticketing.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.trainticketing.model.TrainModel;
import com.trainticketing.model.Trip;
import com.trainticketing.model.UserSession;
import com.trainticketing.view.BuyTicketView;

public class BuyTicketController {
    
    private BuyTicketView view;
    private TrainModel model;

    public BuyTicketController(BuyTicketView view) {
        this.view = view;
        this.model = new TrainModel(); 
    }

    public List<String> getStations() {
        return model.getStations();
    }

    public void handleStepOneContinue(String origin, String destination, LocalDate date) {
        if (origin.equals(destination)) {
            view.showStepOneError("Origin and destination cannot be the same.");
            return;
        }
        if (date.isBefore(LocalDate.now())) {
            view.showStepOneError("You cannot select a past date.");
            return;
        }

        try {
            List<Trip> availableTrips = model.getAvailableTrips(origin, destination, date);
            List<String> displayTrips = new ArrayList<>();
            for (Trip trip : availableTrips) {
                displayTrips.add(trip.toString());
            }
            view.populateTripsList(displayTrips);
            
            if (view.getListedTripsCount() == 0) {
                view.showStepTwoError("No trips available for this route.");
            }
            view.goToStepTwo();
        } catch (IllegalArgumentException e) {
            view.showStepOneError(e.getMessage());
        }
    }

    public void handleStepTwoContinue() {
        if (view.getListedTripsCount() == 0) {
            view.showStepTwoError("No trips available. Please go back.");
        } else if (view.getSelectedTripIndex() == -1) {
            view.showStepTwoError("You must select a trip to continue.");
        } else {
            view.goToStepThree();
        }
    }

    public boolean hasEnoughBalance(double ticketCost) {
        return UserSession.getInstance().getBalance() >= ticketCost;
    }

    public void confirmPurchase(double price, String tripDetails) {
        if (price > 0 && UserSession.getInstance().deductBalance(price)) {
            UserSession.getInstance().addTrip(tripDetails);
        } else if (price == 0) {
            // Caso de Tarjeta de Crédito (no resta saldo de la app)
            UserSession.getInstance().addTrip(tripDetails);
        }
    }

    public void processEditRefund(String oldTripDetails) {
        try {
            String[] parts = oldTripDetails.split("\\|");
            String priceStr = parts[parts.length - 1].replace("€", "").trim();
            double oldPrice = Double.parseDouble(priceStr);
            
            // Si el billete original se pagó con TCyL, devolvemos el dinero al saldo
            if (oldTripDetails.contains("TCyL")) {
                UserSession.getInstance().addBalance(oldPrice);
            }
        } catch (Exception e) {}
        
        // Borramos el billete antiguo de la lista
        UserSession.getInstance().removeTrip(oldTripDetails);
    }
}