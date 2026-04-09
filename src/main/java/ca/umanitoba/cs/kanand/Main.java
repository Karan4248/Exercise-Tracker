package ca.umanitoba.cs.kanand;
import ca.umanitoba.cs.kanand.persistence.ExerciseTrackerPersistence;
import ca.umanitoba.cs.kanand.persistence.json.ExerciseTrackerPersistenceJson;
import ca.umanitoba.cs.kanand.ui.ExerciseTrackerUI;
import java.nio.file.Path;
import java.util.Scanner;

public class Main {
    /**
     * Entry point for the Exercise Tracker application.
     * Initializes persistence, UI with a scanner and starts the interactive loop.
     *
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args) {
        Path dataFile = Path.of("exercise-tracker-data.json");
        ExerciseTrackerPersistence persistence = new ExerciseTrackerPersistenceJson(dataFile);

        Scanner scanner = new Scanner(System.in);
        ExerciseTrackerUI ui = new ExerciseTrackerUI(scanner, persistence);
        ui.start();
        scanner.close();
    }
}

