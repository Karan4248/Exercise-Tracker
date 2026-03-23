package ca.umanitoba.cs.kanand.model;

import java.util.Scanner;

public class Main {
    /**
     * Entry point for the Exercise Tracker application.
     * Initializes the REPL with a scanner and starts the interactive loop.
     *
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ExerciseTrackerREPL repl = new ExerciseTrackerREPL(scanner);
        repl.start();
        scanner.close();
    }
}

