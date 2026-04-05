package ca.umanitoba.cs.kanand;
import ca.umanitoba.cs.kanand.ui.ExerciseTrackerUI;
import java.util.Scanner;

public class Main {
    /**
     * Entry point for the Exercise Tracker application.
     * Initializes the UI with a scanner and starts the interactive loop.
     *
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ExerciseTrackerUI ui = new ExerciseTrackerUI(scanner);
        ui.start();
        scanner.close();
    }
}

