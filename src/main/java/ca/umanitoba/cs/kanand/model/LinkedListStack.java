package ca.umanitoba.cs.kanand.model;

import com.google.common.base.Preconditions;

/**
 * A generic Stack implementation using a linked list data structure.
 * 
 * This implementation provides O(1) time complexity for push, pop, peek operations.
 * It uses a private inner StackNode class to maintain the linked list structure.
 * 
 * Class Invariants:
 *   - head == null if and only if the stack is empty (isEmpty() == true)
 *   - head == null if and only if size == 0
 *   - All nodes are properly linked together with no cycles
 *   - size >= 0 and equals the count of all nodes reachable from head
 *   - If size > 0, head != null and head.data != null
 *   - All data elements in nodes are non-null
 * 
 * @param <T> the type of elements in this stack
 */
public class LinkedListStack<T> implements Stack<T> {
    /**
     * Private inner class representing a single node in the linked list.
     * 
     * Node Invariants:
     *   - data != null (nodes can only contain non-null data)
     *   - next can be null (for the top of the stack) or reference another StackNode
     */
    private static class StackNode<T> {
        final T data;
        StackNode<T> next;

        /**
         * Creates a new stack node with the given data.
         * 
         * Precondition:
         *   - data != null
         * 
         * @param data the data to store in this node
         * @throws NullPointerException if data is null
         */
        StackNode(T data) {
            this.data = Preconditions.checkNotNull(data, "Precondition failed: node data cannot be null");
            this.next = null;
            Preconditions.checkState(checkInvariant(), "Node invariant violated");
        }

        /**
         * Checks the node's class invariant.
         * 
         * @return true if the invariant is satisfied, false otherwise
         */
        private boolean checkInvariant() {
            return data != null;
        }
    }

    private StackNode<T> head;
    private int size;

    /**
     * Creates an empty stack.
     * 
     * Postcondition:
     *   - The stack is empty (head == null, size == 0)
     */
    public LinkedListStack() {
        this.head = null;
        this.size = 0;
        Preconditions.checkState(checkInvariant(), "Invariant violated at construction");
    }

    /**
     * Pushes an element onto the top of the stack.
     * 
     * Precondition:
     *   - item != null
     * 
     * Postcondition:
     *   - size has increased by 1
     *   - The new head of the stack is a node containing item
     *   - peek() returns item
     *   - All previously pushed items remain on the stack
     * 
     * @param item the element to push onto the stack
     * @throws NullPointerException if item is null
     * @throws RuntimeException if invariant is violated
     */
    @Override
    public void push(T item) {
        Preconditions.checkNotNull(item, "Precondition failed: item cannot be null");

        StackNode<T> newNode = new StackNode<>(item);
        newNode.next = head;
        head = newNode;
        size++;

        Preconditions.checkState(checkInvariant(), "Invariant violated after push");
        Preconditions.checkState(size > 0, "Postcondition failed: size should be > 0 after push");
        Preconditions.checkState(head != null, "Postcondition failed: head should not be null after push");
    }

    /**
     * Removes and returns the element at the top of the stack.
     * 
     * Precondition:
     *   - !isEmpty() (the stack must not be empty)
     * 
     * Postcondition:
     *   - size has decreased by 1
     *   - The returned element is the most recently pushed item that was still on the stack
     *   - The new head of the stack is the node that was previously below the popped node
     * 
     * @return the element at the top of the stack
     * @throws RuntimeException if the stack is empty (precondition violation)
     */
    @Override
    public T pop() {
        Preconditions.checkState(!isEmpty(), "Precondition failed: cannot pop from an empty stack");

        T data = head.data;
        head = head.next;
        size--;

        Preconditions.checkState(checkInvariant(), "Invariant violated after pop");
        Preconditions.checkState(size >= 0, "Postcondition failed: size should be >= 0 after pop");
        Preconditions.checkState((size == 0 && head == null) || (size > 0 && head != null), 
            "Postcondition failed: head state does not match size");

        return data;
    }

    /**
     * Returns the element at the top of the stack without removing it.
     * 
     * Precondition:
     *   - !isEmpty() (the stack must not be empty)
     * 
     * Postcondition:
     *   - size is unchanged
     *   - The returned element is the most recently pushed item still on the stack
     *   - The stack structure is unchanged
     * 
     * @return the element at the top of the stack
     * @throws RuntimeException if the stack is empty (precondition violation)
     */
    @Override
    public T peek() {
        Preconditions.checkState(!isEmpty(), "Precondition failed: cannot peek at an empty stack");

        T data = head.data;

        Preconditions.checkState(checkInvariant(), "Invariant violated after peek");
        Preconditions.checkState(size > 0, "Postcondition failed: size should not change");

        return data;
    }

    /**
     * Returns the number of elements currently on the stack.
     * 
     * Postcondition:
     *   - The returned value is >= 0
     *   - size is unchanged
     *   - The result matches the count of nodes reachable from head
     * 
     * @return the number of elements on the stack
     */
    @Override
    public int size() {
        Preconditions.checkState(checkInvariant(), "Invariant violated before size()");
        return size;
    }

    /**
     * Checks if the stack is empty.
     * 
     * Postcondition:
     *   - Returns true if and only if size() == 0
     *   - The result equals (head == null)
     *   - size is unchanged
     * 
     * @return true if the stack is empty, false otherwise
     */
    @Override
    public boolean isEmpty() {
        Preconditions.checkState(checkInvariant(), "Invariant violated before isEmpty()");
        return size == 0 && head == null;
    }

    /**
     * Checks the class invariant.
     * 
     * @return true if all invariant conditions are satisfied, false otherwise
     */
    private boolean checkInvariant() {
        // If size is 0, head must be null
        if (size == 0 && head != null) {
            return false;
        }
        // If size > 0, head must not be null
        if (size > 0 && head == null) {
            return false;
        }
        // Size must be non-negative
        if (size < 0) {
            return false;
        }
        // Verify that size matches the actual count of nodes
        int nodeCount = 0;
        StackNode<T> current = head;
        while (current != null) {
            if (current.data == null) {
                return false;
            }
            nodeCount++;
            current = current.next;
        }
        return nodeCount == size;
    }
}
