package com.gloatyuk.solvex;

import java.math.MathContext;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.Stack;

public class Main {
    static Map<String, Double> variables = new HashMap<>();
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
        System.out.print("\n");
    }

    public static void settings() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Settings Menu" + "\n");
        System.out.println("precision - Sets the precision of float outputs" + "\n");
        System.out.println("Command: ");
        String command = scanner.nextLine().trim();
        if (command.equalsIgnoreCase("precision")) {
            System.out.println("Precision Value: ");
            int precision = scanner.nextInt();
            MathContext mc = new MathContext(precision);
            // not working at the moment
        } else {
            System.out.println("Invalid Command, please try again");
            scanner.nextLine();
        }
    }

    public static void variables() {
        Scanner scanner = new Scanner(System.in);
        String[] varNames = {"X", "Y", "Z", "A", "B", "C", "D", "E", "F"};
        for (String name : varNames) {
            variables.put(name, 0.0);
        }
        System.out.print("\n");
        System.out.println("Variable Menu" + "\n");
        System.out.println("edit --VAR - Edit a variable's data");
        System.out.println("recall - Show values of all variables");
        System.out.println("return - Exit variable menu");
        System.out.println("Command: ");
        String command = scanner.nextLine().trim();
        if (command.equalsIgnoreCase("return")) {
            System.out.println("Exiting Variable Menu...");
            menu();
        }
        if (command.equalsIgnoreCase("recall")) {
            System.out.println("Current Variable Values: " + "\n");
            for (String name : varNames) {
                System.out.println(name + " Value: " + variables.get(name));
            }
            System.out.print("Press enter to continue...");
            scanner.nextLine();
            variables();
        }
        if (command.toLowerCase().startsWith("edit --")) {
            String varName = command.substring(7).trim().toUpperCase();
            if (variables.containsKey(varName)) {
                while (true) {
                    System.out.print("Enter new value for " + varName + ": ");
                    try {
                        double newValue = Double.parseDouble(scanner.nextLine().trim());
                        variables.put(varName, newValue);
                        System.out.println(varName + " updated to " + newValue);
                        break;
                    }
                    catch (NumberFormatException e){
                        System.out.println("Invalid entry, please try again. Press enter to continue...");
                        scanner.nextLine();
                    }
                }
            }
        }
    }

    public static void pythagoras() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("\n");
        System.out.println("Pythagoras Calculations");
        System.out.println("standard - A^2 + B^2 = C^2");
        System.out.println("reverse - C^2 - A^2 = B^2");
        System.out.print("Command: ");
        String command = scanner.nextLine().trim();
        if (command.equalsIgnoreCase("standard")) {
            System.out.print("\n");
            System.out.print("A Value: ");
            double aValue = scanner.nextInt();
            System.out.print("B Value: ");
            double bValue = scanner.nextInt();
            double cValue = Math.sqrt(Math.pow(aValue, 2) + Math.pow(bValue, 2));
            System.out.println("Hypotenuse Length: " + cValue + "\n");
        }
        if (command.equalsIgnoreCase("reverse")) {
            System.out.print("\n");
            System.out.print("C Value: ");
            double cValue = scanner.nextInt();
            System.out.print("A Value: ");
            double aValue = scanner.nextInt();
            double bValue = Math.sqrt(Math.pow(cValue, 2) - Math.pow(aValue, 2));
            System.out.println("Side Length: " + bValue + "\n");
        }
        else {
            System.out.print("Invalid Command, press enter to try again...");
            scanner.nextLine();
            pythagoras();
        }
    }

    public static List<String> toPostfix(String infix) {
        List<String> output = new ArrayList<>();
        Stack<Character> operators = new Stack<>();
        int i = 0;
        while (i < infix.length()) {
            char c = infix.charAt(i);
            if (Character.isDigit(c) || c == '.') {
                StringBuilder number = new StringBuilder();
                while (i < infix.length() && (Character.isDigit(infix.charAt(i)) || infix.charAt(i) == '.')) {
                    number.append(infix.charAt(i));
                    i++;
                }
                output.add(number.toString());
                continue;
            }
            if (c == '(') {
                operators.push(c);
            }
            else if (c == ')') {
                while (!operators.isEmpty() && operators.peek() != '(') {
                    output.add(String.valueOf(operators.pop()));
                }
                if (!operators.isEmpty() && operators.peek() != '(') {
                    operators.pop();
                }
            }
            else if (isOperator(c)) {
                while (!operators.isEmpty() && precedence(operators.peek()) >= precedence(c)) {
                    output.add(String.valueOf(operators.pop()));
                }
                operators.push(c);
            }
            i++;
        }
        while (!operators.isEmpty()) {
            output.add(String.valueOf(operators.pop()));
        }
        return output;
    }

    public static double evaluatePostfix(List<String> postfix) {
        Stack<Double> stack = new Stack<>();
        for (String token : postfix) {
            if (isOperator(token.charAt(0)) && token.length() == 1) {
                double b = stack.pop();
                double a = stack.pop();
                switch (token.charAt(0)) {
                    case '+' -> stack.push(a+b);
                    case '-' -> stack.push(a-b);
                    case '*' -> stack.push(a*b);
                    case '/' -> stack.push(a/b);
                }
            }
            else {
                stack.push(Double.parseDouble(token));
            }
        }
        return stack.pop();
    }

    public static boolean isOperator(char c) {
        return c == '+' || c == '-' || c == '*' || c == '/';
    }

    public static int precedence(char op) {
        return switch (op) {
            case '+', '-' -> 1;
            case '*', '/' -> 2;
            default -> 0;
        };
    }

    public static double calculationEngine(String equation) {
        equation = equation.replaceAll("\\s", "");
        List<String> postfix = toPostfix(equation);
        return evaluatePostfix(postfix);
    }

    public static void calculate() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Calculation Menu\n");
        System.out.println("Please enter equation...");
        System.out.print("Equation: ");
        String equation = scanner.nextLine();
        System.out.println("Calculating...");
        double result = calculationEngine(equation);
        System.out.println("Result: " + result);
        try {
            Thread.sleep(1500);
        }
        catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
        }
    }

    public static void algebra() {
        System.out.println("Algebra Menu" + "\n");

    }
    public static void menu() {
        Scanner scanner = new Scanner(System.in);
        OSIdentify();
        System.out.println("SolveX Main Menu");
        System.out.println("variable - Opens Variable Menu");
        System.out.println("calculate - Opens Calculation Engine");
        System.out.println("probabilities - Open Probability Engine");
        System.out.println("pythagoras - Opens Pythagoras Calculations");
        System.out.println("algebra - Opens Algebraic Calculations");
        System.out.println("settings - Opens Settings Menu");
        System.out.println("help - Opens Help Menu");
        System.out.println("exit - Exit the program");
        System.out.print("Command: ");
        String command = scanner.nextLine().trim();
        switch (command) {
            case "variable": variables(); break;
            case "calculate": calculate(); break;
            //case "probabilities": probability(); break;
            case "algebra": algebra(); break;
            case "pythagoras": pythagoras(); break;
            case "settings": settings(); break;
            //case "help": help(); break;
            case "exit": exit(0); break;
            default: System.out.println("Invalid Command, please try again");
        }
        System.out.println("Press enter to continue...");
        scanner.nextLine();
        menu();
    }

    public static void exit(int code) {
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
        menu();
    }
}