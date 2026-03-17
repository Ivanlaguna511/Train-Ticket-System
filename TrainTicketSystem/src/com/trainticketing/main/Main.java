package com.trainticketing.main;

import javax.swing.UIManager;
import com.trainticketing.model.ViewManager;
import com.trainticketing.model.TrainModel;

/**
 * Main entry point for the Train Ticketing System application.
 * Initializes the core model, sets the native UI look, and starts the views.
 */
public class Main {
    
    private static ViewManager viewManager;
    private static TrainModel model;

    public static ViewManager getViewManager() {
        return viewManager;
    }

    public static TrainModel getModel() {
        return model;
    }

    public static void main(String[] args) {
        // Activate Native OS Look and Feel for a professional design
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.err.println("Could not set System Look and Feel.");
        }

        viewManager = new ViewManager();
        model = new TrainModel();
        
        // Start the application GUI
        viewManager.showInitialMenu();
    }
}