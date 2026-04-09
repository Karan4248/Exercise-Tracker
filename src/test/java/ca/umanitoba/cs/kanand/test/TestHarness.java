package ca.umanitoba.cs.kanand.test;

import ca.umanitoba.cs.kanand.logic.ExerciseTrackerLogicTest;
import ca.umanitoba.cs.kanand.model.*;

public class TestHarness {
    public static void main(String[] args) {
        System.out.println("Test Harness\n---------------");

        TestSuite[] suites = {
                new UserTest(),
                new GridTest(),
                new LinkedListStackTest(),
                new PathFinderTest(),
                new BadStackTest(),
                new ExerciseTrackerLogicTest()
        };

        boolean failures = false;

        for (var suite : suites) {
            System.out.println(suite.name() + "\n--------");
            TestResults results = suite.runTests();

            System.out.printf("Suite tests: %d\n", results.totalTests());
            System.out.printf("\tSuccesses: %d\n", results.successes());
            System.out.printf("\tFailures: %d\n", results.failures());

            if (results.failures() > 0) {
                System.out.println("There were test failures.");
            } else {
                System.out.println("All tests passed!");
            }

            System.out.println("-------------");
            failures = failures || results.failures() > 0;
        }

        System.out.println("Overall Results ------------");
        if (failures) {
            System.out.println("There were test failures.");
        } else {
            System.out.println("All tests passed!");
        }
    }
}
