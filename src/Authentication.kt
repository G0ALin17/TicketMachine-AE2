package OOP_Group_Project
class Authentication {

    private val users = mutableListOf<User>()

    init {
        // hardcoded users for testing
        users.add(AdminUser("admin", "1234"))
        users.add(User("user1", "pass1"))
    }

    fun login(): User? {
        println("=== Login ===")
        print("Username: ")
        val username = readLine()?.trim() ?: ""
        print("Password: ")
        val password = readLine()?.trim() ?: ""

        val foundUser = users.find { it.username == username && it.password == password }

        if (foundUser == null) {
            println("Login failed. Try again.")
            return null
        }

        println("Welcome ${foundUser.username}!")
        return foundUser
    }
}
