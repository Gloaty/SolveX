package com.gloatyuk.solvex;

import java.math.MathContext;
import java.util.*;
import java.util.function.Function;

/**
 * SolveX - A comprehensive calculator application with support for basic arithmetic,
 * trigonometry, algebra, and variable management.
 */
public class Main {
    
    /**
     * Represents a single calculation entry in the history.
     * Stores both the original equation and its computed result.
     */
    public static class HistoryEntry {
        String equation;
        String result;

        public HistoryEntry(String equation, String result) {
            this.equation = equation;
            this.result = result;
        }

        @Override
        public String toString() {
            return equation + " = " + result;
        }
    }
    
    // Static map containing all supported trigonometric functions
    static final Map<String, Function<Double, Double>> trigFunctions = new HashMap<>();
    
    // Initialize trigonometric functions with their implementations
    static {
        // Standard trigonometric functions
        trigFunctions.putAll(Map.of(
                "sin", Math::sin, "cos", Math::cos, "tan", Math::tan,
                "csc", x -> 1 / Math.sin(x),    // Cosecant
                "sec", x -> 1 / Math.cos(x),    // Secant
                "cot", x -> 1 / Math.tan(x)));  // Cotangent
        
        // Inverse trigonometric functions
        trigFunctions.putAll(Map.of(
                "arcsin", Math::asin, "arccos", Math::acos, "arctan", Math::atan,
                "arccsc", x -> 1 / Math.asin(x),    // Inverse cosecant
                "arcsec", x -> 1 / Math.acos(x),    // Inverse secant
                "arccot", x -> 1 / Math.atan(x)));  // Inverse cotangent
        
        // Hyperbolic functions
        trigFunctions.putAll(Map.of(
                "sinh", Math::sinh, "cosh", Math::cosh, "tanh", Math::tanh,
                "csch", x -> 1 / Math.sinh(x),  // Hyperbolic cosecant
                "sech", x -> 1 / Math.cosh(x),  // Hyperbolic secant
                "coth", x -> 1 / Math.tanh(x))); // Hyperbolic cotangent
        
        // Inverse hyperbolic functions (implemented manually since Java doesn't provide them)
        trigFunctions.putAll(Map.of(
                "arcsinh", x -> Math.log(x + Math.sqrt(x * x + 1)),
                "arccosh", x -> Math.log(x + Math.sqrt(x * x - 1)),
                "arctanh", x -> 0.5 * Math.log((1 + x) / (1 - x)),
                "arccsch", x -> Math.log(1 / x + Math.sqrt(1 + 1 / (x * x))),
                "arcsech", x -> Math.log(1 / x + Math.sqrt(1 / (x * x) - 1)),
                "arccoth", x -> 0.5 * Math.log((x + 1) / (x - 1))));
    }
    
    // Global variables storage for user-defined variables
    static Map<String, Double> variables = new HashMap<>();
    
    // History stack to store all calculations performed in the session
    static Stack<HistoryEntry> calculationHistory = new Stack<>();
    
    // Application settings (radian mode, precision, etc.)
    static final Map<String, Boolean> settings = new HashMap<>(Map.of("radianMode", false, "precision", false));
    
    /**
     * Identifies and displays the current operating system.
     * Used for informational purposes at startup.
     */
    public static void OSIdentify() {
        // Get OS name from system properties
        String os = System.getProperty("os.name").toLowerCase();
        System.out.print("Operating System Detected - ");
        
        // Check for different OS types based on name patterns
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

    /**
     * Handles the settings menu where users can configure application preferences.
     * Currently supports precision and radian mode settings.
     */
    public static void settings() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("=== Settings Menu ===\n");
        System.out.println("precision - Sets the precision of float outputs\n");
        System.out.println("radianMode - Toggle between Radian and Degree mode\n");
        System.out.print("Command: ");
        String command = scanner.nextLine().trim();
        
        // Handle precision setting (currently not fully implemented)
        if (command.equalsIgnoreCase("precision")) {
            System.out.println("Precision Value: ");
            int precision = scanner.nextInt();
            MathContext mc = new MathContext(precision);
            // TODO: Implement precision functionality
        }
        
        // Handle radian mode toggle
        if (command.equalsIgnoreCase("radianMode")) {
            String currentMode;
            if (settings.get("radianMode").equals(true)) {
                currentMode = "Radian";
            }
            else {
                currentMode = "Degrees";
            }
            System.out.println("Current Mode: " + currentMode);
        }
        else {
            System.out.println("Invalid Command, please try again");
            scanner.nextLine();
        }
    }

