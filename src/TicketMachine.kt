package OOP_Group_Project

class TicketMachine(
    // The fixed location of the ticketMachine-Luton
    val originStation: Station,

    // The list of all destinations we can travel to
    val stations: MutableList<Station> = mutableListOf(),

    // Tracks the money currently in the machine
    var moneyInserted: Double = 0.0
) {

    var currentUser: User? = null
    val specialOffers: MutableList<SpecialOffer> = mutableListOf()

    //As written in the brief, the stations are hardcoded and not in a database
    init {
        stations.add(Station("London", 20.0, 35.0))
        stations.add(Station("Southampton", 10.0, 18.0))
        stations.add(Station("Manchester", 45.0, 80.0))
        stations.add(Station("Bristol", 25.0, 45.0))
        stations.add(Station("Birmingham", 30.0, 55.0))
    }


    //Admin functions, Implemented by member B

    // Function to show all stations in the console
    fun showAllStations() {
        println("\n--- Current Stations ---")
        for (station in stations) {
            println("Station: ${station.name}")
            println("  Single Price: £${station.singlePrice}")
            println("  Return Price: £${station.returnPrice}")
            println("  Sales: ${station.salesCount}")
            println("  Takings: £${station.totalTakings}")
            println("------------------------")
        }
    }

    // Function to add a new station dynamically
    fun addStation() {
        println("\n--- Add New Station ---")

        print("Enter Station Name: ")
        val nameInput = readln()

        print("Enter Single Ticket Price: ")
        val singlePriceInput = readln().toDouble()

        print("Enter Return Ticket Price: ")
        val returnPriceInput = readln().toDouble()

        val newStation = Station(nameInput, singlePriceInput, returnPriceInput)
        stations.add(newStation)
        println("Success! $nameInput has been added.")
    }

    // Function to edit an existing station
    fun editStation() {
        println("\n--- Edit Station ---")
        print("Enter the name of the station to edit: ")
        val searchName = readln()

        // Find the station
        val stationToEdit = stations.find { it.name == searchName }

        if (stationToEdit != null) {
            println("Editing station: ${stationToEdit.name}")

            print("Enter NEW Single Ticket Price: ")
            val newSingle = readln().toDouble()

            print("Enter NEW Return Ticket Price: ")
            val newReturn = readln().toDouble()

            // Update the station
            stationToEdit.singlePrice = newSingle
            stationToEdit.returnPrice = newReturn
            println("Success! Prices updated for ${stationToEdit.name}.")
        } else {
            println("Error: Station '$searchName' not found.")
        }
    }

    // Function to update all prices by a percentage factor
    fun updateAllPrices() {
        println("\n--- Update All Ticket Prices ---")
        println("Enter the price factor (e.g., 1.10 for +10% increase, 0.90 for -10% discount):")

        // Get the factor
        val factor = readln().toDouble()

        // Loop through every station
        for (station in stations) {
            // 3. Apply the math using *= operator
            station.singlePrice *= factor
            station.returnPrice *= factor
        }

        println("Success! All prices have been updated by a factor of $factor.")
    }

    //User functions. Implemented by member A.

    // Task (a): Search for a ticket.
    // A nullable return type (Pair?) is used to handle cases where the station isn't found.
    // Using ignoreCase=true makes it easier for the user while using lower case or upper case.
    fun searchTicket(destinationName: String, type: TicketType): Pair<Station, Double>? {
        if (destinationName.isBlank()) {
            println("Error: Destination cannot be empty.")
            return null
        }

        // Station search
        val match = stations.find { it.name.equals(destinationName, ignoreCase = true) }

        return if (match != null) {
            // If found, figure out if we need the Single or Return price.
            val price = getPriceForType(match, type)
            Pair(match, price)
        } else {
            null // Return null if destination isn't found.
        }
    }

    // Task (b): Insert money.
    // Allows the passenger to put cash into the machine loop.
    fun insertMoney() {
        println("\n--- Insert Money ---")
        println("Current Amount Inserted: £${formatPrice(moneyInserted)}")
        print("Enter amount to insert (£): ")

        val input = readLine()?.toDoubleOrNull()

        if (isValidAmount(input)) {
            moneyInserted += input!!
            println("Accepted: £${formatPrice(input)}")
        } else {
            println("Error: Invalid amount. Please enter a positive number.")
        }

        println("Total Money Inserted: £${formatPrice(moneyInserted)}")
    }

    // Task (c): Buy ticket.
    // Checks balance, calculates change, and records the statistics.
    fun buyTicket(destination: Station, price: Double, type: TicketType): Ticket? {
        if (moneyInserted >= price) {

            // Calculate Change
            val change = moneyInserted - price

            // Reset Balance
            moneyInserted = 0.0

            // Update Business Metrics
            recordSale(destination, price)

            // Create the Ticket object
            val newTicket = Ticket(originStation, destination, price, type)

            // Print the receipt
            printTicketASCII(newTicket)

            println("TRANSACTION SUCCESSFUL")
            println("Your Change: £${formatPrice(change)}")

            return newTicket
        } else {
            // Tell them how much is missing
            val missing = price - moneyInserted
            println("ERROR: Insufficient funds. You need £${formatPrice(missing)} more.")
            return null
        }
    }

    // Helper Functions to update the sales statistics

    private fun getPriceForType(station: Station, type: TicketType): Double {
        return if (type == TicketType.SINGLE) station.singlePrice else station.returnPrice
    }

    // Updates the sales stats for the Admin view
    private fun recordSale(station: Station, price: Double) {
        station.salesCount++
        station.totalTakings += price
    }

    // Helper to validate money input, ensures we don't accept negative numbers or nulls.
    private fun isValidAmount(amount: Double?): Boolean {
        return amount != null && amount > 0
    }

    // Helper for formatting currency, ensures all prices look like "10.00".
    private fun formatPrice(amount: Double): String {
        return "%.2f".format(amount)
    }

    // Prints the ticket in the specific ASCII format required from the brief
    private fun printTicketASCII(ticket: Ticket) {
        val typeFormatted = ticket.ticketType.name
        println("\n***")
        println("From: ${ticket.origin.name}")
        println("To:   ${ticket.destination.name}")
        println("Price: £${formatPrice(ticket.price)} [$typeFormatted]")
        println("***\n")
    }

    // Member C section: special offers and extras
    fun addStation(station: Station) {
        stations.add(station)
    }

    // This function adds a new offer to my list
    fun addSpecialOffer(offer: SpecialOffer) {
        specialOffers.add(offer)
        println("System: A new special offer was added successfully.")
    }

    // Deleting an offer based on the ID
    fun deleteSpecialOffer(id: Int) {
        // Use a simple loop to find the offer first
        var foundOffer: SpecialOffer? = null

        for (offer in specialOffers) {
            if (offer.offerId == id) {
                foundOffer = offer
            }
        }

        if (foundOffer != null) {
            specialOffers.remove(foundOffer)
            println("The offer with ID $id has been removed.")
        } else {
            println("Error: I could not find an offer with that ID.")
        }
    }

    // Search function for the offers
    fun searchSpecialOffers(stationName: String) {
        println("--- Searching Offers for $stationName ---")

        var foundAny = false

        // Looping through all offers to see if names match
        for (offer in specialOffers) {
            if (offer.station.name == stationName) {
                println("Found Offer:")
                println("- ID: ${offer.offerId}")
                println("- Discount: ${offer.discountPercent}%")
                println("- Dates: ${offer.startDate} to ${offer.endDate}")
                foundAny = true
            }
        }

        if (!foundAny) {
            println("Sorry, no special offers found for this station.")
        }
    }
}