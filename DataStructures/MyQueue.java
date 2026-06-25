package DataStructures;

import java.util.NoSuchElementException;

/* =====================================================================
 *  Course   : CSC263 - Data Structures (Java 2)
 *  Project  : Chapter 16 Data Structures -- Queue
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
 *  class. I (Timothy Astras) directed the work, reviewed every line,
 *  compiled and ran the program, and verified the output. This
 *  disclosure is provided in compliance with the course's
 *  academic-integrity policy on AI tool use.
 * =====================================================================
 *
 *  WHAT THIS FILE IS
 *  -----------------
 *  A generic QUEUE built from scratch using a CIRCULAR ARRAY.
 *  A queue is a FIFO structure -- First In, First Out. Think of a line
 *  at a store: people join at the BACK and are served from the FRONT,
 *  so the first to arrive is the first to leave.
 *
 *  Why "circular"? We keep two indexes, 'head' (front) and 'tail'
 *  (next free slot at the back). As items are added and removed, these
 *  indexes march forward and WRAP AROUND to the start of the array
 *  using modulo (%). This lets us reuse freed slots at the front
 *  instead of shifting every element -- so enqueue and dequeue are O(1).
 *
 *  Core operations:
 *      enqueue(x) -- add x to the BACK
 *      dequeue()  -- remove and return the item at the FRONT
 *      peek()     -- look at the FRONT item without removing it
 * =====================================================================
 */
public class MyQueue<T> {

    private static final int DEFAULT_CAPACITY = 10;

    // ---- FIELDS -----------------------------------------------------
    private Object[] elements; // circular backing store
    private int head;          // index of the front item
    private int tail;          // index of the next free slot at the back
    private int size;          // how many items are currently in the queue

    public MyQueue() {
        elements = new Object[DEFAULT_CAPACITY];
        head = 0;
        tail = 0;
        size = 0;
    }

    /* ----------------------------------------------------------------
     *  enqueue: add an item to the BACK of the queue. If the array is
     *  full we grow it first so there is always room.
     * ---------------------------------------------------------------- */
    public void enqueue(T value) {
        if (size == elements.length) {
            grow();
        }
        elements[tail] = value;
        // Advance tail, wrapping back to 0 if we ran off the end.
        tail = (tail + 1) % elements.length;
        size++;
    }

    /* ----------------------------------------------------------------
     *  dequeue: remove and return the item at the FRONT. Throws if the
     *  queue is empty.
     * ---------------------------------------------------------------- */
    @SuppressWarnings("unchecked")
    public T dequeue() {
        if (isEmpty()) {
            throw new NoSuchElementException("Cannot dequeue from an empty queue");
        }
        T front = (T) elements[head];
        elements[head] = null; // release the reference for the GC
        // Advance head, wrapping around if needed.
        head = (head + 1) % elements.length;
        size--;
        return front;
    }

    /* ----------------------------------------------------------------
     *  peek: return the FRONT item WITHOUT removing it. Throws if empty.
     * ---------------------------------------------------------------- */
    @SuppressWarnings("unchecked")
    public T peek() {
        if (isEmpty()) {
            throw new NoSuchElementException("Cannot peek an empty queue");
        }
        return (T) elements[head];
    }

    // Number of items currently in the queue.
    public int size() {
        return size;
    }

    // True when the queue holds no items.
    public boolean isEmpty() {
        return size == 0;
    }

    // Reset the queue to empty.
    public void clear() {
        for (int i = 0; i < elements.length; i++) {
            elements[i] = null;
        }
        head = 0;
        tail = 0;
        size = 0;
    }

    /* ----------------------------------------------------------------
     *  toString: print from FRONT to BACK, e.g. front -> [a, b, c]
     *  ('a' is the next item dequeue() would return).
     * ---------------------------------------------------------------- */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("front -> [");
        for (int i = 0; i < size; i++) {
            // Read in logical order starting at head, wrapping around.
            int idx = (head + i) % elements.length;
            sb.append(elements[idx]);
            if (i < size - 1) {
                sb.append(", ");
            }
        }
        sb.append("]");
        return sb.toString();
    }

    // ---- PRIVATE HELPER ---------------------------------------------
    // Double the capacity and copy items into the NEW array in clean
    // logical order (front at index 0). This "unwraps" the circle.
    private void grow() {
        Object[] bigger = new Object[elements.length * 2];
        for (int i = 0; i < size; i++) {
            bigger[i] = elements[(head + i) % elements.length];
        }
        elements = bigger;
        head = 0;
        tail = size; // after unwrapping, the back sits right after the items
    }
}
