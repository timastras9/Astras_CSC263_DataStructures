package DataStructures;

import java.util.Arrays;
import java.util.NoSuchElementException;

/* =====================================================================
 *  Course   : CSC263 - Data Structures (Java 2)
 *  Project  : Chapter 16 Data Structures -- Stack
 *  Author   : Timothy Astras
 *  Date     : 2026-06-24
 *
 *  AI ASSISTANCE DISCLOSURE (academic honesty statement):
 *  ----------------------------------------------------------------
 *  This program was developed with AI assistance using Claude Code,
 *  an AI coding assistant from Anthropic. The assignment -- writing
 *  from-scratch Java implementations of the Chapter 16 data
 *  structures (Linked List, Array List, Stack, Queue, Hash Table)
 *  with test classes -- is the requirement for my Java 2 (CSC263)
 *  class. ALL original thoughts, ideas, design decisions, and
 *  innovation behind this work are my own (Timothy Astras); Claude
 *  Code was used purely as a programming assistant to help me
 *  implement MY ideas in Java. I directed every step, reviewed every
 *  line, compiled and ran the program, and verified the output. This
 *  disclosure is provided in compliance with the course's
 *  academic-integrity policy on AI tool use.
 * =====================================================================
 *
 *  WHAT THIS FILE IS
 *  -----------------
 *  A generic STACK built from scratch on top of a resizable array.
 *  A stack is a LIFO structure -- Last In, First Out. Think of a stack
 *  of plates: you add (push) to the top and remove (pop) from the top,
 *  so the most recently added item is the first one to leave.
 *
 *  Core operations (all O(1) amortized):
 *      push(x) -- put x on top
 *      pop()   -- remove and return the top item
 *      peek()  -- look at the top item without removing it
 * =====================================================================
 */
public class MyStack<T> {

    private static final int DEFAULT_CAPACITY = 10;

    // ---- FIELDS -----------------------------------------------------
    // We store items in an array. The "top" of the stack is the slot at
    // index (size - 1) -- i.e. the most recently pushed element.
    private Object[] elements;
    private int size;

    public MyStack() {
        elements = new Object[DEFAULT_CAPACITY];
        size = 0;
    }

    /* ----------------------------------------------------------------
     *  push: add an item to the TOP of the stack.
     * ---------------------------------------------------------------- */
    public void push(T value) {
        ensureCapacity(size + 1);
        elements[size] = value;
        size++;
    }

    /* ----------------------------------------------------------------
     *  pop: remove and return the TOP item. Throws if the stack is
     *  empty, because there is nothing to remove.
     * ---------------------------------------------------------------- */
    @SuppressWarnings("unchecked")
    public T pop() {
        if (isEmpty()) {
            throw new NoSuchElementException("Cannot pop from an empty stack");
        }
        T top = (T) elements[size - 1];
        elements[size - 1] = null; // free the reference for the GC
        size--;
        return top;
    }

    /* ----------------------------------------------------------------
     *  peek: return the TOP item WITHOUT removing it. Throws if empty.
     * ---------------------------------------------------------------- */
    @SuppressWarnings("unchecked")
    public T peek() {
        if (isEmpty()) {
            throw new NoSuchElementException("Cannot peek an empty stack");
        }
        return (T) elements[size - 1];
    }

    // Number of items on the stack.
    public int size() {
        return size;
    }

    // True when there are no items.
    public boolean isEmpty() {
        return size == 0;
    }

    // Remove all items.
    public void clear() {
        for (int i = 0; i < size; i++) {
            elements[i] = null;
        }
        size = 0;
    }

    /* ----------------------------------------------------------------
     *  toString: print from BOTTOM to TOP, marking the top, e.g.
     *  [a, b, c] <- top   (so 'c' is the next item pop() would return).
     * ---------------------------------------------------------------- */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < size; i++) {
            sb.append(elements[i]);
            if (i < size - 1) {
                sb.append(", ");
            }
        }
        sb.append("] <- top");
        return sb.toString();
    }

    // ---- PRIVATE HELPER ---------------------------------------------
    // Grow the backing array (doubling) when it runs out of room.
    private void ensureCapacity(int minCapacity) {
        if (minCapacity > elements.length) {
            int newCapacity = elements.length * 2;
            if (newCapacity < minCapacity) {
                newCapacity = minCapacity;
            }
            elements = Arrays.copyOf(elements, newCapacity);
        }
    }
}
