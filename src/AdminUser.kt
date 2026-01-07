package OOP_Group_Project

class AdminUser(
    username: String,
    password: String,
    val adminLevel: Int = 1
) : User(username, password, true)
