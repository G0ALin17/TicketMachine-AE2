package OOP_Group_Project
fun main() {
    // Setup origin station
    val originStation = Station("Luton", 0.0, 0.0)

    // Create the ticketmachine
    val machine = TicketMachine(originStation)

    // --- MEMBER C: Setup Auth ---
    val auth = Authentication()

    var systemActive = true

    println("******************************************")
    println("Welcome to the Central Ticket System")
    println("Current Location: ${originStation.name}")
    println("******************************************")

    // --- Main Menu ---
    // This loop lets us switch between Admin mode and User mode without restarting the program

    while (systemActive) {
        println("\n--- MAIN MENU: SELECT ROLE ---")
        println("1. Passenger (Buy Tickets)")
        println("2. Admin (Manage Stations & Prices)")
        println("3. Shut Down System")
        print("Please enter your choice (1-3): ")

        val input = readln()

        when (input) {
            "1" -> {
                // Launch the User Menu (Member A)
                runPassengerMode(machine)
            }
            "2" -> {
                // Launch the Admin Menu
                // Check Member C's login
                println("\n... Accessing Admin Panel ...")

                // MEMBER C: Authentication Check
                val user = auth.login()

                if (user != null) {
                    if (user.isAdmin) {
                        println("Login success! Welcome " + user.username)
                        // If login is good, we go to the main admin hub
                        runCombinedAdminHub(machine)
                    } else {
                        println("Error: You are logged in, but not an Admin.")
                    }
                } else {
                    println("Login failed.")
                }
            }
            "3" -> {
                println("Shutting down... Goodbye!")
                systemActive = false
            }
            else -> println("Invalid selection. Please try again.")
        }
    }
}

// Integration function ( Member B and Member C)
fun runCombinedAdminHub(machine: TicketMachine) {
    var inAdmin = true
    while (inAdmin) {
        println("\n=== SYSTEM ADMIN DASHBOARD ===")
        println("1. Station Management (Member B)")
        println("2. Special Offers (Member C)")
        println("3. Logout")
        print("Choose: ")

        val choice = readln()
        when (choice) {
            "1" -> runAdminMode(machine) // Calls Member B's original function
            "2" -> runSpecialOffersMenu(machine) // Calls Member C's function
            "3" -> inAdmin = false
            else -> println("Invalid option.")
        }
    }
}

// MEMBER B: Admin features
fun runAdminMode(machine: TicketMachine) {
    var adminRunning = true

    println("\n--- ADMIN PANEL (Member B) ---")

    while (adminRunning) {
        println("\n--- Admin Options ---")
        println("1. Show All Stations")
        println("2. Add a New Station")
        println("3. Edit an Existing Station")
        println("4. Update All Prices (Global)")
        println("5. Back to Main Menu")
        print("Select option: ")

        val input = readln()

        when (input) {
            "1" -> machine.showAllStations()
            "2" -> machine.addStation()
            "3" -> machine.editStation()
            "4" -> machine.updateAllPrices()
            "5" -> {
                println("Logging out of Admin Mode...")
                adminRunning = false // Breaks this loop.
            }
            else -> println("Please enter a number between 1 and 5.")
        }
    }
}


// MEMBER A: Passenger features

fun runPassengerMode(machine: TicketMachine) {
    var userRunning = true

    println("\n--- PASSENGER INTERFACE ---")

    while (userRunning) {
        // Print the sub-menu for passengers
        println("\n*****************************")
        println("1. Search and Buy Ticket")
        println("2. Insert Money")
        println("3. Back to Main Menu")
        println("*****************************")

        // Show current balance so they know if they need to add cash
        println("Current Balance: £%.2f".format(machine.moneyInserted))
        print("Select an option: ")
        //Read User Choice
        val input = readln()

        when (input) {
            "1" -> handleTicketSearchAndBuy(machine)
            "2" -> machine.insertMoney()
            "3" -> {
                // Return money if any is left before exiting
                if (machine.moneyInserted > 0) {
                    println("Refunded: £%.2f".format(machine.moneyInserted))
                }
                println("Returning to Main Menu...")
                userRunning = false
            }
            "4" -> showDebugStats(machine) // Hidden debug option
            else -> println("Invalid selection.")
        }
    }
}

