package com.example;

import com.example.dao.UserDAO;
import com.example.dao.UserDAOImpl;
import com.example.model.User;
import com.example.util.HibernateUtil;
import com.example.exception.UserException;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {
    private static final Logger logger = Logger.getLogger(Main.class.getName());
    private static final UserDAO userDAO = new UserDAOImpl();
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        try {
            displayMenu();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Application error", e);
        } finally {
            HibernateUtil.shutdown();
            scanner.close();
        }
    }

    private static void displayMenu() {
        while (true) {
            System.out.println("\n=== User Management System ===");
            System.out.println("1. Create User");
            System.out.println("2. Get User by ID");
            System.out.println("3. Get All Users");
            System.out.println("4. Update User");
            System.out.println("5. Delete User");
            System.out.println("6. Get User by Email");
            System.out.println("7. Exit");
            System.out.print("Choose an option: ");

            try {
                int choice = Integer.parseInt(scanner.nextLine());

                switch (choice) {
                    case 1:
                        createUser();
                        break;
                    case 2:
                        getUserById();
                        break;
                    case 3:
                        getAllUsers();
                        break;
                    case 4:
                        updateUser();
                        break;
                    case 5:
                        deleteUser();
                        break;
                    case 6:
                        getUserByEmail();
                        break;
                    case 7:
                        System.out.println("Exiting...");
                        return;
                    default:
                        System.out.println("Invalid option. Please try again.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            } catch (UserException e) {
                System.out.println("Error: " + e.getMessage());
            } catch (Exception e) {
                System.out.println("Unexpected error: " + e.getMessage());
            }
        }
    }

    private static void createUser() {
        System.out.println("\n--- Create User ---");
        System.out.print("Enter name: ");
        String name = scanner.nextLine();

        System.out.print("Enter email: ");
        String email = scanner.nextLine();

        System.out.print("Enter age: ");
        int age = Integer.parseInt(scanner.nextLine());

        User user = new User(name, email, age);
        User createdUser = userDAO.createUser(user);
        System.out.println("User created successfully: " + createdUser);
    }

    private static void getUserById() {
        System.out.println("\n--- Get User by ID ---");
        System.out.print("Enter user ID: ");
        Long id = Long.parseLong(scanner.nextLine());

        Optional<User> user = userDAO.getUserById(id);
        if (user.isPresent()) {
            System.out.println("User found: " + user.get());
        } else {
            System.out.println("User not found with ID: " + id);
        }
    }

    private static void getAllUsers() {
        System.out.println("\n--- All Users ---");
        List<User> users = userDAO.getAllUsers();
        if (users.isEmpty()) {
            System.out.println("No users found.");
        } else {
            users.forEach(System.out::println);
        }
    }

    private static void updateUser() {
        System.out.println("\n--- Update User ---");
        System.out.print("Enter user ID to update: ");
        Long id = Long.parseLong(scanner.nextLine());

        Optional<User> existingUser = userDAO.getUserById(id);
        if (existingUser.isEmpty()) {
            System.out.println("User not found with ID: " + id);
            return;
        }

        User user = existingUser.get();
        System.out.println("Current user: " + user);

        System.out.print("Enter new name (press enter to keep current): ");
        String name = scanner.nextLine();
        if (!name.isEmpty()) {
            user.setName(name);
        }

        System.out.print("Enter new email (press enter to keep current): ");
        String email = scanner.nextLine();
        if (!email.isEmpty()) {
            user.setEmail(email);
        }

        System.out.print("Enter new age (press enter to keep current): ");
        String ageInput = scanner.nextLine();
        if (!ageInput.isEmpty()) {
            user.setAge(Integer.parseInt(ageInput));
        }

        User updatedUser = userDAO.updateUser(user);
        System.out.println("User updated successfully: " + updatedUser);
    }

    private static void deleteUser() {
        System.out.println("\n--- Delete User ---");
        System.out.print("Enter user ID to delete: ");
        Long id = Long.parseLong(scanner.nextLine());

        boolean deleted = userDAO.deleteUser(id);
        if (deleted) {
            System.out.println("User deleted successfully.");
        } else {
            System.out.println("User not found with ID: " + id);
        }
    }

    private static void getUserByEmail() {
        System.out.println("\n--- Get User by Email ---");
        System.out.print("Enter email: ");
        String email = scanner.nextLine();

        Optional<User> user = userDAO.getUserByEmail(email);
        if (user.isPresent()) {
            System.out.println("User found: " + user.get());
        } else {
            System.out.println("User not found with email: " + email);
        }
    }
}