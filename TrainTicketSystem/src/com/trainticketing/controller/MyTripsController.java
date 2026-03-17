package com.trainticketing.controller;

import java.util.List;
import com.trainticketing.model.UserSession;
import com.trainticketing.view.MyTripsView;

public class MyTripsController {
    private MyTripsView view;

    public MyTripsController(MyTripsView view) {
        this.view = view;
    }

    public List<String> getMyTrips() {
        return UserSession.getInstance().getPurchasedTrips();
    }

    // Lógica real para devolver el billete
    public void refundTrip(String tripDetails) {
        // Borramos el billete del sistema
        UserSession.getInstance().removeTrip(tripDetails);
        
        // Extraemos el precio del texto para devolver el dinero al saldo
        try {
            String[] parts = tripDetails.split("\\|");
            String priceStr = parts[parts.length - 1].replace("€", "").trim();
            double price = Double.parseDouble(priceStr);
            UserSession.getInstance().addBalance(price); // Devolvemos el dinero
        } catch (Exception e) {
            System.err.println("No se pudo extraer el precio para el reembolso.");
        }
    }
}