package com.trainticketing.view;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import com.trainticketing.controller.MyTripsController;
import com.trainticketing.main.Main;

public class MyTripsView extends JFrame {

    private MyTripsController controller;
    private JList<String> tripsList;
    private DefaultListModel<String> listModel;
    private JLabel errorLabel;

    private final Color BG_WHITE = Color.WHITE;
    private final Color TEXT_DARK = new Color(32, 33, 36);
    private final Color PRIMARY_BLUE = new Color(26, 115, 232);
    private final Color DANGER_RED = new Color(217, 48, 37);
    private final Color WARNING_YELLOW = new Color(244, 180, 0);

    public MyTripsView() {
        controller = new MyTripsController(this);
        setupUI();
    }

    private void setupUI() {
        setTitle("My Trips");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(850, 500);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout(0, 20));
        mainPanel.setBackground(BG_WHITE);
        mainPanel.setBorder(new EmptyBorder(30, 40, 30, 40));

        JLabel title = new JLabel("My Purchased Trips", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setForeground(TEXT_DARK);
        mainPanel.add(title, BorderLayout.NORTH);

        listModel = new DefaultListModel<>();
        refreshTripsList(); 
        
        tripsList = new JList<>(listModel);
        tripsList.setFont(new Font("Consolas", Font.PLAIN, 15));
        tripsList.setBackground(new Color(248, 249, 250));
        tripsList.setForeground(TEXT_DARK);
        tripsList.setSelectionBackground(new Color(232, 240, 254)); 
        tripsList.setSelectionForeground(PRIMARY_BLUE);
        
        JScrollPane scrollPane = new JScrollPane(tripsList);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(218, 220, 224)));
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBackground(BG_WHITE);

        JButton backBtn = createBtn("< Back", new Color(241, 243, 244), TEXT_DARK);
        backBtn.addActionListener(e -> Main.getViewManager().showUserMenu());

        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        actionPanel.setBackground(BG_WHITE);
        
        JButton editBtn = createBtn("Edit Ticket", WARNING_YELLOW, TEXT_DARK);
        JButton refundBtn = createBtn("Refund Ticket", DANGER_RED, Color.WHITE);

        errorLabel = new JLabel(" ");
        errorLabel.setForeground(DANGER_RED);
        errorLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        bottomPanel.add(errorLabel, BorderLayout.NORTH);

        // LÓGICA DEL BOTÓN EDITAR
        editBtn.addActionListener(e -> {
            if (tripsList.getSelectedIndex() <= 1) { 
                errorLabel.setText("Please select a valid ticket from the list to edit.");
            } else {
                errorLabel.setText(" ");
                String selectedTrip = tripsList.getSelectedValue();
                Main.getViewManager().showBuyTicketViewInEditMode(selectedTrip);
            }
        });

        // LÓGICA DEL BOTÓN REEMBOLSAR
        refundBtn.addActionListener(e -> {
            if (tripsList.getSelectedIndex() <= 1) {
                errorLabel.setText("Please select a valid ticket from the list to refund.");
            } else {
                errorLabel.setText(" ");
                int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to refund this ticket? The money will be returned to your card.", "Confirm Refund", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    String selectedTrip = tripsList.getSelectedValue();
                    controller.refundTrip(selectedTrip);
                    refreshTripsList();
                    JOptionPane.showMessageDialog(this, "Ticket refunded successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });

        actionPanel.add(editBtn);
        actionPanel.add(refundBtn);

        bottomPanel.add(backBtn, BorderLayout.WEST);
        bottomPanel.add(actionPanel, BorderLayout.EAST);

        mainPanel.add(bottomPanel, BorderLayout.SOUTH);
        setContentPane(mainPanel);
    }

    private void refreshTripsList() {
        listModel.clear();
        listModel.addElement("Date       | Payment | Route                           | Price");
        listModel.addElement("------------------------------------------------------------------");
        for(String trip : controller.getMyTrips()) {
            listModel.addElement(trip);
        }
    }

    private JButton createBtn(String text, Color bg, Color fg) {
        JButton btn = new JButton(text);
        btn.setPreferredSize(new Dimension(140, 45));
        btn.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btn.setBackground(bg);
        btn.setForeground(fg);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }
}