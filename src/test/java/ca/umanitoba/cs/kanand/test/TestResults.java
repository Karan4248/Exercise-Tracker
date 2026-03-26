package ca.umanitoba.cs.kanand.test;

/**
 * Communicates the number of successes and failures between a test suite and a test runner.
 */
public class TestResults {
    private int totalTests = 0;
    private int successes = 0;
    private int failures = 0;

    public int totalTests() {
        return this.totalTests;
    }

    public int successes() {
        return this.successes;
    }

    public int failures() {
        return this.failures;
    }

    public void pass(String msg) {
        System.out.println("✓ PASS: " + msg);
        successes++;
        totalTests++;
    }

    public void fail(String msg) {
        System.out.println("✗ FAIL: " + msg);
        failures++;
        totalTests++;
    }
}
