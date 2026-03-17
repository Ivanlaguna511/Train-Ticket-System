package com.trainticketing.view;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import com.trainticketing.controller.RechargeCardController;
import com.trainticketing.main.Main;

public class RechargeCardView extends JFrame {

    private RechargeCardController controller;
    private double selectedAmount = 0.0; // Guarda el dinero seleccionado, pero no lo suma aún
    private boolean isRecharging = false;

    // Paleta Material Design
    private final Color BG_WHITE = Color.WHITE;
    private final Color TEXT_DARK = new Color(32, 33, 36);
    private final Color TEXT_MUTED = new Color(95, 99, 104);
    private final Color SUCCESS_GREEN = new Color(24, 128, 56);
    private final Color PRIMARY_BLUE = new Color(26, 115, 232);
    private final Color ERROR_RED = new Color(217, 48, 37);

    public RechargeCardView() {
        controller = new RechargeCardController(this);
        setupUI();
    }

    private void setupUI() {
        setTitle("Recharge Card");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 550);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(BG_WHITE);
        mainPanel.setBorder(new EmptyBorder(30, 50, 30, 50));

        JLabel title = new JLabel("Recharge Your Card");
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setForeground(TEXT_DARK);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel balanceLabel = new JLabel("Current Balance: " + controller.getFormattedBalance());
        balanceLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        balanceLabel.setForeground(SUCCESS_GREEN);
        balanceLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel cardImage = new JLabel();
        cardImage.setAlignmentX(Component.CENTER_ALIGNMENT);
        try {
            ImageIcon icon = new ImageIcon("src/resources/images/tarjetacredito.png");
            Image scaled = icon.getImage().getScaledInstance(250, 140, Image.SCALE_SMOOTH);
            cardImage.setIcon(new ImageIcon(scaled));
        } catch (Exception e) {}

        JLabel instruction = new JLabel("Select amount and hover your mouse to simulate tap");
        instruction.setFont(new Font("Segoe UI", Font.ITALIC, 16));
        instruction.setForeground(TEXT_MUTED);
        instruction.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Grupo de botones para que solo se pueda seleccionar uno a la vez
        ButtonGroup amountGroup = new ButtonGroup();
        JPanel amountPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        amountPanel.setBackground(BG_WHITE);
        
        JToggleButton btn10 = createAmountButton("10 €");
        btn10.addActionListener(e -> selectedAmount = 10.0); // Solo guarda el valor
        
        JToggleButton btn20 = createAmountButton("20 €");
        btn20.addActionListener(e -> selectedAmount = 20.0); // Solo guarda el valor
        
        JToggleButton btn50 = createAmountButton("50 €");
        btn50.addActionListener(e -> selectedAmount = 50.0); // Solo guarda el valor

        amountGroup.add(btn10);
        amountGroup.add(btn20);
        amountGroup.add(btn50);

        amountPanel.add(btn10);
        amountPanel.add(btn20);
        amountPanel.add(btn50);

        // LÓGICA DEL LECTOR DE TARJETA ARREGLADA
        Timer tapTimer = new Timer(2000, e -> {
            if (selectedAmount > 0) {
                controller.rechargeAmount(selectedAmount);
                balanceLabel.setText("Current Balance: " + controller.getFormattedBalance());
                instruction.setText("Recharge successful!");
                instruction.setForeground(SUCCESS_GREEN);
                selectedAmount = 0.0; // Resetear
                amountGroup.clearSelection(); // Desmarcar botón
            }
        });
        tapTimer.setRepeats(false);

        cardImage.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                if (selectedAmount > 0) {
                    instruction.setText("Reading card... Please hold.");
                    instruction.setForeground(PRIMARY_BLUE);
                    setCursor(new Cursor(Cursor.HAND_CURSOR));
                    tapTimer.start();
                } else {
                    instruction.setText("Error: Select an amount before tapping!");
                    instruction.setForeground(ERROR_RED);
                }
            }
            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                tapTimer.stop();
                if (selectedAmount > 0) {
                    instruction.setText("Select amount and hover your mouse to simulate tap");
                    instruction.setForeground(TEXT_MUTED);
                }
            }
        });

        JButton backBtn = new JButton("< Back");
        backBtn.setFont(new Font("Segoe UI", Font.BOLD, 15));
        backBtn.setBackground(new Color(241, 243, 244));
        backBtn.setForeground(TEXT_DARK);
        backBtn.setFocusPainted(false);
        backBtn.setBorderPainted(false);
        backBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        backBtn.addActionListener(e -> Main.getViewManager().showUserMenu());

        mainPanel.add(title);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        mainPanel.add(balanceLabel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        mainPanel.add(cardImage);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        mainPanel.add(instruction);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        mainPanel.add(amountPanel);
        mainPanel.add(Box.createVerticalGlue()); 
        mainPanel.add(backBtn);

        setContentPane(mainPanel);
    }

    private JToggleButton createAmountButton(String text) {
        JToggleButton btn = new JToggleButton(text);
        btn.setPreferredSize(new Dimension(90, 45));
        btn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btn.setBackground(Color.WHITE);
        btn.setForeground(PRIMARY_BLUE);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setFocusPainted(false);
        return btn;
    }
}