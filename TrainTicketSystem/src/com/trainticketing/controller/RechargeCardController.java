package com.trainticketing.controller;

import com.trainticketing.model.UserSession;
import com.trainticketing.view.RechargeCardView;

public class RechargeCardController {
    private RechargeCardView view;

    public RechargeCardController(RechargeCardView view) {
        this.view = view;
    }

    public void rechargeAmount(double amount) {
        UserSession.getInstance().addBalance(amount);
    }

    public String getFormattedBalance() {
        return String.format("%.2f €", UserSession.getInstance().getBalance());
    }
}