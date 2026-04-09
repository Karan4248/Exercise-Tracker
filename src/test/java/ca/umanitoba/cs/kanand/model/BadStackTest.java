package ca.umanitoba.cs.kanand.model;

import ca.umanitoba.cs.comp2450.stack.Stack;
import ca.umanitoba.cs.comp2450.stack.impl.*;
import ca.umanitoba.cs.kanand.test.TestResults;
import ca.umanitoba.cs.kanand.test.TestSuite;

public class BadStackTest implements TestSuite {
    private final TestResults results = new TestResults();

    @Override
    public String name() {
        return "Tests for BadStack Implementations";
    }

    @Override
    public TestResults runTests() {
        System.out.println("--- Testing BadStack1 ---");
        runStackTests("BadStack1");

        System.out.println("\n--- Testing BadStack2 ---");
        runStackTests("BadStack2");

        System.out.println("\n--- Testing BadStack3 ---");
        runStackTests("BadStack3");

        System.out.println("\n--- Testing BadStack4 ---");
        runStackTests("BadStack4");

        System.out.println("\n--- Testing BadStack5 ---");
        runStackTests("BadStack5");

        return results;
    }

    /**
     * Runs all stack tests on a given implementation.
     * Each test creates its own fresh stack instance.
     *
     * @param name label for the implementation being tested
     */
    private void runStackTests(String name) {
        testEmptyStackIsEmpty(name);
        testEmptyStackSizeZero(name);
        testPushSingleElement(name);
        testPushIncrementsSize(name);
        testPeekReturnsTopElement(name);
        testPeekDoesNotChangeSize(name);
        testPopReturnsTopElement(name);
        testPopDecrementsSize(name);
        testLIFOOrder(name);
        testSingleElementPushPop(name);
        testMixedPushPopSequence(name);
        testPopToEmpty(name);
        testPushAfterPopToEmpty(name);
        testPopOnEmptyThrows(name);
        testPeekOnEmptyThrows(name);
    }

    /**
     * Edge case: new stack should be empty.
     */
    private void testEmptyStackIsEmpty(String name) {
        try {
            Stack<Integer> stack = createStack(name);
            if (stack.isEmpty()) {
                results.pass(name + ": new stack isEmpty() returns true");
            } else {
                results.fail(name + ": new stack isEmpty() returns true");
            }
        } catch (Exception e) {
            results.fail(name + ": new stack isEmpty() threw " + e.getClass().getSimpleName() + ": " + e.getMessage());
        }
    }

    /**
     * Edge case: new stack should have size 0.
     */
    private void testEmptyStackSizeZero(String name) {
        try {
            Stack<Integer> stack = createStack(name);
            if (stack.size() == 0) {
                results.pass(name + ": new stack size() returns 0");
            } else {
                results.fail(name + ": new stack size() returns 0 (got " + stack.size() + ")");
            }
        } catch (Exception e) {
            results.fail(name + ": new stack size() threw " + e.getClass().getSimpleName() + ": " + e.getMessage());
        }
    }

    /**
     * General case: push one element, stack should not be empty.
     */
    private void testPushSingleElement(String name) {
        try {
            Stack<String> stack = createStringStack(name);
            stack.push("hello");
            if (!stack.isEmpty()) {
                results.pass(name + ": push makes isEmpty() return false");
            } else {
                results.fail(name + ": push makes isEmpty() return false");
            }
        } catch (Exception e) {
            results.fail(name + ": push single element threw " + e.getClass().getSimpleName() + ": " + e.getMessage());
        }
    }

    /**
     * General case: push should increment size by 1.
     */
    private void testPushIncrementsSize(String name) {
        try {
            Stack<Integer> stack = createStack(name);
            stack.push(10);
            if (stack.size() == 1) {
                stack.push(20);
                if (stack.size() == 2) {
                    stack.push(30);
                    if (stack.size() == 3) {
                        results.pass(name + ": push increments size correctly");
                    } else {
                        results.fail(name + ": push increments size (expected 3, got " + stack.size() + ")");
                    }
                } else {
                    results.fail(name + ": push increments size (expected 2, got " + stack.size() + ")");
                }
            } else {
                results.fail(name + ": push increments size (expected 1, got " + stack.size() + ")");
            }
        } catch (Exception e) {
            results.fail(name + ": push increments size threw " + e.getClass().getSimpleName() + ": " + e.getMessage());
        }
    }

