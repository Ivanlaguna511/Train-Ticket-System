package com.trainticketing.controller;

import com.trainticketing.model.UserSession;
import com.trainticketing.view.UserMenuView;

public class UserMenuController {
    private UserMenuView view;

    public UserMenuController(UserMenuView view) {
        this.view = view;
    }

    public String getFormattedBalance() {
        return String.format("%.2f €", UserSession.getInstance().getBalance());
    }

    public String getTicketsCount() {
        return String.valueOf(UserSession.getInstance().getTripsCount());
    }
}