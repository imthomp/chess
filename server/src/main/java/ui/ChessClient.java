package ui;

import java.util.Scanner;

public class ChessClient {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Display a welcome message
        System.out.println("Welcome to the Chess Client!");

        // Main program loop
        boolean running = true;
        while (running) {
            // Display menu options
            System.out.println("\nMain Menu:");
            System.out.println("1. Register");
            System.out.println("2. Login");
            System.out.println("3. Exit");
            System.out.print("Enter your choice: ");

            // Read user input
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline character

            // Handle user choice
            switch (choice) {
                case 1:
                    registerUser();
                    break;
                case 2:
                    loginUser();
                    break;
                case 3:
                    System.out.println("Exiting Chess Client. Goodbye!");
                    running = false;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }

        // Close the scanner
        scanner.close();
    }

    private static void registerUser() {
        // Implement user registration logic

    }

    private static void loginUser() {
        // Implement user login logic

    }
}