    /**
     * General case: peek should return the most recently pushed element.
     */
    private void testPeekReturnsTopElement(String name) {
        try {
            Stack<Integer> stack = createStack(name);
            stack.push(100);
            stack.push(200);

            Integer peeked = stack.peek();
            if (peeked != null && peeked == 200) {
                results.pass(name + ": peek() returns top element");
            } else {
                results.fail(name + ": peek() returns top element (expected 200, got " + peeked + ")");
            }
        } catch (Exception e) {
            results.fail(name + ": peek() threw " + e.getClass().getSimpleName() + ": " + e.getMessage());
        }
    }

    /**
     * General case: peek should not change the size.
     */
    private void testPeekDoesNotChangeSize(String name) {
        try {
            Stack<Integer> stack = createStack(name);
            stack.push(1);
            stack.push(2);
            int sizeBefore = stack.size();
            stack.peek();
            int sizeAfter = stack.size();

            if (sizeBefore == sizeAfter) {
                results.pass(name + ": peek() does not change size");
            } else {
                results.fail(name + ": peek() does not change size (before=" + sizeBefore + ", after=" + sizeAfter + ")");
            }
        } catch (Exception e) {
            results.fail(name + ": peek() size check threw " + e.getClass().getSimpleName() + ": " + e.getMessage());
        }
    }

    /**
     * General case: pop should return the most recently pushed element.
     */
    private void testPopReturnsTopElement(String name) {
        try {
            Stack<Integer> stack = createStack(name);
            stack.push(10);
            stack.push(20);

            Integer popped = stack.pop();
            if (popped != null && popped == 20) {
                results.pass(name + ": pop() returns top element");
            } else {
                results.fail(name + ": pop() returns top element (expected 20, got " + popped + ")");
            }
        } catch (Exception e) {
            results.fail(name + ": pop() threw " + e.getClass().getSimpleName() + ": " + e.getMessage());
        }
    }

    /**
     * General case: pop should decrement size by 1.
     */
    private void testPopDecrementsSize(String name) {
        try {
            Stack<Integer> stack = createStack(name);
            stack.push(1);
            stack.push(2);
            stack.push(3);
            stack.pop();

            if (stack.size() == 2) {
                stack.pop();
                if (stack.size() == 1) {
                    results.pass(name + ": pop() decrements size correctly");
                } else {
                    results.fail(name + ": pop() decrements size (expected 1, got " + stack.size() + ")");
                }
            } else {
                results.fail(name + ": pop() decrements size (expected 2, got " + stack.size() + ")");
            }
        } catch (Exception e) {
            results.fail(name + ": pop() decrement threw " + e.getClass().getSimpleName() + ": " + e.getMessage());
        }
    }

    /**
     * General case: stack must maintain LIFO ordering.
     */
    private void testLIFOOrder(String name) {
        try {
            Stack<Integer> stack = createStack(name);
            stack.push(1);
            stack.push(2);
            stack.push(3);

            Integer third = stack.pop();
            Integer second = stack.pop();
            Integer first = stack.pop();

            if (third == 3 && second == 2 && first == 1) {
                results.pass(name + ": stack maintains LIFO order");
            } else {
                results.fail(name + ": stack maintains LIFO order (got " + third + ", " + second + ", " + first + ")");
            }
        } catch (Exception e) {
            results.fail(name + ": LIFO order threw " + e.getClass().getSimpleName() + ": " + e.getMessage());
        }
    }

    /**
     * Edge case: push and pop a single element, stack should be empty again.
     */
    private void testSingleElementPushPop(String name) {
        try {
            Stack<Integer> stack = createStack(name);
            stack.push(42);

            Integer value = stack.pop();
            if (value == 42 && stack.isEmpty() && stack.size() == 0) {
                results.pass(name + ": single push/pop returns correct value and empties stack");
            } else {
                results.fail(name + ": single push/pop (value=" + value + ", isEmpty=" + stack.isEmpty() + ", size=" + stack.size() + ")");
            }
        } catch (Exception e) {
            results.fail(name + ": single push/pop threw " + e.getClass().getSimpleName() + ": " + e.getMessage());
        }
    }

