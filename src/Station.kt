package OOP_Group_Project
data class Station(
    var name: String,
    var singlePrice: Double,
    var returnPrice: Double,
    var salesCount: Int = 0,        // Default to 0 sales
    var totalTakings: Double = 0.0  // Default to 0.0 earnings
)
