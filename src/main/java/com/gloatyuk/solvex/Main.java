package com.gloatyuk.solvex;

import java.math.MathContext;
import java.util.Objects;
import java.util.Scanner;

public class Main {
    public static void OSIdentify() {
        String os = System.getProperty("os.name").toLowerCase();
        System.out.print("Operating System Detected - ");
        if (os.contains("win")) {
            System.out.println("Microsoft Windows");
        }
        else if (os.contains("nux") || os.contains("nix")) {
            System.out.println("Linux Distro");
        }
        else if (os.contains("mac")) {
            System.out.println("MacOS");
        }
        else if (os.contains("sunos") || os.contains("unix")) {
            System.out.println("Generic Unix");
        }
        else if (os.contains("freebsd")) {
            System.out.println("FreeBSD");
        }
        else {
            System.out.println("Unknown");
        }
        System.out.println("\n");
    }
    public static void Settings() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Settings Menu" + "\n");
        System.out.println("precision - Sets the precision of float outputs" + "\n");
        System.out.println("Command: ");
        String command = scanner.nextLine();
        if (Objects.equals(command, "precision")) {
            System.out.println("Precision Value: ");
            int precision = scanner.nextInt();
            MathContext mc = new MathContext(precision);
            // not working at the moment
        }
        else {
            System.out.println("Invalid Command, please try again");
            scanner.nextLine();
        }
    }
    public static void Variables() {
        double variableX, variableY, variableZ,variableA, variableB, variableC, variableD, variableE, variableF;
        System.out.print("\n");
        System.out.println("Variable Menu" + "\n");
        // get this shit working sooner or later
    }
    public static void Pythagoras() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("\n");
        System.out.println("Pythagoras Calculations");
        System.out.println("standard - A^2 + B^2 = C^2");
        System.out.println("reverse - C^2 - A^2 = B^2");
        String command = scanner.nextLine();
        if (Objects.equals(command, "standard")) {
            System.out.print("\n");
            System.out.print("A Value: ");
            int aValue = scanner.nextInt();
            System.out.print("B Value: ");
            int bValue = scanner.nextInt();
            double cValue = Math.sqrt(Math.pow(aValue, 2) + Math.pow(bValue, 2));
            System.out.println("Hypotenuse Length: " + cValue + "\n");
        }
        if (Objects.equals(command, "reverse")) {
            System.out.print("\n");
            System.out.print("C Value: ");
            int cValue = scanner.nextInt();
            System.out.print("A Value: ");
            int aValue = scanner.nextInt();
            double bValue = Math.sqrt(Math.pow(cValue, 2) - Math.pow(aValue, 2));
            System.out.println("Side Length: " + bValue + "\n");
        }
        else {
            System.out.print("Invalid Command, press enter to try again...");
            scanner.nextLine();
            Pythagoras();
        }
    }

    public static void Menu() {
        Scanner scanner = new Scanner(System.in);
        OSIdentify();
        System.out.println("SolveX Main Menu");
        System.out.println("variable - Opens Variable Menu");
        System.out.println("calculate - Opens Calculation Engine");
        System.out.println("probabilities - Open Probability Engine");
        System.out.println("pythagoras - Opens Pythagoras Calculations");
        System.out.println("settings - Opens Settings Menu");
        System.out.println("help - Opens Help Menu");
        System.out.println("exit - Exit the program");
        System.out.print("Command: ");
        String command = scanner.nextLine();
        if (Objects.equals(command, "variable")) {
            // Variables();
        }
        else if (Objects.equals(command, "calculate")) {
            // Calculator();
        }
        else if (Objects.equals(command, "probabilities")) {
            // Probability();
        }
        else if (Objects.equals(command, "pythagoras")) {
            Pythagoras();
        }
        else if (Objects.equals(command, "settings")) {
            Settings();
        }
        else if (Objects.equals(command, "help")) {
            // Help();
        }
        else if (Objects.equals(command, "exit")) {
            Exit(0);
        }
        else {
            System.out.println("Invalid Command, please try again");
        }
        System.out.println("Press enter to continue...");
        scanner.nextLine();
        Menu();
    }

    public static void Exit(int code) {
        System.out.println("Exiting program...");
        try {
            Thread.sleep(1000);
        }
        catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
        }
        System.exit(code);
    }

    public static void main(String[] args) {
        System.out.println("Hello World!");
        Menu();
    }
}