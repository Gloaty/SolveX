package com.gloatyuk.solvex;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.Scanner;
import java.math.RoundingMode;

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
    }
    public static BigDecimal setPrecision(BigDecimal num, int precision) {
        return num.setScale(precision, RoundingMode.HALF_UP);
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
        }
        else {
            System.out.println("Invalid Command, please try again");
            scanner.nextLine();
        }
    }
    public static void main(String[] args) {
        System.out.println("Hello World!");
        OSIdentify();
        System.exit(1);
    }
}