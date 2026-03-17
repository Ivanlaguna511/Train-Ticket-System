package com.trainticketing.view;

import java.awt.*;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import com.toedter.calendar.JCalendar;
import com.trainticketing.controller.BuyTicketController;
import com.trainticketing.main.Main;

public class BuyTicketView extends JFrame {
    
    private BuyTicketController controller;
    private double balance = 20.0;
    private boolean isCreditCardRead = false;
    private String tripToEdit = null; // Variable que guarda si estamos editando
    
    // Material Design Palette
    private final Color BG_WHITE = Color.WHITE;
    private final Color BG_GRAY = new Color(248, 249, 250);
    private final Color TEXT_DARK = new Color(32, 33, 36);
    private final Color TEXT_MUTED = new Color(95, 99, 104);
    private final Color PRIMARY_BLUE = new Color(26, 115, 232);
    private final Color SUCCESS_GREEN = new Color(24, 128, 56);
    private final Color ERROR_RED = new Color(217, 48, 37);

    // UI Components
    private JTabbedPane tabbedPane;
    private JComboBox<String> originComboBox;
    private JComboBox<String> destinationComboBox;
    private JButton swapButton;
    private JCalendar calendar;
    private JLabel step1ErrorLabel;
    
    private JList<String> tripsList;
    private JCheckBox bikeCheckBox;
    private JCheckBox petCheckBox;
    private JLabel step2ErrorLabel;
    
    private JRadioButton creditCardRadio;
    private JRadioButton tcylCardRadio;
    
    private JTextField pinTextField;
    private JLabel creditCardInstructionLabel;
    private JLabel tcylInstructionLabel;
    private JLabel tcylErrorLabel;
    private JLabel creditCardImageLabel;
    private JLabel tcylCardImageLabel;

    public BuyTicketView() {
        controller = new BuyTicketController(this);
        initComponents();
        populateStations(); 
        setupTimers();
    }

    private void initComponents() {
        setTitle("Buy Train Ticket");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(950, 700);
        setResizable(false);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        tabbedPane = new JTabbedPane();
        tabbedPane.setUI(new javax.swing.plaf.metal.MetalTabbedPaneUI() {
            @Override
            protected int calculateTabAreaHeight(int tabPlacement, int horizRunCount, int maxTabHeight) {
                return 0; 
            }
        });
        tabbedPane.setBackground(BG_WHITE);

        tabbedPane.addTab("Step 1", createStep1Panel());
        tabbedPane.addTab("Step 2", createStep2Panel());
        tabbedPane.addTab("Step 3", createStep3Panel());
        tabbedPane.addTab("Credit Card", createCreditCardPanel());
        tabbedPane.addTab("TCyL Card", createTcylCardPanel());
        tabbedPane.addTab("Confirmation", createConfirmationPanel());

        add(tabbedPane, BorderLayout.CENTER);
    }

    // =====================================================================
    // MODO EDICIÓN
    // =====================================================================
    public void setEditMode(String oldTripDetails) {
        this.tripToEdit = oldTripDetails;
        
        try {
            // Ejemplo de formato: "2026-03-17 | TCyL Card | Origen -> Destino | 15.0€"
            String[] parts = oldTripDetails.split("\\|");
            String routePart = "";
            for (String p : parts) {
                if (p.contains("->")) {
                    routePart = p;
                    break;
                }
            }
            String[] stations = routePart.split("->");
            String origin = stations[0].trim();
            String dest = stations[1].trim();

            originComboBox.setSelectedItem(origin);
            destinationComboBox.setSelectedItem(dest);

            // Bloquear los campos de estación
            originComboBox.setEnabled(false);
            destinationComboBox.setEnabled(false);
            swapButton.setEnabled(false);
            
            step1ErrorLabel.setText("EDIT MODE: You can change Date, Train and Extras.");
            step1ErrorLabel.setForeground(new Color(244, 180, 0)); // Warning yellow

        } catch (Exception e) {
            System.err.println("Error parsing trip for edit mode.");
        }
    }

    private void populateStations() {
        List<String> stations = controller.getStations();
        originComboBox.removeAllItems();
        destinationComboBox.removeAllItems();
        for (String station : stations) {
            originComboBox.addItem(station);
            destinationComboBox.addItem(station);
        }
    }

