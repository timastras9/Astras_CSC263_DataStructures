# CSC263 — Chapter 16 Data Structures (Java 2 Submission)

**Author:** Timothy Astras
**Course:** CSC263 — Data Structures (Java 2)
**Date:** 2026-06-24

From-scratch, generic Java implementations of the five core Chapter 16
data structures — **Linked List, Array List, Stack, Queue, and Hash
Table** — each paired with its own **test class**. None of these wrap
the built-in `java.util` collections; they are implemented directly so
the underlying mechanics (nodes, resizing arrays, circular buffers,
hashing with collision handling) are visible.

---

## How to run

Requirements:
- Java **11 or newer** (only the standard library — no internet, no JUnit).

From the project folder (`Astras_CSC263_DataStructures/`):

```bash
# compile everything
javac DataStructures/*.java

# run ALL five test suites at once
java DataStructures.RunAllTests
```

You can also run any single suite on its own:

```bash
java DataStructures.MyLinkedListTest
java DataStructures.MyArrayListTest
java DataStructures.MyStackTest
java DataStructures.MyQueueTest
java DataStructures.MyHashTableTest
```

Each test prints a `[PASS]` / `[FAIL]` line per check and a summary
total. A suite exits with a non-zero status code if any check fails, so
it also works in automated grading. All suites currently pass
(**151 checks, 0 failures**).

> **Note:** Compiling prints one *"uses unchecked or unsafe
> operations"* note. This is expected and harmless — it comes from
> mixing Java generics with arrays (`new Entry[...]`), which is
> unavoidable when building generic collections from scratch. The code
> uses `@SuppressWarnings` where this is intentional.

---

## What each structure implements

| Structure       | File                | Design                                   | Highlights |
|-----------------|---------------------|------------------------------------------|-----------|
| **Linked List** | `MyLinkedList.java` | Doubly linked nodes (head + tail)        | O(1) add/remove at ends; `add/get/set/remove(index)`, `remove(value)`, `indexOf`, `contains` |
| **Array List**  | `MyArrayList.java`  | Resizable `Object[]` (doubles when full) | O(1) `get/set`; auto-growth; middle insert/remove with shifting |
| **Stack**       | `MyStack.java`      | LIFO over a resizable array              | `push`, `pop`, `peek`; throws on empty pop/peek |
| **Queue**       | `MyQueue.java`      | FIFO over a **circular** array           | `enqueue`, `dequeue`, `peek`; index wrap-around + resize |
| **Hash Table**  | `MyHashTable.java`  | `K→V` map, **separate chaining**         | `put/get/remove/containsKey/keys`; auto-rehash at 0.75 load factor |

All five are **generic** (`<T>`, or `<K, V>` for the hash table), so
they hold any object type while staying type-safe.

---

## Test coverage (for grading)

Each test class lives next to the structure it verifies and checks the
normal cases, the search/update cases, the edge cases (empty
structures, out-of-bounds indexes), and a **stress case** that forces
internal growth:

| Test class             | Notable checks |
|------------------------|----------------|
| `MyLinkedListTest`     | end/middle insertion, set returns old value, remove by index & value, bounds exceptions, genericity |
| `MyArrayListTest`      | shifting on insert/remove, bounds exceptions, **1,000-element growth** keeps data intact |
| `MyStackTest`          | LIFO order (`cba`), empty-pop/peek throw, 500-push growth |
| `MyQueueTest`          | FIFO order, empty-dequeue throw, **circular wrap-around** via interleaved enqueue/dequeue, 1,000-element drain in order |
| `MyHashTableTest`      | put/update/remove, `containsKey`, `keys()`, **5,000-entry rehash** with every value verified |

`TestUtil.java` is a small shared helper providing `check`,
`checkEquals`, and `expectException` so the tests stay readable without
pulling in an external framework. `RunAllTests.java` runs every suite
in sequence.

---

## File layout

```
Astras_CSC263_DataStructures/
├── README.md                       (this file)
├── .gitignore                      (excludes compiled *.class files)
└── DataStructures/
    ├── MyLinkedList.java           (doubly linked list)
    ├── MyArrayList.java            (resizable array list)
    ├── MyStack.java                (LIFO stack)
    ├── MyQueue.java                (FIFO circular queue)
    ├── MyHashTable.java            (separate-chaining hash table)
    ├── TestUtil.java               (shared assert/print helper)
    ├── MyLinkedListTest.java       (test suite)
    ├── MyArrayListTest.java        (test suite)
    ├── MyStackTest.java            (test suite)
    ├── MyQueueTest.java            (test suite)
    ├── MyHashTableTest.java        (test suite)
    └── RunAllTests.java            (runs all five suites)
```

---

## AI Assistance Disclosure (Academic Honesty)

This project was developed with AI assistance using **Claude Code**, an
AI coding assistant from Anthropic. The assignment — writing
from-scratch Java implementations of the Chapter 16 data structures
(Linked List, Array List, Stack, Queue, Hash Table) with test classes —
is the requirement given for my Java 2 (CSC263) class, drawing on the
course textbook (Chapter 16) and standard data-structures references.
**All original thoughts, ideas, design decisions, and innovation behind
this work are my own (Timothy Astras)** — Claude Code was used purely as
a programming assistant to help me implement my ideas in Java. I
directed every step, reviewed every line, compiled and ran all tests,
and verified the output. This disclosure is provided in compliance with
the course's academic-integrity policy on AI tool use.
