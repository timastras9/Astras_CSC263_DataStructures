package DataStructures;

import java.util.Arrays;

/* =====================================================================
 *  Course   : CSC263 - Data Structures (Java 2)
 *  Project  : Chapter 16 Data Structures -- Array List
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
 *  A generic, RESIZABLE ARRAY LIST built from scratch (we do NOT use
 *  java.util.ArrayList). Internally it stores elements in a plain
 *  Object[] array. The trick is that a plain array has a FIXED length,
 *  so when it fills up we allocate a bigger array and copy everything
 *  over -- this is called "growing" the backing store.
 *
 *  Trade-offs vs. a linked list:
 *    + get(index)/set(index) are O(1) -- direct array indexing.
 *    - insert/remove in the MIDDLE is O(n) -- elements must shift.
 * =====================================================================
 */
public class MyArrayList<T> {

    // Starting capacity of the backing array before any growth.
    private static final int DEFAULT_CAPACITY = 10;

    // ---- FIELDS -----------------------------------------------------
    private Object[] elements; // backing store (may have empty trailing slots)
    private int size;          // number of slots actually in use

    // Create an empty list with the default starting capacity.
    public MyArrayList() {
        elements = new Object[DEFAULT_CAPACITY];
        size = 0;
    }

    /* ----------------------------------------------------------------
     *  add(value): append to the end. Amortized O(1) -- occasionally we
     *  pay to grow the array, but on average each add is constant time.
     * ---------------------------------------------------------------- */
    public void add(T value) {
        ensureCapacity(size + 1); // make sure there is room for one more
        elements[size] = value;
        size++;
    }

    /* ----------------------------------------------------------------
     *  add(index, value): insert at position index, shifting the
     *  elements at and after index one slot to the right. O(n).
     * ---------------------------------------------------------------- */
    public void add(int index, T value) {
        checkIndexForAdd(index);
        ensureCapacity(size + 1);
        // Shift right to open a gap at 'index'. Copy from the back so we
        // do not overwrite values we still need to move.
        for (int i = size; i > index; i--) {
            elements[i] = elements[i - 1];
        }
        elements[index] = value;
        size++;
    }

    /* ----------------------------------------------------------------
     *  get(index): return the element at index. O(1).
     * ---------------------------------------------------------------- */
    @SuppressWarnings("unchecked")
    public T get(int index) {
        checkIndex(index);
        // The backing array is Object[]; we cast back to T on the way out.
        return (T) elements[index];
    }

    /* ----------------------------------------------------------------
     *  set(index, value): overwrite index, return the old value. O(1).
     * ---------------------------------------------------------------- */
    @SuppressWarnings("unchecked")
    public T set(int index, T value) {
        checkIndex(index);
        T old = (T) elements[index];
        elements[index] = value;
        return old;
    }

    /* ----------------------------------------------------------------
     *  remove(index): delete the element at index, shifting everything
     *  after it one slot to the left to close the gap. O(n).
     * ---------------------------------------------------------------- */
    @SuppressWarnings("unchecked")
    public T remove(int index) {
        checkIndex(index);
        T old = (T) elements[index];
        for (int i = index; i < size - 1; i++) {
            elements[i] = elements[i + 1];
        }
        elements[size - 1] = null; // let the garbage collector reclaim it
        size--;
        return old;
    }

    /* ----------------------------------------------------------------
     *  remove(value): remove the first element equal to value.
     *  Returns true if something was removed.
     * ---------------------------------------------------------------- */
    public boolean remove(T value) {
        int idx = indexOf(value);
        if (idx >= 0) {
            remove(idx);
            return true;
        }
        return false;
    }

    /* ----------------------------------------------------------------
     *  indexOf(value): first position holding value, or -1 if absent.
     * ---------------------------------------------------------------- */
    public int indexOf(T value) {
        for (int i = 0; i < size; i++) {
            if (equalsValue((T) elements[i], value)) {
                return i;
            }
        }
        return -1;
    }

    // True if value appears anywhere in the list.
    public boolean contains(T value) {
        return indexOf(value) >= 0;
    }

    // Number of elements currently stored (NOT the backing capacity).
    public int size() {
        return size;
    }

    // True when the list holds no elements.
    public boolean isEmpty() {
        return size == 0;
    }

    // Empty the list. We null out slots so objects can be collected.
    public void clear() {
        for (int i = 0; i < size; i++) {
            elements[i] = null;
        }
        size = 0;
    }

    // Readable form like [a, b, c] for printing.
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < size; i++) {
            sb.append(elements[i]);
            if (i < size - 1) {
                sb.append(", ");
            }
        }
        sb.append("]");
        return sb.toString();
    }

    // ================================================================
    //  PRIVATE HELPERS
    // ================================================================

    // Guarantee the backing array can hold at least 'minCapacity'
    // elements. If not, DOUBLE the capacity and copy everything over.
    private void ensureCapacity(int minCapacity) {
        if (minCapacity > elements.length) {
            int newCapacity = elements.length * 2;
            if (newCapacity < minCapacity) {
                newCapacity = minCapacity;
            }
            elements = Arrays.copyOf(elements, newCapacity);
        }
    }

    // Bounds check for read/replace operations (0 .. size-1).
    private void checkIndex(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException(
                    "Index " + index + " out of bounds for size " + size);
        }
    }

    // Bounds check for add(index): index == size (append) is allowed.
    private void checkIndexForAdd(int index) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException(
                    "Index " + index + " out of bounds for size " + size);
        }
    }

    // Null-safe equality so the list tolerates null elements.
    private boolean equalsValue(T a, T b) {
        return (a == null) ? (b == null) : a.equals(b);
    }
}
