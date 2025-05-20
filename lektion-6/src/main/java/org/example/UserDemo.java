package org.example;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class UserDemo {
    public static void main(String[] args) {
        UserDao userDao = new UserDao();

        try {
            // 🔹 1. Skapa en ny användare
            User newUser = new User(
                    "anna.andersson@example.com",
                    "hashedpassword123",
                    "Anna",
                    "Andersson",
                    "070-1234567",
                    User.Role.CUSTOMER
            );
            User savedUser = userDao.save(newUser);
            System.out.println("✅ Skapad användare: " + savedUser);

            // 🔹 2. Hämta alla användare
            System.out.println("\n📋 Alla användare:");
            List<User> users = userDao.findAll();
            users.forEach(System.out::println);

            // 🔹 3. Uppdatera användaren
            savedUser.setPhone("070-7654321");
            savedUser.setRole(User.Role.ADMIN);
            userDao.save(savedUser);
            System.out.println("\n✏️ Uppdaterad användare: " + savedUser);

            // 🔹 4. Hämta med ID
            Optional<User> foundUser = userDao.findById(savedUser.getId());
            foundUser.ifPresent(user -> System.out.println("\n🔍 Hittad användare: " + user));

            // 🔹 5. Ta bort användaren
            boolean deleted = userDao.delete(savedUser.getId());
            System.out.println("\n🗑️ Användare raderad: " + deleted);

        } catch (SQLException e) {
            System.err.println("💥 Fel: " + e.getMessage());
            e.printStackTrace();
        } finally {
            DatabaseConnection.closeConnection();
        }
    }
}
