package com.gloatyuk.solvex;

import java.math.MathContext;
import java.util.*;
import java.util.function.Function;

public class Main {
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
    static final Map<String, Function<Double, Double>> trigFunctions = new HashMap<>();
    static {
        //Standard
        trigFunctions.put("sin", Math::sin);
        trigFunctions.put("cos", Math::cos);
        trigFunctions.put("tan", Math::tan);
        trigFunctions.put("csc", x -> 1/Math.sin(x));
        trigFunctions.put("sec", x -> 1/Math.cos(x));
        trigFunctions.put("cot", x -> 1/Math.tan(x));
        //Inverse
        trigFunctions.put("arcsin", Math::asin);
        trigFunctions.put("arccos", Math::acos);
        trigFunctions.put("arctan", Math::atan);
        trigFunctions.put("arccsc", x -> 1/Math.asin(x));
        trigFunctions.put("arcsec", x -> 1/Math.acos(x));
        trigFunctions.put("arccot", x -> 1/Math.atan(x));
        //Hyperbolic
        trigFunctions.put("sinh", Math::sinh);
        trigFunctions.put("cosh", Math::cosh);
        trigFunctions.put("tanh", Math::tanh);
        trigFunctions.put("csch", x -> 1/Math.sinh(x));
        trigFunctions.put("sech", x -> 1/Math.cosh(x));
        trigFunctions.put("coth", x -> 1/Math.tanh(x));
        //Hyperbolic Inverse
        trigFunctions.put("arcsinh", x -> Math.log(x + Math.sqrt(x * x + 1)));
        trigFunctions.put("arccosh", x -> Math.log(x + Math.sqrt(x * x - 1)));
        trigFunctions.put("arctanh", x -> 0.5 * Math.log((1 + x) / (1 - x)));
        trigFunctions.put("arccsch", x -> Math.log(1 / x + Math.sqrt(1 + 1 / (x * x))));
        trigFunctions.put("arcsech", x -> Math.log(1 / x + Math.sqrt(1 / (x * x) - 1)));
        trigFunctions.put("arccoth", x -> 0.5 * Math.log((x + 1) / (x - 1)));
    }
    static Map<String, Double> variables = new HashMap<>();
    static Stack<HistoryEntry> calculationHistory = new Stack<>();
    public static void OSIdentify() {
        //Identify the host OS
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
        //Settings menu
        Scanner scanner = new Scanner(System.in);
        System.out.println("=== Settings Menu ===\n");
        System.out.println("precision - Sets the precision of float outputs\n");
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
        //Variable menu
        Scanner scanner = new Scanner(System.in);
        String[] varNames = {"X", "Y", "Z", "A", "B", "C", "D", "E", "F"};
        for (String name : varNames) {
            variables.put(name, 0.0);
        }
        System.out.print("\n");
        System.out.println("=== Variable Menu ===\n");
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
            System.out.println("Current Variable Values: \n");
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
    public static void trigonometry() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("\n=== Trigonometric Calculations ===\n");
        System.out.println("NOTE - All input angles must be given as radians. ");
        System.out.println("pythagoras - Open Pythagoras Menu");
        System.out.println("calculate - Basic 1-step Trig Calculations");
        System.out.println("convert - Convert degrees to radians, and vice versa");
        System.out.println("back - Return to previous menu");
        System.out.println("Command: ");
        String command = scanner.nextLine().trim().toLowerCase();
        if (command.equalsIgnoreCase("pythagoras")) {
            pythagoras();
        }
        if (command.equalsIgnoreCase("calculate")) {
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
            switch (command) {
                case "arc":
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
                case "hyp":
                    System.out.println("sinh - Hyperbolic Sine");
                    System.out.println("cosh - Hyperbolic Cosine");
                    System.out.println("tanh - Hyperbolic Tangent");
                    System.out.println("csch - Hyperbolic Cosecant");
                    System.out.println("sech - Hyperbolic Secant");
                    System.out.println("coth - Hyperbolic Cotangent\n");
                    scanner.nextLine();
                    trigonometry();
                case "archyp":
                    System.out.println("arcsinh - Inverse Hyperbolic Sine");
                    System.out.println("arccosh - Inverse Hyperbolic Cosine");
                    System.out.println("arctanh - Inverse Hyperbolic Tangent");
                    System.out.println("arccsch - Inverse Hyperbolic Cosecant");
                    System.out.println("arcsech - Inverse Hyperbolic Secant");
                    System.out.println("arccoth - Inverse Hyperbolic Cotangent");
                    break;

                default:
                    System.out.println("Invalid command, please try again. ");
                    System.out.println("Press enter to continue...");
                    scanner.nextLine();
                    trigonometry();
            }
            if (command.equalsIgnoreCase("convert")) {
                System.out.println("\n== Conversion Menu ===\n");
                System.out.println("degree - Convert radians to degrees");
                System.out.println("radian - Convert degrees to radians");
                System.out.println("back - Return to previous menu");
                System.out.println("Command: ");
                command = scanner.nextLine().trim().toLowerCase();
                double input;
                switch (command) {
                    case "degree":
                        System.out.println("Input radians: ");
                        input = scanner.nextDouble();
                        double degrees = Math.toDegrees(input);
                        System.out.println("Output degrees: " + degrees);
                        trigonometry();
                    case "radian":
                        System.out.println("Input degrees: ");
                        input = scanner.nextDouble();
                        double radians = Math.toRadians(input);
                        System.out.println("Output radians: " + radians);
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
        }
        trigonometry();
    }

    public static void pythagoras() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("\n=== Pythagoras Calculations ===\n");
        System.out.println("standard - A^2 + B^2 = C^2");
        System.out.println("reverse - C^2 - A^2 = B^2");
        System.out.println("back - Return to previous menu");
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
            String equation = aValue + "^2 + " + bValue + "^2";
            calculationHistory.push(new HistoryEntry(equation, Double.toString(cValue)));
        }
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

    public static List<String> toPostfix(String infix) {
        //Shunting Yard Algorithm - Touch at own risk
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
                if (!operators.isEmpty() && operators.peek() == '(') {
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
                    case '^' -> stack.push(Math.pow(a, b));
                    case '%' -> stack.push(a%b);
                }
            }
            else {
                stack.push(Double.parseDouble(token));
            }
        }
        return stack.pop();
    }

    public static boolean isOperator(char c) {
        return c == '+' || c == '-' || c == '*' || c == '/' || c == '^' || c == '%';
    }

    public static int precedence(char op) {
        //Handles operator precedence within equation
        return switch (op) {
            case '+', '-' -> 1;
            case '*', '/', '%' -> 2;
            case '^' -> 3;
            default -> 0;
        };
    }

    public static double calculationEngine(String equation) {
        equation = equation.replaceAll("\\s", "");
        List<String> postfix = toPostfix(equation);
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
        return evaluatePostfix(postfix);
    }

    public static void calculate() {
        //Calculation menu frontend
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
        try {
            Thread.sleep(1500);
        }
        catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
        }
        calculate();
    }

    public static void algebra() {
        //Algebraic calculations
        Scanner scanner = new Scanner(System.in);
        System.out.println("=== Algebra Menu ===\n");
        System.out.println("quadratic - Solve quadratic");
        System.out.println("solvex - Solve for a value of X");
        System.out.print("Command: ");
        String command = scanner.nextLine().trim().toLowerCase();
        switch (command) {
            case "quadratic":
                System.out.println("Quadratics must be given in the form Ax^2 + Bx + C = 0");
                System.out.print("A Value: ");
                double aValue = scanner.nextDouble();
                System.out.print("B Value: ");
                double bValue = scanner.nextDouble();
                System.out.print("C Value: ");
                double cValue = scanner.nextDouble();
                double resultPos = ((-bValue + Math.sqrt(Math.pow(bValue, 2) - (4 * aValue * cValue))) / (2 * aValue));
                double resultNeg = ((-bValue - Math.sqrt(Math.pow(bValue, 2) - (4 * aValue * cValue))) / (2 * aValue));
                System.out.println("+Result: " + resultPos);
                System.out.println("-Result: " + resultNeg);
                calculationHistory.push(new HistoryEntry( aValue + "x^2 + " + bValue + "x + " + cValue, resultPos + ", " + resultNeg));
                algebra();
            case "solvex":
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
    public static void historyMenu() {
        //Handles calculation history
        Scanner scanner = new Scanner(System.in);
        System.out.println("=== History Menu ===\n");
        System.out.println("view - View this session's history");
        System.out.println("clear - Clear this session's history");
        System.out.println("back - Return to main menu");
        String command = scanner.nextLine().trim().toLowerCase();
        switch (command) {
            case "view":
                Iterator<HistoryEntry> iterator = calculationHistory.iterator();
                while (iterator.hasNext()) {
                    System.out.println(calculationHistory.peek());
                }
                try {
                    Thread.sleep(1500);
                }
                catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                }
                historyMenu();
            case "clear":
            case "back":
            default:
                System.out.println("Invalid command, please try again.");
                System.out.println("Press enter to continue...");
                scanner.nextLine();
                historyMenu();
        }
    }
    public static void menu() {
        //Main frontend menu
        Scanner scanner = new Scanner(System.in);
        OSIdentify();
        System.out.println("=== SolveX Main Menu ===\n");
        System.out.println("variable - Opens Variable Menu");
        System.out.println("calculate - Opens Calculation Engine");
        System.out.println("probabilities - Open Probability Engine");
        System.out.println("trigonometry - Opens Trigonometric Calculations");
        System.out.println("algebra - Opens Algebraic Calculations");
        System.out.println("settings - Opens Settings Menu");
        System.out.println("help - Opens Help Menu");
        System.out.println("exit - Exit the program");
        System.out.print("Command: ");
        String command = scanner.nextLine().trim().toLowerCase();
        switch (command) {
            case "variable": variables(); break;
            case "calculate": calculate(); break;
            //case "probabilities": probability(); break;
            case "algebra": algebra(); break;
            case "trigonometry": trigonometry(); break;
            case "settings": settings(); break;
            //case "help": help(); break;
            case "exit": exit(0); break;
            case "history": historyMenu(); break;
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
        //Main function, program entry point. DO NOT TOUCH
        menu();
    }
}