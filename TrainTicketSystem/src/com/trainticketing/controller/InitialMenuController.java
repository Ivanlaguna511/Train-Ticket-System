package com.trainticketing.controller;

import com.trainticketing.model.ViewManager;
import com.trainticketing.model.TrainModel;
import com.trainticketing.view.InitialMenuView;

public class InitialMenuController {
    
    private InitialMenuView view;
    private TrainModel model;
    private ViewManager viewManager;

    public InitialMenuController(InitialMenuView view) {
        this.view = view;
        this.model = new TrainModel();
    }
            
    public ViewManager getViewManager() {
        return viewManager;
    }
}