    private void setupTimers() {
        // TARJETA DE CRÉDITO
        Timer creditCardTimer = new Timer(2000, e -> {
            isCreditCardRead = true;
            pinTextField.setText("");
            pinTextField.setEnabled(true);
            creditCardInstructionLabel.setText("Card read successfully. Enter PIN and click Accept.");
            creditCardInstructionLabel.setForeground(SUCCESS_GREEN);
        });
        creditCardTimer.setRepeats(false);

        creditCardImageLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                creditCardInstructionLabel.setText("Reading card... Please hold.");
                creditCardInstructionLabel.setForeground(PRIMARY_BLUE);
                setCursor(new Cursor(Cursor.HAND_CURSOR));
                creditCardTimer.start();
            }
            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                if (!isCreditCardRead) {
                    creditCardTimer.stop();
                    creditCardInstructionLabel.setText("Hover your mouse over the card to simulate tap");
                    creditCardInstructionLabel.setForeground(TEXT_MUTED);
                }
            }
        });

        // TARJETA TCYL
        Timer tcylCardTimer = new Timer(2000, e -> {
            double price = getSelectedTripPrice();
            if (controller.hasEnoughBalance(price)) {
                
                // Si estamos en modo edición, devolvemos el billete antiguo ANTES de comprar el nuevo
                if (tripToEdit != null) {
                    controller.processEditRefund(tripToEdit);
                }

                String selectedDate = calendar.getCalendar().getTime().toString().substring(0, 10);
                String tripData = selectedDate + " | TCyL Card | " + originComboBox.getSelectedItem() + " -> " + destinationComboBox.getSelectedItem() + " | " + price + "€";
                controller.confirmPurchase(price, tripData);
                
                tabbedPane.setSelectedIndex(5); 
            } else {
                tcylErrorLabel.setText("Insufficient balance. Select another payment method.");
                tcylErrorLabel.setVisible(true);
            }
        });
        tcylCardTimer.setRepeats(false);

        tcylCardImageLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                tcylInstructionLabel.setText("Reading TCyL Card... Please hold.");
                tcylInstructionLabel.setForeground(PRIMARY_BLUE);
                setCursor(new Cursor(Cursor.HAND_CURSOR));
                tcylCardTimer.start();
            }
            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                tcylCardTimer.stop();
                if (tabbedPane.getSelectedIndex() != 5) {
                    tcylInstructionLabel.setText("Hover your mouse over the card to simulate tap");
                    tcylInstructionLabel.setForeground(TEXT_MUTED);
                }
            }
        });
    }

    private JPanel createStep1Panel() {
        JPanel panel = new JPanel(new BorderLayout(20, 20));
        panel.setBackground(BG_WHITE);
        panel.setBorder(new EmptyBorder(40, 50, 40, 50));

        JLabel title = new JLabel("Step 1: Select Route and Date");
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setForeground(TEXT_DARK);
        panel.add(title, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new GridLayout(1, 2, 40, 0));
        centerPanel.setBackground(BG_WHITE);

        calendar = new JCalendar();
        calendar.setBackground(BG_WHITE);
        centerPanel.add(calendar);

        JPanel stationsPanel = new JPanel(new GridLayout(5, 1, 0, 10));
        stationsPanel.setBackground(BG_WHITE);

        JLabel originLabel = new JLabel("Origin Station:");
        originLabel.setForeground(TEXT_DARK);
        originLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        
        originComboBox = new JComboBox<>();
        originComboBox.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        originComboBox.setBackground(BG_WHITE);

        swapButton = createSecondaryBtn("↑↓ Swap Stations");
        swapButton.addActionListener(e -> {
            int originIdx = originComboBox.getSelectedIndex();
            originComboBox.setSelectedIndex(destinationComboBox.getSelectedIndex());
            destinationComboBox.setSelectedIndex(originIdx);
        });

        JLabel destLabel = new JLabel("Destination Station:");
        destLabel.setForeground(TEXT_DARK);
        destLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));

        destinationComboBox = new JComboBox<>();
        destinationComboBox.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        destinationComboBox.setBackground(BG_WHITE);

        stationsPanel.add(originLabel);
        stationsPanel.add(originComboBox);
        stationsPanel.add(swapButton);
        stationsPanel.add(destLabel);
        stationsPanel.add(destinationComboBox);

        centerPanel.add(stationsPanel);
        panel.add(centerPanel, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBackground(BG_WHITE);

        step1ErrorLabel = new JLabel(" ");
        step1ErrorLabel.setForeground(ERROR_RED);
        step1ErrorLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        bottomPanel.add(step1ErrorLabel, BorderLayout.NORTH);

        JButton backButton = createSecondaryBtn("< Main Menu");
        backButton.addActionListener(e -> Main.getViewManager().showInitialMenu());
        
        JButton continueButton = createPrimaryBtn("Continue >");
        continueButton.addActionListener(e -> {
            String origin = (String) originComboBox.getSelectedItem();
            String dest = (String) destinationComboBox.getSelectedItem();
            Calendar cal = calendar.getCalendar();
            LocalDate date = LocalDate.of(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1, cal.get(Calendar.DAY_OF_MONTH));
            controller.handleStepOneContinue(origin, dest, date);
        });

        bottomPanel.add(backButton, BorderLayout.WEST);
        bottomPanel.add(continueButton, BorderLayout.EAST);
        panel.add(bottomPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createStep2Panel() {
        JPanel panel = new JPanel(new BorderLayout(20, 20));
        panel.setBackground(BG_WHITE);
        panel.setBorder(new EmptyBorder(40, 50, 40, 50));

        JLabel title = new JLabel("Step 2: Select your Train");
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setForeground(TEXT_DARK);
        panel.add(title, BorderLayout.NORTH);

        tripsList = new JList<>();
        tripsList.setFont(new Font("Consolas", Font.PLAIN, 16));
        tripsList.setBackground(BG_WHITE);
        tripsList.setForeground(TEXT_DARK);
        tripsList.setSelectionBackground(new Color(232, 240, 254));
        tripsList.setSelectionForeground(PRIMARY_BLUE);
        tripsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        JScrollPane scrollPane = new JScrollPane(tripsList);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(218, 220, 224)));
        panel.add(scrollPane, BorderLayout.CENTER);

        JPanel bottomContainer = new JPanel(new BorderLayout(0, 20));
        bottomContainer.setBackground(BG_WHITE);

        JPanel optionsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 30, 0));
        optionsPanel.setBackground(BG_WHITE);
        bikeCheckBox = new JCheckBox("Add Bicycle (+3.00€)");
        bikeCheckBox.setForeground(TEXT_DARK);
        bikeCheckBox.setBackground(BG_WHITE);
        bikeCheckBox.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        
        petCheckBox = new JCheckBox("Add Pet (+5.00€)");
        petCheckBox.setForeground(TEXT_DARK);
        petCheckBox.setBackground(BG_WHITE);
        petCheckBox.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        
        optionsPanel.add(bikeCheckBox);
        optionsPanel.add(petCheckBox);
        bottomContainer.add(optionsPanel, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel(new BorderLayout());
        buttonPanel.setBackground(BG_WHITE);

        step2ErrorLabel = new JLabel(" ");
        step2ErrorLabel.setForeground(ERROR_RED);
        step2ErrorLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        buttonPanel.add(step2ErrorLabel, BorderLayout.NORTH);

        JButton backButton = createSecondaryBtn("< Back to Step 1");
        backButton.addActionListener(e -> tabbedPane.setSelectedIndex(0));
        
        JButton continueButton = createPrimaryBtn("Continue to Payment >");
        continueButton.addActionListener(e -> controller.handleStepTwoContinue());

        buttonPanel.add(backButton, BorderLayout.WEST);
        buttonPanel.add(continueButton, BorderLayout.EAST);
        bottomContainer.add(buttonPanel, BorderLayout.SOUTH);

        panel.add(bottomContainer, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel createStep3Panel() {
        JPanel panel = new JPanel(new BorderLayout(20, 20));
        panel.setBackground(BG_WHITE);
        panel.setBorder(new EmptyBorder(40, 50, 40, 50));

        JLabel title = new JLabel("Step 3: Select Payment Method");
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setForeground(TEXT_DARK);
        panel.add(title, BorderLayout.NORTH);

        JPanel radioPanel = new JPanel(new GridLayout(2, 1, 0, 20));
        radioPanel.setBackground(BG_GRAY);
        radioPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(218, 220, 224)),
            new EmptyBorder(50, 50, 50, 50)
        ));

        ButtonGroup paymentGroup = new ButtonGroup();
        creditCardRadio = new JRadioButton("  Credit / Debit Card");
        creditCardRadio.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        creditCardRadio.setForeground(TEXT_DARK);
        creditCardRadio.setBackground(BG_GRAY);
        creditCardRadio.setSelected(true);
        
        tcylCardRadio = new JRadioButton("  TCyL User Card");
        tcylCardRadio.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        tcylCardRadio.setForeground(TEXT_DARK);
        tcylCardRadio.setBackground(BG_GRAY);

        paymentGroup.add(creditCardRadio);
        paymentGroup.add(tcylCardRadio);

        radioPanel.add(creditCardRadio);
        radioPanel.add(tcylCardRadio);
        panel.add(radioPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new BorderLayout());
        buttonPanel.setBackground(BG_WHITE);
        
        JButton backButton = createSecondaryBtn("< Back to Step 2");
        backButton.addActionListener(e -> tabbedPane.setSelectedIndex(1));
        
        JButton continueButton = createPrimaryBtn("Pay Now >");
        continueButton.setBackground(SUCCESS_GREEN); 
        continueButton.addActionListener(e -> {
            if (creditCardRadio.isSelected()) tabbedPane.setSelectedIndex(3);
            else tabbedPane.setSelectedIndex(4);
        });

        buttonPanel.add(backButton, BorderLayout.WEST);
        buttonPanel.add(continueButton, BorderLayout.EAST);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createCreditCardPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(BG_WHITE);
        panel.setBorder(new EmptyBorder(40, 50, 40, 50));

        JLabel title = new JLabel("Credit Card Payment");
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setForeground(TEXT_DARK);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        creditCardImageLabel = new JLabel();
        creditCardImageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        try {
            ImageIcon icon = new ImageIcon("src/resources/images/tarjetacredito.png");
            Image scaled = icon.getImage().getScaledInstance(300, 160, Image.SCALE_SMOOTH);
            creditCardImageLabel.setIcon(new ImageIcon(scaled));
        } catch (Exception e) {}

        creditCardInstructionLabel = new JLabel("Hover your mouse here for 2 seconds to simulate tap");
        creditCardInstructionLabel.setForeground(TEXT_MUTED);
        creditCardInstructionLabel.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        creditCardInstructionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel pinPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        pinPanel.setBackground(BG_WHITE);
        
        pinTextField = new JPasswordField(10);
        pinTextField.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        pinTextField.setEnabled(false); 
        
        JButton acceptPinBtn = createPrimaryBtn("Accept PIN");
        acceptPinBtn.addActionListener(e -> {
            if (!isCreditCardRead) {
                JOptionPane.showMessageDialog(this, "Please hold your card on the reader first.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (pinTextField.getText().equals("1234")) {
                double price = getSelectedTripPrice();
                
                if (controller.hasEnoughBalance(price)) {
                    // Si estamos editando, devolvemos el billete antiguo
                    if (tripToEdit != null) {
                        controller.processEditRefund(tripToEdit);
                    }

                    String selectedDate = calendar.getCalendar().getTime().toString().substring(0, 10);
                    String tripData = selectedDate + " | Credit Card | " + originComboBox.getSelectedItem() + " -> " + destinationComboBox.getSelectedItem() + " | " + price + "€";
                    
                    controller.confirmPurchase(0.0, tripData); 
                    tabbedPane.setSelectedIndex(5); 
                } else {
                    pinTextField.setText("");
                    creditCardInstructionLabel.setText("Insufficient balance in your bank account.");
                    creditCardInstructionLabel.setForeground(ERROR_RED);
                }
            } else {
                pinTextField.setText("");
                creditCardInstructionLabel.setText("Incorrect PIN. Try again.");
                creditCardInstructionLabel.setForeground(ERROR_RED);
            }
        });
        
        pinPanel.add(pinTextField);
        pinPanel.add(acceptPinBtn);

        JButton backButton = createSecondaryBtn("< Change Payment Method");
        backButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        backButton.addActionListener(e -> {
            isCreditCardRead = false;
            pinTextField.setEnabled(false);
            pinTextField.setText("");
            creditCardInstructionLabel.setText("Hover your mouse here for 2 seconds to simulate tap");
            creditCardInstructionLabel.setForeground(TEXT_MUTED);
            tabbedPane.setSelectedIndex(2);
        });

        panel.add(title);
        panel.add(Box.createRigidArea(new Dimension(0, 40)));
        panel.add(creditCardImageLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        panel.add(creditCardInstructionLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 30)));
        panel.add(pinPanel);
        panel.add(Box.createVerticalGlue());
        panel.add(backButton);

        return panel;
    }

    private JPanel createTcylCardPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(BG_WHITE);
        panel.setBorder(new EmptyBorder(40, 50, 40, 50));

        JLabel title = new JLabel("TCyL Card Payment");
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setForeground(TEXT_DARK);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        tcylCardImageLabel = new JLabel();
        tcylCardImageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        try {
            ImageIcon icon = new ImageIcon("src/resources/images/tarjetatren.png");
            Image scaled = icon.getImage().getScaledInstance(300, 160, Image.SCALE_SMOOTH);
            tcylCardImageLabel.setIcon(new ImageIcon(scaled));
        } catch (Exception e) {}

        tcylInstructionLabel = new JLabel("Hover your mouse here for 2 seconds to simulate tap");
        tcylInstructionLabel.setForeground(TEXT_MUTED);
        tcylInstructionLabel.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        tcylInstructionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        tcylErrorLabel = new JLabel(" ");
        tcylErrorLabel.setForeground(ERROR_RED);
        tcylErrorLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        tcylErrorLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        tcylErrorLabel.setVisible(false);

        JButton backButton = createSecondaryBtn("< Change Payment Method");
        backButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        backButton.addActionListener(e -> {
            tcylErrorLabel.setVisible(false);
            tabbedPane.setSelectedIndex(2);
        });

        panel.add(title);
        panel.add(Box.createRigidArea(new Dimension(0, 40)));
        panel.add(tcylCardImageLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        panel.add(tcylInstructionLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        panel.add(tcylErrorLabel);
        panel.add(Box.createVerticalGlue());
        panel.add(backButton);

        return panel;
    }

    private JPanel createConfirmationPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(BG_WHITE);
        panel.setBorder(new EmptyBorder(80, 50, 40, 50));

        JLabel title = new JLabel("PAYMENT SUCCESSFUL!");
        title.setFont(new Font("Stencil", Font.BOLD, 52));
        title.setForeground(SUCCESS_GREEN); 
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitle = new JLabel("Your ticket has been processed. Have a safe trip!");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        subtitle.setForeground(TEXT_DARK);
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 40, 0));
        buttonPanel.setBackground(BG_WHITE);
        
        JButton buyAnotherButton = createPrimaryBtn(tripToEdit != null ? "Buy Another Ticket" : "Buy Another Ticket");
        buyAnotherButton.addActionListener(e -> {
            // Reset Wizard and Edit Mode
            tripToEdit = null;
            originComboBox.setEnabled(true);
            destinationComboBox.setEnabled(true);
            swapButton.setEnabled(true);
            
            isCreditCardRead = false;
            pinTextField.setText("");
            pinTextField.setEnabled(false);
            creditCardInstructionLabel.setText("Hover your mouse here for 2 seconds to simulate tap");
            creditCardInstructionLabel.setForeground(TEXT_MUTED);
            tcylErrorLabel.setVisible(false);
            step1ErrorLabel.setText(" ");
            step2ErrorLabel.setText(" ");
            bikeCheckBox.setSelected(false);
            petCheckBox.setSelected(false);
            tabbedPane.setSelectedIndex(0);
        });
        
        JButton exitButton = createSecondaryBtn("Return to Main Menu");
        exitButton.addActionListener(e -> Main.getViewManager().showInitialMenu());

        buttonPanel.add(buyAnotherButton);
        buttonPanel.add(exitButton);

        panel.add(title);
        panel.add(Box.createRigidArea(new Dimension(0, 30)));
        panel.add(subtitle);
        panel.add(Box.createRigidArea(new Dimension(0, 80)));
        panel.add(buttonPanel);

        return panel;
    }

    private JButton createPrimaryBtn(String text) {
        JButton btn = new JButton(text);
        btn.setPreferredSize(new Dimension(220, 45));
        btn.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btn.setBackground(PRIMARY_BLUE);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setOpaque(true);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private JButton createSecondaryBtn(String text) {
        JButton btn = new JButton(text);
        btn.setPreferredSize(new Dimension(220, 45));
        btn.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btn.setBackground(new Color(241, 243, 244));
        btn.setForeground(TEXT_DARK);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setOpaque(true);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    public void showStepOneError(String message) { step1ErrorLabel.setText(message); }
    public void showStepTwoError(String message) { step2ErrorLabel.setText(message); }
    public void populateTripsList(List<String> trips) {
        DefaultListModel<String> model = new DefaultListModel<>();
        for (String trip : trips) model.addElement(trip);
        tripsList.setModel(model); 
    }
    public int getListedTripsCount() { return tripsList.getModel().getSize(); }
    public int getSelectedTripIndex() { return tripsList.getSelectedIndex(); }
    
    public void goToStepTwo() {
        step1ErrorLabel.setText(" ");
        tabbedPane.setSelectedIndex(1);
    }
    
    public void goToStepThree() {
        step2ErrorLabel.setText(" ");
        tabbedPane.setSelectedIndex(2);
    }

    public double getCurrentBalance() { return balance; }

    public Double getSelectedTripPrice() {
        String selected = tripsList.getSelectedValue();
        if (selected != null) {
            try {
                String[] parts = selected.split("Price: ");
                String priceStr = parts[1].replace("€", "").trim();
                
                double basePrice = Double.parseDouble(priceStr);
                if (bikeCheckBox.isSelected()) basePrice += 3.0; 
                if (petCheckBox.isSelected()) basePrice += 5.0;  
                
                return basePrice;
            } catch (Exception e) { return 0.0; }
        }
        return 0.0;
    }
}