    /**
     * Manages user-defined variables (X, Y, Z, A, B, C, D, E, F).
     * Allows users to edit variable values and recall current values.
     */
    public static void variables() {
        Scanner scanner = new Scanner(System.in);
        
        // Initialize default variable names
        String[] varNames = {"X", "Y", "Z", "A", "B", "C", "D", "E", "F"};
        
        // Set all variables to default value of 0.0
        for (String name : varNames) {
            variables.put(name, 0.0);
        }
        
        System.out.print("\n");
        System.out.println("=== Variable Menu ===\n");
        System.out.println("edit --VAR - Edit a variable's data");
        System.out.println("recall - Show values of all variables");
        System.out.println("return - Exit variable menu");
        System.out.print("Command: ");
        String command = scanner.nextLine().trim();
        
        // Handle menu navigation
        if (command.equalsIgnoreCase("return")) {
            System.out.println("Exiting Variable Menu...");
            menu();
        }
        
        // Display all variable values
        if (command.equalsIgnoreCase("recall")) {
            System.out.println("Current Variable Values: \n");
            for (String name : varNames) {
                System.out.println(name + " Value: " + variables.get(name));
            }
            System.out.print("Press enter to continue...");
            scanner.nextLine();
            variables();
        }
        
        // Handle variable editing with format "edit --VARNAME"
        if (command.toLowerCase().startsWith("edit --")) {
            String varName = command.substring(7).trim().toUpperCase();
            if (variables.containsKey(varName)) {
                // Input validation loop for numeric values
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
    
    /**
     * Main trigonometry menu that handles all trigonometric calculations,
     * conversions, and Pythagorean theorem calculations.
     */
    public static void trigonometry() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("\n=== Trigonometric Calculations ===\n");
        
        // Display current angle mode
        if (Boolean.TRUE.equals(settings.get("radianMode"))) {
            System.out.println("Radian Mode");
        }
        else {
            System.out.println("Degree Mode");
        }
        
        System.out.println("NOTE - All input angles must be given as radians. ");
        System.out.println("pythagoras - Open Pythagoras Menu");
        System.out.println("calculate - Basic 1-step Trig Calculations");
        System.out.println("convert - Convert degrees to radians, and vice versa");
        System.out.println("back - Return to previous menu");
        System.out.print("Command: ");
        String command = scanner.nextLine().trim().toLowerCase();
        
        if (command.equalsIgnoreCase("pythagoras")) {
            pythagoras();
        }
        
        // Handle trigonometric function calculations
        if (command.equalsIgnoreCase("calculate")) {
            // Display available trigonometric functions
            System.out.println("Valid command and operators: ");
            System.out.println("sin - Trigonometric Sine");
            System.out.println("cos - Trigonometric Cosine");
            System.out.println("tan - Trigonometric Tangent");
            System.out.println("csc - Trigonometric Cosecant");
            System.out.println("sec - Trigonometric Secant");
            System.out.println("cot - Trigonometric Cotangent");
            System.out.println("arc - Open Inverse menu");
            System.out.println("hyp - Open Hyperbolic menu");
            System.out.println("archyp - Open Inverse Hyperbolic menu");
            System.out.print("Command: ");
            command = scanner.nextLine().trim().toLowerCase();
            
            // Handle inverse trigonometric functions menu
            if (command.equalsIgnoreCase("arc")) {
                System.out.println("Inverse Trigonometry Commands: ");
                System.out.println("arcsin - Inverse Sine");
                    System.out.println("arccos - Inverse Cosine");
                System.out.println("arctan - Inverse Tangent");
                System.out.println("arccsc - Inverse Cosecant");
                System.out.println("arcsec - Inverse Secant");
                System.out.println("arccot - Inverse Cotangent\n");
                System.out.println("Press any key to continue...");
                scanner.nextLine();
                trigonometry();
            }
            
            // Handle hyperbolic functions menu
            if (command.equalsIgnoreCase("hyp")) {
                System.out.println("sinh - Hyperbolic Sine");
                System.out.println("cosh - Hyperbolic Cosine");
                System.out.println("tanh - Hyperbolic Tangent");
                System.out.println("csch - Hyperbolic Cosecant");
                System.out.println("sech - Hyperbolic Secant");
                System.out.println("coth - Hyperbolic Cotangent\n");
                scanner.nextLine();
                trigonometry();
            }
            
            // Handle inverse hyperbolic functions menu
            if (command.equalsIgnoreCase("archyp")) {
                System.out.println("arcsinh - Inverse Hyperbolic Sine");
                System.out.println("arccosh - Inverse Hyperbolic Cosine");
                System.out.println("arctanh - Inverse Hyperbolic Tangent");
                System.out.println("arccsch - Inverse Hyperbolic Cosecant");
                System.out.println("arcsech - Inverse Hyperbolic Secant");
                System.out.println("arccoth - Inverse Hyperbolic Cotangent");
                trigonometry();
            }
            else {
                // Execute the trigonometric function if it exists
                if (trigFunctions.containsKey(command)) {
                    System.out.print("Enter angle: ");
                    try {
                        double angleValue = scanner.nextDouble();
                        double result;
                        Function<Double, Double> trigOperation = trigFunctions.get(command);
                        
                        // Apply angle mode conversion if needed
                        if (Boolean.TRUE.equals(settings.get("radianMode"))) {
                            result = trigOperation.apply(angleValue);
                        }
                        else {
                            // Convert degrees to radians for calculation, then result back if needed
                            result = Math.toDegrees(trigOperation.apply(Math.toRadians(angleValue)));
                        }
                        
                        System.out.println(command + "(" + angleValue + ") = " + result);
                        calculationHistory.push(new HistoryEntry(command + "(" + angleValue + ")", Double.toString(result)));
                        System.out.print("Press enter to continue...");
                        scanner.nextLine();
                        scanner.nextLine();
                        trigonometry();
                    }
                    catch (InputMismatchException e) {
                        System.out.println("Invalid value, please try again. ");
                        System.out.println("Press enter to continue...");
                        scanner.nextLine();
                        trigonometry();
                    }
                }
                else {
                    System.out.println("Invalid command, please try again. ");
                    System.out.println("Press enter to continue...");
                    scanner.nextLine();
                    trigonometry();
                }
            }
        }
        
        // Handle angle conversion between degrees and radians
        if (command.equalsIgnoreCase("convert")) {
            System.out.println("\n== Conversion Menu ===\n");
            System.out.println("degree - Convert radians to degrees");
            System.out.println("radian - Convert degrees to radians");
            System.out.println("back - Return to previous menu");
            System.out.print("Command: ");
            command = scanner.nextLine().trim().toLowerCase();
            double input;
            
            switch (command) {
                case "degree":
                    System.out.print("Input radians: ");
                    input = scanner.nextDouble();
                    double degrees = Math.toDegrees(input);
                    System.out.println("Output degrees: " + degrees);
                    System.out.println("Press enter to continue...");
                    calculationHistory.push(new HistoryEntry(input + "Radians = " + degrees + "Degrees", Double.toString(degrees)));
                    scanner.nextLine();
                    trigonometry();
                case "radian":
                    System.out.print("Input degrees: ");
                    input = scanner.nextDouble();
                    double radians = Math.toRadians(input);
                    System.out.println("Output radians: " + radians);
                    System.out.println("Press enter to continue...");
                    calculationHistory.push(new HistoryEntry(input + "Degrees = " + radians + "Radians", Double.toString(radians)));
                    scanner.nextLine();
                    trigonometry();
                case "back":
                    menu();
                default:
                    System.out.println("Invalid command, please try again");
                    System.out.println("Press enter to continue...");
                    scanner.nextLine();
                    trigonometry();
                }
            }
            
            if (command.equalsIgnoreCase("back")) {
                menu();
            }
            else {
                System.out.println("Invalid command, please try again...");
                System.out.println("Press enter to continue...");
                scanner.nextLine();
            }
        trigonometry();
    }

    /**
     * Handles Pythagorean theorem calculations for right triangles.
     * Supports both standard (A² + B² = C²) and reverse (C² - A² = B²) calculations.
     */
    public static void pythagoras() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("\n=== Pythagoras Calculations ===\n");
        System.out.println("standard - A^2 + B^2 = C^2");
        System.out.println("reverse - C^2 - A^2 = B^2");
        System.out.println("back - Return to previous menu");
        System.out.print("Command: ");
        String command = scanner.nextLine().trim();
        
        // Standard Pythagorean theorem: find hypotenuse
        if (command.equalsIgnoreCase("standard")) {
            System.out.print("\n");
            System.out.print("A Value: ");
            double aValue = scanner.nextInt();
            System.out.print("B Value: ");
            double bValue = scanner.nextInt();
            double cValue = Math.sqrt(Math.pow(aValue, 2) + Math.pow(bValue, 2));
            System.out.println("Hypotenuse Length: " + cValue + "\n");
            String equation = aValue + "^2 + " + bValue + "^2";
            calculationHistory.push(new HistoryEntry(equation, Double.toString(cValue)));
        }
        
        // Reverse Pythagorean theorem: find one side given hypotenuse and other side
        if (command.equalsIgnoreCase("reverse")) {
            System.out.print("\n");
            System.out.print("C Value: ");
            double cValue = scanner.nextInt();
            System.out.print("A Value: ");
            double aValue = scanner.nextInt();
            double bValue = Math.sqrt(Math.pow(cValue, 2) - Math.pow(aValue, 2));
            System.out.println("Side Length: " + bValue + "\n");
            String equation = aValue + "^2 - " + bValue + "^2";
            calculationHistory.push(new HistoryEntry(equation, Double.toString(cValue)));
        }
        
        if (command.equalsIgnoreCase("back")) {
            menu();
        }
        else {
            System.out.print("Invalid Command, press enter to try again...");
            scanner.nextLine();
            pythagoras();
        }
    }

    /**
     * Converts infix notation to postfix notation using the Shunting Yard Algorithm.
     * This is crucial for proper order of operations in mathematical expressions.
     * 
     * @param infix The mathematical expression in infix notation
     * @return List of tokens in postfix notation
     */
    public static List<String> toPostfix(String infix) {
        List<String> output = new ArrayList<>();
        Stack<Character> operators = new Stack<>();
        int i = 0;
        
        while (i < infix.length()) {
            char c = infix.charAt(i);
            
            // Handle multi-digit numbers and decimals
            if (Character.isDigit(c) || c == '.') {
                StringBuilder number = new StringBuilder();
                while (i < infix.length() && (Character.isDigit(infix.charAt(i)) || infix.charAt(i) == '.')) {
                    number.append(infix.charAt(i));
                    i++;
                }
                output.add(number.toString());
                continue;
            }
            
            // Handle opening parenthesis
            if (c == '(') {
                operators.push(c);
            }
            // Handle closing parenthesis
            else if (c == ')') {
                while (!operators.isEmpty() && operators.peek() != '(') {
                    output.add(String.valueOf(operators.pop()));
                }
                if (!operators.isEmpty() && operators.peek() == '(') {
                    operators.pop(); // Remove the opening parenthesis
                }
            }
            // Handle operators based on precedence
            else if (isOperator(c)) {
                while (!operators.isEmpty() && precedence(operators.peek()) >= precedence(c)) {
                    output.add(String.valueOf(operators.pop()));
                }
                operators.push(c);
            }
            i++;
        }
        
        // Pop remaining operators
        while (!operators.isEmpty()) {
            output.add(String.valueOf(operators.pop()));
        }
        return output;
    }

    /**
     * Evaluates a mathematical expression in postfix notation.
     * 
     * @param postfix List of tokens in postfix notation
     * @return The calculated result
     */
    public static double evaluatePostfix(List<String> postfix) {
        Stack<Double> stack = new Stack<>();
        
        for (String token : postfix) {
            // If token is an operator, pop two operands and apply operation
            if (isOperator(token.charAt(0)) && token.length() == 1) {
                double b = stack.pop();
                double a = stack.pop();
                switch (token.charAt(0)) {
                    case '+' -> stack.push(a+b);
                    case '-' -> stack.push(a-b);
                    case '*' -> stack.push(a*b);
                    case '/' -> stack.push(a/b);
                    case '^' -> stack.push(Math.pow(a, b));
                    case '%' -> stack.push(a%b);
                }
            }
            // If token is a number, push it onto stack
            else {
                stack.push(Double.parseDouble(token));
            }
        }
        return stack.pop();
    }

    /**
     * Checks if a character is a mathematical operator.
     * 
     * @param c The character to check
     * @return true if the character is an operator, false otherwise
     */
    public static boolean isOperator(char c) {
        return c == '+' || c == '-' || c == '*' || c == '/' || c == '^' || c == '%';
    }

    /**
     * Returns the precedence value of an operator for the correct order of operations.
     * Higher values indicate higher precedence.
     * 
     * @param op The operator character
     * @return The precedence value
     */
    public static int precedence(char op) {
        return switch (op) {
            case '+', '-' -> 1;         // Lowest precedence
            case '*', '/', '%' -> 2;    // Medium precedence
            case '^' -> 3;              // Highest precedence
            default -> 0;               // Invalid operator
        };
    }

    /**
     * Main calculation engine that processes mathematical expressions.
     * Handles both standard arithmetic and special functions like pow() and root().
     * 
     * @param equation The mathematical expression to evaluate
     * @return The calculated result
     */
    public static double calculationEngine(String equation) {
        // Remove all whitespace from the equation
        equation = equation.replaceAll("\\s", "");
        
        // Handle special functions: pow() and root()
        if (equation.contains("pow") || equation.contains("root")) {
            double targetNumber = Double.parseDouble(equation.substring(equation.indexOf("(")+1, equation.indexOf(",")));
            double power = Double.parseDouble(equation.substring(equation.indexOf(",")+1, equation.indexOf(")")));
            
            if (equation.contains("pow")) {
                return Math.pow(targetNumber, power);
            }
            if (equation.contains("root")) {
                return Math.pow(targetNumber, 1/power);
            }
        }
        
        // For standard arithmetic, use postfix evaluation
        List<String> postfix = toPostfix(equation);
        return evaluatePostfix(postfix);
    }

    /**
     * Main calculation interface that handles user input and displays results.
     * Provides a continuous calculation loop until user chooses to go back.
     */
    public static void calculate() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("=== Calculation Menu ===\n");
        System.out.println("Enter 'back' to return to menu");
        System.out.println("Please enter equation...");
        System.out.print("Equation: ");
        String equation = scanner.nextLine();
        
        if (equation.equalsIgnoreCase("back")) {
            menu();
        }
        
        System.out.println("Calculating...");
        double result = calculationEngine(equation);
        calculationHistory.push(new HistoryEntry(equation, Double.toString(result)));
        System.out.println("Result: " + result);
        
        // Brief pause for user experience
        try {
            Thread.sleep(1500);
        }
        catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
        }
        calculate(); // Continue calculation loop
    }

    /**
     * Handles algebraic calculations including quadratic equations and solving for x.
     */
    public static void algebra() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("=== Algebra Menu ===\n");
        System.out.println("quadratic - Solve quadratic");
        System.out.println("solvex - Solve for a value of X");
        System.out.print("Command: ");
        String command = scanner.nextLine().trim().toLowerCase();
        
        switch (command) {
            case "quadratic":
                // Quadratic formula: (-b ± √(b² - 4ac)) / 2a
                System.out.println("Quadratics must be given in the form Ax^2 + Bx + C = 0");
                System.out.print("A Value: ");
                double aValue = scanner.nextDouble();
                System.out.print("B Value: ");
                double bValue = scanner.nextDouble();
                System.out.print("C Value: ");
                double cValue = scanner.nextDouble();
                
                // Calculate both roots
                double resultPos = ((-bValue + Math.sqrt(Math.pow(bValue, 2) - (4 * aValue * cValue))) / (2 * aValue));
                double resultNeg = ((-bValue - Math.sqrt(Math.pow(bValue, 2) - (4 * aValue * cValue))) / (2 * aValue));
                
                System.out.println("+Result: " + resultPos);
                System.out.println("-Result: " + resultNeg);
                calculationHistory.push(new HistoryEntry( aValue + "x^2 + " + bValue + "x + " + cValue, resultPos + ", " + resultNeg));
                algebra();
                
            case "solvex":
                // Solve linear equations for x
                System.out.println("Solving for Value of X: ");
                System.out.print("Coefficient of X: ");
                double xCoefficient = scanner.nextDouble();
                scanner.nextLine();
                System.out.print("X Equation: ");
                String xEquation = scanner.nextLine();
                
                double result = calculationEngine(xEquation);
                double xFinal = result / xCoefficient;
                System.out.println("Value of X: " + xFinal);
                calculationHistory.push(new HistoryEntry(xCoefficient + "x = " + xEquation +" = x", Double.toString(xFinal)));
                algebra();
                
            case "back":
                menu();
                
            default:
                System.out.println("Invalid command, please try again. ");
                System.out.println("Press enter to continue...");
                scanner.nextLine();
                algebra();
        }
    }
    
    /**
     * Manages the calculation history functionality.
     * Allows users to view and clear their calculation history.
     */
    public static void historyMenu() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("=== History Menu ===\n");
        System.out.println("view - View this session's history");
        System.out.println("clear - Clear this session's history");
        System.out.println("back - Return to main menu");
        String command = scanner.nextLine().trim().toLowerCase();
        
        switch (command) {
            case "view":
                // Display all calculation history entries
                for (HistoryEntry historyEntry : calculationHistory) {
                    System.out.println(historyEntry);
                }
                try {
                    Thread.sleep(1500);
                }
                catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                }
                System.out.println("Press enter to continue...");
                scanner.nextLine();
                historyMenu();
                
            case "clear":
                // Clear all history entries
                calculationHistory.empty();
                System.out.println("History cleared.");
                System.out.println("Press enter to continue...");
                scanner.nextLine();
                historyMenu();
                
            case "back":
                menu();
                
            default:
                System.out.println("Invalid command, please try again.");
                System.out.println("Press enter to continue...");
                scanner.nextLine();
                historyMenu();
        }
    }
    
    /**
     * Main menu interface that provides access to all calculator features.
     * Serves as the primary navigation hub for the application.
     */
    public static void menu() {
        Scanner scanner = new Scanner(System.in);
        OSIdentify(); // Display OS information
        
        System.out.println("=== SolveX Main Menu ===\n");
        System.out.println("variable - Opens Variable Menu");
        System.out.println("calculate - Opens Calculation Engine");
        System.out.println("probabilities - Open Probability Engine");
        System.out.println("trigonometry - Opens Trigonometric Calculations");
        System.out.println("algebra - Opens Algebraic Calculations");
        System.out.println("settings - Opens Settings Menu");
        System.out.println("history - Opens History Menu");
        System.out.println("help - Opens Help Menu");
        System.out.println("exit - Exit the program");
        System.out.print("Command: ");
        String command = scanner.nextLine().trim().toLowerCase();
        
        // Route to appropriate menu based on user input
        switch (command) {
            case "variable": variables(); break;
            case "calc":
            case "calculate": calculate(); break;
            //case "probabilities": probability(); break;  // Not implemented
            case "algebra": algebra(); break;
            case "trig":
            case "trigonometry": trigonometry(); break;
            case "settings": settings(); break;
            //case "help": help(); break;  // Not implemented
            case "exit": exit(0); break;
            case "history": historyMenu(); break;
            default: System.out.println("Invalid Command, please try again");
        }
        
        System.out.println("Press enter to continue...");
        scanner.nextLine();
        menu(); // Return to main menu
    }

    /**
     * Gracefully exits the application with a specified exit code.
     * 
     * @param code The exit code to return to the system
     */
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

    /**
     * Main entry point of the application.
     * Initializes the calculator and starts the main menu.
     * 
     * @param args Command line arguments (not used)
     */
    public static void main(String[] args) {
        menu();
    }
}