// Function handleTicketSearchAndBuy handles the ticket purchasing flow:
fun handleTicketSearchAndBuy(machine: TicketMachine) {
    println("\n*** Search Ticket ***")

// Show the user what stations are available so they know what to type.
    println("Available destinations:")
    for (station in machine.stations) {
        println("* ${station.name}")
    }
    println("*********************")

    print("Enter destination station: ")
    val destName = readln()

    // Validate the ticket type input
    var type: TicketType? = null
    while (type == null) {
        print("Enter ticket type (1 for Single, 2 for Return): ")
        val typeInput = readln()
        when (typeInput) {
            "1" -> type = TicketType.SINGLE
            "2" -> type = TicketType.RETURN
            else -> println("Invalid input. Try again.")
        }
    }

    // Use the machine to find the ticket
    val result = machine.searchTicket(destName, type)

    if (result != null) {
        val station = result.first
        val price = result.second

        println("\nFound ticket to: ${station.name}")
        println("Ticket Type: $type")
        println("Price: £%.2f".format(price))

        // Check if user has enough money.
        while (machine.moneyInserted < price) {
            val missing = price - machine.moneyInserted
            println("\n(!) Insufficient funds. You need £%.2f more.".format(missing))
            println("Current Balance: £%.2f".format(machine.moneyInserted))
            print("Insert money now? (Y/N): ")

            val answer = readln().uppercase()
            if (answer == "Y") {
                machine.insertMoney() //Call insert money function

            } else {
                println("Transaction cancelled.")
                return // Exit function if they don't want to pay
            }
        }

        // If the user has enough money, processing purchase.
        println("\nProcessing purchase...")
        machine.buyTicket(station, price, type)

    } else {
        println("\nError: Destination '$destName' not found.")
        println("Please check the spelling and try again.")
    }
}

// Hidden function to check sales stats
fun showDebugStats(machine: TicketMachine) {
    println("\n*** Station Statistics (Debug) ***")
    for (s in machine.stations) {
        println("${s.name}: Sold ${s.salesCount}, Total: £%.2f".format(s.totalTakings))
    }
}

// MEMBER C: Offers and authentication
// This function is created to handle the menu for offers
fun runSpecialOffersMenu(machine: TicketMachine) {

    var loopActive = true

    while (loopActive) {
        println("\n=== SPECIAL OFFERS (Member C) ===")
        println("1. Add a new Special Offer")
        println("2. Search for Offers")
        println("3. Delete an Offer")
        println("4. Back to Dashboard")

        print("What do you want to do: ")
        val input = readln().trim()

        if (input == "1") {
            // Add a new offer
            println("--- New Offer ---")
            print("Enter Offer ID (number): ")
            // Used toIntOrNull to prevent crash if they type letters
            val id = readln().toIntOrNull()

            if (id != null) {
                print("Which Station Name: ")
                val stName = readln().trim()

                // Trying to find the station in the machine list
                val foundStation = machine.stations.find { it.name.equals(stName, ignoreCase = true) }

                if (foundStation != null) {
                    print("Discount % (e.g. 25.0): ")
                    val disc = readln().toDoubleOrNull() ?: 0.0

                    print("Start Date: ")
                    val sDate = readln()
                    print("End Date: ")
                    val eDate = readln()

                    // Create the object
                    val newOffer = SpecialOffer(id, foundStation, disc, sDate, eDate)

                    // Add it to the machine
                    machine.addSpecialOffer(newOffer)
                    println("Success! Offer added.")
                } else {
                    println("Sorry, that station does not exist in the system.")
                }
            } else {
                println("Invalid ID format.")
            }

        } else if (input == "2") {
            // Search logic
            print("Enter station name to see offers: ")
            val searchName = readln().trim()
            machine.searchSpecialOffers(searchName)

        } else if (input == "3") {
            // Delete logic
            print("Enter the Offer ID to delete: ")
            val delId = readln().toIntOrNull()
            if (delId != null) {
                machine.deleteSpecialOffer(delId)
            } else {
                println("That is not a valid number.")
            }

        } else if (input == "4") {
            println("Going back...")
            loopActive = false
        } else {
            println("I didn't understand that option.")
        }
    }
}