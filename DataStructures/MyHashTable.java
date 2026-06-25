package DataStructures;

/* =====================================================================
 *  Course   : CSC263 - Data Structures (Java 2)
 *  Project  : Chapter 16 Data Structures -- Hash Table
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
 *  A generic HASH TABLE (key -> value map) built from scratch using
 *  SEPARATE CHAINING to resolve collisions. We do NOT use
 *  java.util.HashMap.
 *
 *  HOW A HASH TABLE WORKS
 *    1. We keep an array of "buckets."
 *    2. To store a key, we compute the key's hashCode(), squeeze it
 *       into a valid bucket index with the modulo operator (%), and
 *       drop the (key, value) pair into that bucket.
 *    3. COLLISIONS happen when two different keys map to the same
 *       bucket. With separate chaining, each bucket is a small linked
 *       list, so colliding entries simply chain together in that
 *       bucket.
 *    4. As the table fills up (measured by the LOAD FACTOR =
 *       entries / buckets), lookups slow down. When the load factor
 *       crosses a threshold we REHASH: build a bigger bucket array and
 *       re-insert every entry. This keeps average put/get at O(1).
 * =====================================================================
 */
public class MyHashTable<K, V> {

    // ---- ENTRY: one (key, value) pair living in a bucket chain ------
    private static class Entry<K, V> {
        final K key;       // the key never changes once placed
        V value;           // the value can be overwritten by put()
        Entry<K, V> next;  // link to the next entry in the same bucket

        Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }

    // Default number of buckets, and the load-factor limit that
    // triggers a rehash (0.75 is the same threshold java.util uses).
    private static final int DEFAULT_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;

    // ---- FIELDS -----------------------------------------------------
    private Entry<K, V>[] buckets; // array of bucket chains
    private int size;              // total number of (key, value) pairs

    @SuppressWarnings("unchecked")
    public MyHashTable() {
        // Java will not let us write "new Entry[...]" with generics, so
        // we create a raw array and cast it -- a standard pattern.
        buckets = (Entry<K, V>[]) new Entry[DEFAULT_CAPACITY];
        size = 0;
    }

    /* ----------------------------------------------------------------
     *  put(key, value): insert a new pair, or UPDATE the value if the
     *  key already exists. Returns the previous value for that key, or
     *  null if the key is new.
     * ---------------------------------------------------------------- */
    public V put(K key, V value) {
        int index = bucketIndex(key);

        // Walk this bucket's chain looking for an existing matching key.
        for (Entry<K, V> e = buckets[index]; e != null; e = e.next) {
            if (equalsKey(e.key, key)) {
                V old = e.value;
                e.value = value; // key found -> overwrite its value
                return old;
            }
        }

        // Key not present: create a new entry and push it to the FRONT
        // of the bucket chain (O(1) insertion).
        Entry<K, V> entry = new Entry<>(key, value);
        entry.next = buckets[index];
        buckets[index] = entry;
        size++;

        // If we have grown too dense, enlarge and rehash everything.
        if ((double) size / buckets.length > LOAD_FACTOR) {
            rehash();
        }
        return null;
    }

    /* ----------------------------------------------------------------
     *  get(key): return the value stored for key, or null if the key
     *  is not present.
     * ---------------------------------------------------------------- */
    public V get(K key) {
        int index = bucketIndex(key);
        for (Entry<K, V> e = buckets[index]; e != null; e = e.next) {
            if (equalsKey(e.key, key)) {
                return e.value;
            }
        }
        return null;
    }

    /* ----------------------------------------------------------------
     *  remove(key): delete the pair for key. Returns the removed value,
     *  or null if the key was not present.
     * ---------------------------------------------------------------- */
    public V remove(K key) {
        int index = bucketIndex(key);
        Entry<K, V> prev = null;
        Entry<K, V> e = buckets[index];

        while (e != null) {
            if (equalsKey(e.key, key)) {
                // Unlink 'e' from the bucket chain.
                if (prev == null) {
                    buckets[index] = e.next; // it was first in the chain
                } else {
                    prev.next = e.next;
                }
                size--;
                return e.value;
            }
            prev = e;
            e = e.next;
        }
        return null; // key not found
    }

    // True if the key has a value stored in the table.
    public boolean containsKey(K key) {
        int index = bucketIndex(key);
        for (Entry<K, V> e = buckets[index]; e != null; e = e.next) {
            if (equalsKey(e.key, key)) {
                return true;
            }
        }
        return false;
    }

    // Number of (key, value) pairs stored.
    public int size() {
        return size;
    }

    // True when the table holds no pairs.
    public boolean isEmpty() {
        return size == 0;
    }

    /* ----------------------------------------------------------------
     *  keys(): collect every key into our own MyArrayList so callers
     *  can iterate over the table's contents.
     * ---------------------------------------------------------------- */
    public MyArrayList<K> keys() {
        MyArrayList<K> result = new MyArrayList<>();
        for (Entry<K, V> bucket : buckets) {
            for (Entry<K, V> e = bucket; e != null; e = e.next) {
                result.add(e.key);
            }
        }
        return result;
    }

    /* ----------------------------------------------------------------
     *  toString: list the pairs like {a=1, b=2}. Bucket order is not
     *  meaningful, so do not rely on the ordering.
     * ---------------------------------------------------------------- */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("{");
        boolean first = true;
        for (Entry<K, V> bucket : buckets) {
            for (Entry<K, V> e = bucket; e != null; e = e.next) {
                if (!first) {
                    sb.append(", ");
                }
                sb.append(e.key).append("=").append(e.value);
                first = false;
            }
        }
        sb.append("}");
        return sb.toString();
    }

    // ================================================================
    //  PRIVATE HELPERS
    // ================================================================

    // Map a key to a bucket index in 0 .. buckets.length-1.
    // We mask off the sign bit so the index is never negative, then
    // take it modulo the number of buckets.
    private int bucketIndex(K key) {
        int hash = (key == null) ? 0 : key.hashCode();
        return (hash & 0x7fffffff) % buckets.length;
    }

    // Grow the bucket array (doubling) and re-insert every existing
    // entry, because the bucket index depends on the array length.
    @SuppressWarnings("unchecked")
    private void rehash() {
        Entry<K, V>[] old = buckets;
        buckets = (Entry<K, V>[]) new Entry[old.length * 2];

        // Re-place each entry into the new, larger table.
        for (Entry<K, V> bucket : old) {
            for (Entry<K, V> e = bucket; e != null; ) {
                Entry<K, V> next = e.next;       // remember next before relinking
                int index = bucketIndex(e.key);  // recompute for the new size
                e.next = buckets[index];
                buckets[index] = e;
                e = next;
            }
        }
    }

    // Null-safe key equality.
    private boolean equalsKey(K a, K b) {
        return (a == null) ? (b == null) : a.equals(b);
    }
}
