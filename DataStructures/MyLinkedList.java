package DataStructures;

/* =====================================================================
 *  Course   : CSC263 - Data Structures (Java 2)
 *  Project  : Chapter 16 Data Structures -- Linked List
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
 *  A generic, DOUBLY LINKED LIST built from scratch (we do NOT use
 *  java.util.LinkedList). A linked list stores each element inside a
 *  "node." Each node holds:
 *      - the value,
 *      - a reference to the PREVIOUS node, and
 *      - a reference to the NEXT node.
 *  The list keeps a pointer to the first node (head) and the last
 *  node (tail). Because nodes are chained by references, inserting or
 *  removing at the ends is O(1) -- no shifting of elements is needed.
 *
 *  <T> is a "type parameter": it lets the same list hold Strings,
 *  Integers, or any object type while staying type-safe.
 * =====================================================================
 */
public class MyLinkedList<T> {

    // ---- NODE: the building block of the linked list ----------------
    // A node is a small box holding one value plus links to its
    // neighbors. It is private+static because callers never touch it.
    private static class Node<T> {
        T value;        // the data stored in this node
        Node<T> prev;   // link to the node before this one (null at head)
        Node<T> next;   // link to the node after this one (null at tail)

        Node(T value) {
            this.value = value;
        }
    }

    // ---- FIELDS -----------------------------------------------------
    private Node<T> head; // first node in the list (null when empty)
    private Node<T> tail; // last node in the list  (null when empty)
    private int size;     // how many elements are currently stored

    /* ----------------------------------------------------------------
     *  addLast / add: append a value to the END of the list. O(1).
     * ---------------------------------------------------------------- */
    public void addLast(T value) {
        Node<T> node = new Node<>(value);
        if (tail == null) {
            // List is empty: this new node is both head and tail.
            head = node;
            tail = node;
        } else {
            // Link the new node after the current tail, then move tail.
            node.prev = tail;
            tail.next = node;
            tail = node;
        }
        size++;
    }

    // Convenience alias so callers can use the familiar name add().
    public void add(T value) {
        addLast(value);
    }

    /* ----------------------------------------------------------------
     *  addFirst: insert a value at the FRONT of the list. O(1).
     * ---------------------------------------------------------------- */
    public void addFirst(T value) {
        Node<T> node = new Node<>(value);
        if (head == null) {
            head = node;
            tail = node;
        } else {
            node.next = head;
            head.prev = node;
            head = node;
        }
        size++;
    }

    /* ----------------------------------------------------------------
     *  add(index, value): insert so the value lands at position index.
     *  Valid index range is 0..size (size means "append at the end").
     * ---------------------------------------------------------------- */
    public void add(int index, T value) {
        checkIndexForAdd(index);
        if (index == 0) {
            addFirst(value);
        } else if (index == size) {
            addLast(value);
        } else {
            Node<T> current = nodeAt(index);   // node currently at index
            Node<T> node = new Node<>(value);
            Node<T> before = current.prev;
            // Splice the new node in between 'before' and 'current'.
            node.prev = before;
            node.next = current;
            before.next = node;
            current.prev = node;
            size++;
        }
    }

    /* ----------------------------------------------------------------
     *  get(index): return the value stored at the given position. O(n).
     * ---------------------------------------------------------------- */
    public T get(int index) {
        checkIndex(index);
        return nodeAt(index).value;
    }

    /* ----------------------------------------------------------------
     *  set(index, value): overwrite the value at index, return the old
     *  value so the caller knows what was replaced.
     * ---------------------------------------------------------------- */
    public T set(int index, T value) {
        checkIndex(index);
        Node<T> node = nodeAt(index);
        T old = node.value;
        node.value = value;
        return old;
    }

    /* ----------------------------------------------------------------
     *  remove(index): unlink and return the value at the given index.
     * ---------------------------------------------------------------- */
    public T remove(int index) {
        checkIndex(index);
        Node<T> node = nodeAt(index);
        return unlink(node);
    }

    /* ----------------------------------------------------------------
     *  remove(value): remove the FIRST node whose value equals 'value'.
     *  Returns true if something was removed, false if not found.
     * ---------------------------------------------------------------- */
    public boolean remove(T value) {
        for (Node<T> n = head; n != null; n = n.next) {
            if (equalsValue(n.value, value)) {
                unlink(n);
                return true;
            }
        }
        return false;
    }

    /* ----------------------------------------------------------------
     *  indexOf(value): position of the first matching value, or -1.
     * ---------------------------------------------------------------- */
    public int indexOf(T value) {
        int i = 0;
        for (Node<T> n = head; n != null; n = n.next) {
            if (equalsValue(n.value, value)) {
                return i;
            }
            i++;
        }
        return -1;
    }

    // True if the value is present anywhere in the list.
    public boolean contains(T value) {
        return indexOf(value) >= 0;
    }

    // Number of elements currently stored.
    public int size() {
        return size;
    }

    // True when the list holds no elements.
    public boolean isEmpty() {
        return size == 0;
    }

    // Remove every element, returning the list to its empty state.
    public void clear() {
        head = null;
        tail = null;
        size = 0;
    }

    /* ----------------------------------------------------------------
     *  toString: produce a readable form like [a, b, c] for printing.
     * ---------------------------------------------------------------- */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        for (Node<T> n = head; n != null; n = n.next) {
            sb.append(n.value);
            if (n.next != null) {
                sb.append(", ");
            }
        }
        sb.append("]");
        return sb.toString();
    }

    // ================================================================
    //  PRIVATE HELPERS (internal plumbing -- not part of the public API)
    // ================================================================

    // Walk to the node at the given index. Walking from the nearer end
    // (head or tail) keeps this as fast as possible.
    private Node<T> nodeAt(int index) {
        Node<T> n;
        if (index < size / 2) {
            n = head;
            for (int i = 0; i < index; i++) {
                n = n.next;
            }
        } else {
            n = tail;
            for (int i = size - 1; i > index; i--) {
                n = n.prev;
            }
        }
        return n;
    }

    // Detach a node from the chain, fix its neighbors' links, and
    // return the value it held.
    private T unlink(Node<T> node) {
        Node<T> before = node.prev;
        Node<T> after = node.next;

        if (before == null) {
            head = after;            // node was the head
        } else {
            before.next = after;
        }
        if (after == null) {
            tail = before;           // node was the tail
        } else {
            after.prev = before;
        }
        size--;
        return node.value;
    }

    // Bounds check for operations that read/replace an existing slot.
    private void checkIndex(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException(
                    "Index " + index + " out of bounds for size " + size);
        }
    }

    // Bounds check for add(index): here index == size is allowed.
    private void checkIndexForAdd(int index) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException(
                    "Index " + index + " out of bounds for size " + size);
        }
    }

    // Null-safe equality so the list works even if a value is null.
    private boolean equalsValue(T a, T b) {
        return (a == null) ? (b == null) : a.equals(b);
    }
}
