package ca.umanitoba.cs.kanand.model;

import ca.umanitoba.cs.kanand.test.TestResults;

/**
 * Test suite for LinkedListStack<T> implementation.
 * 
 * Tests domain behavior following COMP 2450 class methodology with professor's pattern:
 * - Uses TestResults to track pass/fail counts
 * - Try-catch blocks to verify exceptions are properly thrown
 * - Tests valid inputs and edge cases only
 * - NO assertion statements
 * 
 * Edge cases: empty stack, single element, sequence operations, LIFO order.
 */
public class LinkedListStackTest {
    private TestResults results = new TestResults();

    public static void main(String[] args) {
        LinkedListStackTest tests = new LinkedListStackTest();
        tests.runAllTests();
    }

    public void runAllTests() {
        System.out.println("\n╔" + "═".repeat(58) + "╗");
        System.out.println("║" + " ".repeat(16) + "LinkedListStack Test Suite" + " ".repeat(16) + "║");
        System.out.println("╚" + "═".repeat(58) + "╝\n");

        testEmptyStackIsEmpty();
        testEmptyStackSizeZero();
        testPushIncrementsSize();
        testLIFOOrder();
        testPeekDoesNotModifySize();
        testSingleElementPushPop();
        testMultipleElements();
        testMixedSequence();
        testPointStack();

        printSummary();
    }

    /**
     * Edge case: new stack should be empty
     */
    private void testEmptyStackIsEmpty() {
        try {
            LinkedListStack<Integer> stack = new LinkedListStack<>();
            if (stack.isEmpty()) {
                results.pass("Empty stack returns true for isEmpty()");
            } else {
                results.fail("Empty stack returns true for isEmpty()");
            }
        } catch (Exception e) {
            results.fail("Unexpected exception in testEmptyStackIsEmpty: " + e.getMessage());
        }
    }

    /**
     * Edge case: new stack should have size 0
     */
    private void testEmptyStackSizeZero() {
        try {
            LinkedListStack<String> stack = new LinkedListStack<>();
            if (stack.size() == 0) {
                results.pass("Empty stack has size == 0");
            } else {
                results.fail("Empty stack has size == 0");
            }
        } catch (Exception e) {
            results.fail("Unexpected exception in testEmptyStackSizeZero: " + e.getMessage());
        }
    }

    /**
     * General case: push should increment size
     */
    private void testPushIncrementsSize() {
        try {
            LinkedListStack<Integer> stack = new LinkedListStack<>();
            stack.push(1);
            if (stack.size() == 1) {
                results.pass("Push increments size");
            } else {
                results.fail("Push increments size");
            }
        } catch (Exception e) {
            results.fail("Unexpected exception in testPushIncrementsSize: " + e.getMessage());
        }
    }

    /**
     * General case: pop should return elements in LIFO order
     */
    private void testLIFOOrder() {
        try {
            LinkedListStack<Integer> stack = new LinkedListStack<>();
            stack.push(1);
            stack.push(2);
            stack.push(3);
            
            Integer third = stack.pop();
            Integer second = stack.pop();
            Integer first = stack.pop();
            
            if (third == 3 && second == 2 && first == 1) {
                results.pass("Stack maintains LIFO order on pop");
            } else {
                results.fail("Stack maintains LIFO order on pop");
            }
        } catch (Exception e) {
            results.fail("Unexpected exception in testLIFOOrder: " + e.getMessage());
        }
    }

    /**
     * General case: peek should not modify size
     */
    private void testPeekDoesNotModifySize() {
        try {
            LinkedListStack<String> stack = new LinkedListStack<>();
            stack.push("hello");
            int sizeBefore = stack.size();
            
            String peeked = stack.peek();
            int sizeAfter = stack.size();
            
            if (sizeBefore == sizeAfter && peeked.equals("hello")) {
                results.pass("Peek does not modify size");
            } else {
                results.fail("Peek does not modify size");
            }
        } catch (Exception e) {
            results.fail("Unexpected exception in testPeekDoesNotModifySize: " + e.getMessage());
        }
    }

    /**
     * Edge case: single element operations
     */
    private void testSingleElementPushPop() {
        try {
            LinkedListStack<Integer> stack = new LinkedListStack<>();
            stack.push(42);
            
            if (!stack.isEmpty() && stack.size() == 1) {
                Integer value = stack.pop();
                if (value == 42 && stack.isEmpty()) {
                    results.pass("Single element push/pop works correctly");
                } else {
                    results.fail("Single element push/pop works correctly");
                }
            } else {
                results.fail("Single element push/pop works correctly");
            }
        } catch (Exception e) {
            results.fail("Unexpected exception in testSingleElementPushPop: " + e.getMessage());
        }
    }

    /**
     * General case: multiple elements should maintain size
     */
    private void testMultipleElements() {
        try {
            LinkedListStack<Integer> stack = new LinkedListStack<>();
            for (int i = 0; i < 5; i++) {
                stack.push(i);
            }
            
            if (stack.size() == 5 && !stack.isEmpty()) {
                results.pass("Multiple elements maintain correct size");
            } else {
                results.fail("Multiple elements maintain correct size");
            }
        } catch (Exception e) {
            results.fail("Unexpected exception in testMultipleElements: " + e.getMessage());
        }
    }

    /**
     * Edge case: mixed push/pop/peek sequence
     */
    private void testMixedSequence() {
        try {
            LinkedListStack<Integer> stack = new LinkedListStack<>();
            stack.push(10);
            stack.push(20);
            
            int first = stack.pop();
            stack.push(30);
            int third = stack.pop();
            
            if (first == 20 && third == 30 && stack.size() == 1) {
                results.pass("Mixed push/pop sequences work correctly");
            } else {
                results.fail("Mixed push/pop sequences work correctly");
            }
        } catch (Exception e) {
            results.fail("Unexpected exception in testMixedSequence: " + e.getMessage());
        }
    }

    /**
     * General case: stack should work with domain model objects
     */
    private void testPointStack() {
        try {
            LinkedListStack<Point> stack = new LinkedListStack<>();
            Point p1 = new Point(1, 2);
            Point p2 = new Point(3, 4);
            
            stack.push(p1);
            stack.push(p2);
            
            if (stack.pop().equals(p2) && stack.pop().equals(p1)) {
                results.pass("Stack works with domain model objects (Point)");
            } else {
                results.fail("Stack works with domain model objects (Point)");
            }
        } catch (Exception e) {
            results.fail("Unexpected exception in testPointStack: " + e.getMessage());
        }
    }

    private void printSummary() {
        System.out.println("\n" + "─".repeat(60));
        System.out.printf("LinkedListStack: %d passed, %d failed out of %d tests%n", 
            results.successes(), results.failures(), results.totalTests());
        System.out.println("─".repeat(60) + "\n");
    }
}
