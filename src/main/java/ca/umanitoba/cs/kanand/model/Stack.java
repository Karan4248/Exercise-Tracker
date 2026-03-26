package ca.umanitoba.cs.kanand.model;

/**
 * Generic Stack Abstract Data Type (ADT).
 * 
 * A stack is a Last-In-First-Out (LIFO) data structure where elements are added
 * and removed from the same end (the "top" of the stack).
 * 
 * This interface defines the contract for stack implementations.
 * Implementations must include appropriate preconditions, postconditions, and
 * class invariants as specified in the Javadoc.
 * 
 * @param <T> the type of elements in this stack
 * 
 * Class Invariants:
 *   - size() >= 0
 *   - isEmpty() is true if and only if size() == 0
 *   - The stack maintains LIFO ordering for push and pop operations
 */
public interface Stack<T> {
    /**
     * Pushes an element onto the top of the stack.
     * 
     * Preconditions:
     *   - item != null
     * 
     * Postconditions:
     *   - The new size is the old size + 1
     *   - The item is now at the top of the stack (peek() returns item)
     *   - All previous items remain in the stack in their original order
     * 
     * @param item the element to push onto the stack
     * @throws NullPointerException if item is null
     */
    void push(T item);

    /**
     * Removes and returns the element at the top of the stack.
     * 
     * Preconditions:
     *   - !isEmpty() (the stack must not be empty)
     * 
     * Postconditions:
     *   - The new size is the old size - 1
     *   - The returned item is the item that was most recently pushed
     *   - All other items remain in the stack in their original order
     * 
     * @return the element at the top of the stack
     * @throws RuntimeException if the stack is empty (precondition violation)
     */
    T pop();

    /**
     * Returns the element at the top of the stack without removing it.
     * 
     * Preconditions:
     *   - !isEmpty() (the stack must not be empty)
     * 
     * Postconditions:
     *   - The size is unchanged
     *   - The returned item is the item that was most recently pushed
     *   - No elements are removed from the stack
     * 
     * @return the element at the top of the stack
     * @throws RuntimeException if the stack is empty (precondition violation)
     */
    T peek();

    /**
     * Returns the number of elements currently on the stack.
     * 
     * Preconditions:
     *   - None
     * 
     * Postconditions:
     *   - The returned value is >= 0
     *   - The size is unchanged by this operation
     * 
     * @return the number of elements on the stack
     */
    int size();

    /**
     * Checks if the stack is empty.
     * 
     * Preconditions:
     *   - None
     * 
     * Postconditions:
     *   - Returns true if and only if size() == 0
     *   - The stack is unchanged by this operation
     * 
     * @return true if the stack is empty, false otherwise
     */
    boolean isEmpty();
}
