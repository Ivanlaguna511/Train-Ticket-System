package com.trainticketing.model;

import javax.swing.JFrame;
import com.trainticketing.view.BuyTicketView;
import com.trainticketing.view.UserMenuView;
import com.trainticketing.view.InitialMenuView;
import com.trainticketing.view.MyTripsView;
import com.trainticketing.view.RechargeCardView;

public class ViewManager {
    
    private JFrame currentState;

    public ViewManager() {}
    
    private void switchView(JFrame newView) {
        if (currentState != null) {
            currentState.setVisible(false);
            currentState.dispose();
        }
        currentState = newView;
        currentState.setLocationRelativeTo(null); 
        currentState.setVisible(true);
    }
    
    public void showInitialMenu() { switchView(new InitialMenuView()); }
    public void showUserMenu() { switchView(new UserMenuView()); }  
    public void showBuyTicketView() { switchView(new BuyTicketView()); }  
    public void showMyTripsView() { switchView(new MyTripsView()); } 
    public void showRechargeCardView() { switchView(new RechargeCardView()); }

    // NUEVO MÉTODO: Lanza la vista de compra pero en Modo Edición
    public void showBuyTicketViewInEditMode(String oldTripDetails) {
        BuyTicketView editView = new BuyTicketView();
        editView.setEditMode(oldTripDetails); // Bloquea los campos
        switchView(editView);
    }
}