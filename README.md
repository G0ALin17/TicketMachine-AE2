# OOP Group Project - Ticket Machine Simulator

This project is a console-based ticket machine application written in Kotlin. It is a group project for the Object-Oriented Design and Development module (QHO543) at Solent University.

The application simulates a train ticket purchasing and management system, with separate functionalities for regular passengers and administrators.

## Features

The system is divided into two main roles: Passenger and Admin.

### Passenger Mode (Member A)
*   **Search for a Ticket**: Users can search for available tickets by specifying a destination and ticket type (Single or Return).
*   **Insert Money**: Users can add funds to the machine to purchase tickets.
*   **Buy a Ticket**: After searching and ensuring sufficient funds, users can buy a ticket. The system prints a receipt and calculates change.

### Admin Mode (Members B & C)
Access to the admin dashboard is protected by a login system.

*   **Station Management (Member B)**:
    *   View a list of all train stations with their current prices and sales statistics.
    *   Add new stations to the system.
    *   Edit the prices of an existing station.
    *   Update all ticket prices in the system by a global percentage factor.
*   **Security & Special Offers (Member C)**:
    *   A secure login system to grant access to admin functionalities.
    *   Add new special offers for specific stations with a discount percentage and a date range.
    *   Search for existing offers by station.
    *   Delete special offers by their ID.

## How to Run

1.  Open the project in a compatible IDE (e.g., IntelliJ IDEA).
2.  Locate the `main` function in the `src/Main.kt` file.
3.  Run the application directly from the IDE.

## Project Structure

The project source code is located in the `src/` directory:

-   `Main.kt`: The main entry point for the application, containing the user interface logic.
-   `TicketMachine.kt`: The core class that handles all business logic for ticket sales, station management, and special offers.
-   `Authentication.kt`: Handles the admin login functionality.
-   `Station.kt`: Data class representing a train station.
-   `Ticket.kt`: Data class representing a purchased ticket.
-   `SpecialOffer.kt`: Data class for special offers.
-   `User.kt` / `AdminUser.kt`: Classes representing user types.
-   `TicketType.kt`: An enum for the different types of tickets (Single, Return).

## Group Members

*   **Andrei Mungiu** (Member A)
*   **Florin Gabriel Ivan** (Member B)
*   **Daniel Vasile Anitas** (Member C)
