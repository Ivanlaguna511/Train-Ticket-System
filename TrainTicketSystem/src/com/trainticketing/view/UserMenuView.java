package com.trainticketing.view;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import com.trainticketing.controller.UserMenuController;
import com.trainticketing.main.Main;

public class UserMenuView extends JFrame {

    private UserMenuController controller;

    // Paleta Material Design
    private final Color BG_WHITE = Color.WHITE;
    private final Color BG_GRAY = new Color(248, 249, 250);
    private final Color TEXT_DARK = new Color(32, 33, 36);
    private final Color PRIMARY_BLUE = new Color(26, 115, 232);
    private final Color SUCCESS_GREEN = new Color(24, 128, 56);

    public UserMenuView() {
        controller = new UserMenuController(this);
        setupUI();
    }

    private void setupUI() {
        setTitle("User Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 500);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel mainPanel = new JPanel(new BorderLayout(20, 20));
        mainPanel.setBackground(new Color(255, 255, 255));
        mainPanel.setBorder(new EmptyBorder(40, 60, 40, 60));

        JLabel titleLabel = new JLabel("User Dashboard", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        titleLabel.setForeground(new Color(32, 33, 36));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        JPanel statsPanel = new JPanel(new GridLayout(2, 2, 20, 20));
        statsPanel.setBackground(new Color(248, 249, 250));
        statsPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(218, 220, 224)),
            new EmptyBorder(30, 30, 30, 30)
        ));

        statsPanel.add(createStatLabel("Card Balance:", SwingConstants.RIGHT));
        // CARGA EL SALDO REAL DESDE EL CONTROLADOR
        statsPanel.add(createValueLabel(controller.getFormattedBalance(), new Color(24, 128, 56))); 

        statsPanel.add(createStatLabel("Purchased Tickets:", SwingConstants.RIGHT));
        // CARGA LA CANTIDAD DE BILLETES DESDE EL CONTROLADOR
        statsPanel.add(createValueLabel(controller.getTicketsCount(), new Color(32, 33, 36)));

        mainPanel.add(statsPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        buttonPanel.setBackground(Color.WHITE);

        JButton backBtn = createSecondaryBtn("< Back");
        backBtn.addActionListener(e -> Main.getViewManager().showInitialMenu());

        JButton rechargeBtn = createPrimaryBtn("Recharge Card");
        rechargeBtn.addActionListener(e -> Main.getViewManager().showRechargeCardView());

        JButton myTripsBtn = createPrimaryBtn("My Trips");
        myTripsBtn.addActionListener(e -> Main.getViewManager().showMyTripsView());

        buttonPanel.add(backBtn);
        buttonPanel.add(rechargeBtn);
        buttonPanel.add(myTripsBtn);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        setContentPane(mainPanel);
    }

    private JLabel createStatLabel(String text, int alignment) {
        JLabel lbl = new JLabel(text, alignment);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        lbl.setForeground(new Color(95, 99, 104)); // Texto secundario gris
        return lbl;
    }

    private JLabel createValueLabel(String text, Color color) {
        JLabel lbl = new JLabel(text, SwingConstants.LEFT);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lbl.setForeground(color);
        return lbl;
    }

    private JButton createPrimaryBtn(String text) {
        JButton btn = new JButton(text);
        btn.setPreferredSize(new Dimension(160, 45));
        btn.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btn.setForeground(Color.WHITE);
        btn.setBackground(PRIMARY_BLUE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private JButton createSecondaryBtn(String text) {
        JButton btn = new JButton(text);
        btn.setPreferredSize(new Dimension(120, 45));
        btn.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btn.setForeground(TEXT_DARK);
        btn.setBackground(new Color(241, 243, 244));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }
}