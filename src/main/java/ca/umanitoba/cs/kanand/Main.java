package ca.umanitoba.cs.kanand;

import ca.umanitoba.cs.kanand.model.ExerciseTrackerREPL;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ExerciseTrackerREPL repl = new ExerciseTrackerREPL(scanner);
        repl.start();
        scanner.close();
    }
}

