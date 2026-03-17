# Train Ticket System (Second practice of the subject Human-Computer Interaction, second quarter of the second year)

<div align="center">
  
  ![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=java&logoColor=white)
  ![Swing](https://img.shields.io/badge/Java_Swing-GUI-007396?style=for-the-badge)
  ![MVC](https://img.shields.io/badge/Architecture-MVC-brightgreen?style=for-the-badge)
  ![Academic](https://img.shields.io/badge/Academic_Project-2nd_Year-purple?style=for-the-badge)

</div>

> **2026 PROFESSIONAL REFACTOR:**
> This repository contains the original legacy logic evolved into a professional production-ready architecture. 
> I have completely refactored the original NetBeans-generated code to implement a hand-coded **Material Design** interface, strict **MVC separation**, and a robust **Singleton** state management system.

---

## About the Project

"Train Ticket System" is a professional desktop application simulating an advanced self-service train ticketing kiosk. It was developed as the final practical work for the **Human-Computer Interaction (HCI)** course. 

The primary focus of this project was to design an intuitive User Experience (UX) that guides the user through a complex multi-step purchasing process while maintaining data integrity and simulating real-world hardware interactions.

### Key Workflows
* **Purchase Wizard:** A guided 3-step flow (Date/Route → Trip selection → Payment).
* **User Dashboard:** Real-time balance management and ticket lifecycle (View, Edit, Refund).
* **Hardware Simulation:** Interactive card readers with proximity detection and timed validation.

### Interface Preview
![Train Ticket System Gameplay](src/resources/images/screenshot1.png)

### Tech Stack
- **Language:** Java 11+
- **GUI Framework:** `Java Swing` & `AWT` (Hand-coded, no Drag & Drop tools used).
- **Architecture:** Model-View-Controller (MVC).
- **Design System:** Custom Google Material Design palette.

### Key Technical Features
* **Global State Management:** Implementation of the **Singleton Pattern** via `UserSession` to synchronize balance and ticket data across independent views.
* **Dynamic Data Engine:** Real-time parsing of CSV data stores for stations, schedules, and pricing, ensuring the UI is decoupled from the data.
* **UX/UI Polish:** High-contrast layouts, semantic color feedback (Success/Error/Warning), and responsive event handling using `javax.swing.Timer`.
* **Safe Ticket Editing:** Advanced logic that permits trip modifications while ensuring transactional security (the old ticket is only processed after the new one is confirmed).

---

## How to Run Locally

To run this application, you will need the Java Development Kit (JDK) installed on your machine.

**1. Clone the repository:**
```bash
git clone [https://github.com/YOUR_USERNAME/Train-Ticketing-System.git](https://github.com/YOUR_USERNAME/Train-Ticketing-System.git)
cd Train-Ticketing-System
```

##2. Library Configuration:
Ensure the lib/jcalendar-1.4.jar file is added to your project's build path (Referenced Libraries in VS Code).

##3. Run the application:
Run the main method located in:
```bash
src/com/trainticketing/main/Main.java
```

##Original Authors
-Iván Moro Cienfuegos and Eric Soto San José
