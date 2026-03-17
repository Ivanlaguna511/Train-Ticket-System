package com.trainticketing.view;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import com.trainticketing.controller.InitialMenuController;
import com.trainticketing.main.Main;

public class InitialMenuView extends JFrame {
    
    private InitialMenuController controller;

    // Paleta de colores Material Design (Google Style)
    private final Color COLOR_BG = Color.WHITE;
    private final Color COLOR_PRIMARY = new Color(26, 115, 232); // Google Blue
    private final Color COLOR_SECONDARY = new Color(241, 243, 244); // Light Gray
    private final Color COLOR_TEXT_DARK = new Color(32, 33, 36);

    public InitialMenuView() {
        controller = new InitialMenuController(this);
        setupUI();
    }

    private void setupUI() {
        setTitle("Train Ticketing System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null); 
        
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(COLOR_BG);
        mainPanel.setBorder(new EmptyBorder(40, 0, 40, 0)); 

        // Logo
        JLabel logoLabel = new JLabel();
        logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        try {
            ImageIcon icon = new ImageIcon("src/resources/images/logo-tren.png");
            Image scaledImage = icon.getImage().getScaledInstance(400, 300, Image.SCALE_SMOOTH);
            logoLabel.setIcon(new ImageIcon(scaledImage));
        } catch (Exception e) {
            logoLabel.setText("[ Logo Train System ]");
            logoLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
            logoLabel.setForeground(COLOR_TEXT_DARK);
        }

        // Botones estilo Material
        JButton buyTicketBtn = createPrimaryButton("Buy Ticket");
        buyTicketBtn.addActionListener(e -> Main.getViewManager().showBuyTicketView());

        JButton userMenuBtn = createSecondaryButton("Card Access");
        userMenuBtn.addActionListener(e -> Main.getViewManager().showUserMenu());

        JButton exitBtn = createSecondaryButton("Exit");
        exitBtn.setForeground(new Color(217, 48, 37)); // Google Red text
        exitBtn.addActionListener(e -> System.exit(0));

        // Añadir todo al panel
        mainPanel.add(logoLabel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 40)));
        mainPanel.add(buyTicketBtn);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        mainPanel.add(userMenuBtn);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        mainPanel.add(exitBtn);

        setContentPane(mainPanel);
    }

    private JButton createPrimaryButton(String text) {
        JButton btn = new JButton(text);
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setMaximumSize(new Dimension(250, 45));
        btn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btn.setForeground(Color.WHITE);
        btn.setBackground(COLOR_PRIMARY);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setOpaque(true);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private JButton createSecondaryButton(String text) {
        JButton btn = new JButton(text);
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setMaximumSize(new Dimension(250, 45));
        btn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btn.setForeground(COLOR_TEXT_DARK);
        btn.setBackground(COLOR_SECONDARY);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setOpaque(true);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }
}