    /**
     * Edge case: mixed push/pop sequence preserves correct state.
     */
    private void testMixedPushPopSequence(String name) {
        try {
            Stack<Integer> stack = createStack(name);
            stack.push(10);
            stack.push(20);
            Integer first = stack.pop();  // should be 20
            stack.push(30);
            Integer second = stack.peek(); // should be 30
            Integer third = stack.pop();  // should be 30

            if (first == 20 && second == 30 && third == 30 && stack.size() == 1) {
                results.pass(name + ": mixed push/pop/peek sequence correct");
            } else {
                results.fail(name + ": mixed sequence (pop1=" + first + ", peek=" + second + ", pop2=" + third + ", size=" + stack.size() + ")");
            }
        } catch (Exception e) {
            results.fail(name + ": mixed sequence threw " + e.getClass().getSimpleName() + ": " + e.getMessage());
        }
    }

    /**
     * Edge case: pop all elements until empty.
     */
    private void testPopToEmpty(String name) {
        try {
            Stack<Integer> stack = createStack(name);
            stack.push(1);
            stack.push(2);
            stack.push(3);
            stack.pop();
            stack.pop();
            stack.pop();

            if (stack.isEmpty() && stack.size() == 0) {
                results.pass(name + ": pop to empty works correctly");
            } else {
                results.fail(name + ": pop to empty (isEmpty=" + stack.isEmpty() + ", size=" + stack.size() + ")");
            }
        } catch (Exception e) {
            results.fail(name + ": pop to empty threw " + e.getClass().getSimpleName() + ": " + e.getMessage());
        }
    }

    /**
     * Edge case: push after fully emptying the stack.
     */
    private void testPushAfterPopToEmpty(String name) {
        try {
            Stack<Integer> stack = createStack(name);
            stack.push(1);
            stack.pop();
            stack.push(99);

            if (stack.size() == 1 && stack.peek() == 99 && !stack.isEmpty()) {
                results.pass(name + ": push after emptying works correctly");
            } else {
                results.fail(name + ": push after emptying (size=" + stack.size() + ", peek=" + stack.peek() + ")");
            }
        } catch (Exception e) {
            results.fail(name + ": push after emptying threw " + e.getClass().getSimpleName() + ": " + e.getMessage());
        }
    }

    /**
     * Edge case: pop on empty stack should throw an exception.
     */
    private void testPopOnEmptyThrows(String name) {
        try {
            Stack<Integer> stack = createStack(name);
            try {
                stack.pop();
                results.fail(name + ": pop on empty should throw exception");
            } catch (Exception e) {
                results.pass(name + ": pop on empty throws exception");
            }
        } catch (Exception e) {
            results.fail(name + ": pop on empty outer threw " + e.getClass().getSimpleName());
        }
    }

    /**
     * Edge case: peek on empty stack should throw an exception.
     */
    private void testPeekOnEmptyThrows(String name) {
        try {
            Stack<Integer> stack = createStack(name);
            try {
                stack.peek();
                results.fail(name + ": peek on empty should throw exception");
            } catch (Exception e) {
                results.pass(name + ": peek on empty throws exception");
            }
        } catch (Exception e) {
            results.fail(name + ": peek on empty outer threw " + e.getClass().getSimpleName());
        }
    }

    /**
     * Factory method to create the appropriate BadStack implementation.
     */
    @SuppressWarnings("unchecked")
    private Stack<Integer> createStack(String name) {
        return switch (name) {
            case "BadStack1" -> new BadStack1<>();
            case "BadStack2" -> new BadStack2<>();
            case "BadStack3" -> new BadStack3<>();
            case "BadStack4" -> new BadStack4<>();
            case "BadStack5" -> new BadStack5<>();
            default -> throw new IllegalArgumentException("Unknown stack: " + name);
        };
    }

    /**
     * Factory method to create a String-typed BadStack implementation.
     */
    @SuppressWarnings("unchecked")
    private Stack<String> createStringStack(String name) {
        return switch (name) {
            case "BadStack1" -> new BadStack1<>();
            case "BadStack2" -> new BadStack2<>();
            case "BadStack3" -> new BadStack3<>();
            case "BadStack4" -> new BadStack4<>();
            case "BadStack5" -> new BadStack5<>();
            default -> throw new IllegalArgumentException("Unknown stack: " + name);
        };
    }

}
