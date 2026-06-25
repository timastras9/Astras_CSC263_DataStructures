package DataStructures;

/* =====================================================================
 *  Course   : CSC263 - Data Structures (Java 2)
 *  Project  : Chapter 16 Data Structures -- Stack TEST CLASS
 *  Author   : Timothy Astras
 *  Date     : 2026-06-24
 *
 *  AI ASSISTANCE DISCLOSURE: Developed with AI assistance using Claude
 *  Code (Anthropic). ALL thoughts, ideas, and innovation are my own
 *  (Timothy Astras); Claude Code only helped implement my ideas. I
 *  directed, reviewed, ran, and verified all work. See MyStack.java
 *  for the full disclosure statement.
 * =====================================================================
 *
 *  This test class exercises MyStack and proves its LIFO (Last In,
 *  First Out) behavior: push, pop, peek, size, and the error case of
 *  popping/peeking an empty stack.
 *
 *  Run it on its own with:
 *      java DataStructures.MyStackTest
 * =====================================================================
 */
public class MyStackTest {

    public static void main(String[] args) {
        TestUtil t = new TestUtil("MyStackTest");

        MyStack<Integer> stack = new MyStack<>();
        t.check("new stack isEmpty", stack.isEmpty());
        t.checkEquals("new stack size 0", 0, stack.size());

        // push three values: 1 goes on first, 3 ends up on top.
        stack.push(1);
        stack.push(2);
        stack.push(3);               // top -> 3
        t.checkEquals("size after 3 pushes", 3, stack.size());

        // peek shows the top WITHOUT removing it.
        t.checkEquals("peek shows top (3)", 3, stack.peek());
        t.checkEquals("peek did not change size", 3, stack.size());

        // pop returns items in REVERSE of insertion order (LIFO).
        t.checkEquals("pop returns 3", 3, stack.pop());
        t.checkEquals("pop returns 2", 2, stack.pop());
        t.checkEquals("pop returns 1", 1, stack.pop());
        t.check("empty after popping all", stack.isEmpty());

        // popping/peeking an empty stack must throw.
        t.expectException("pop empty throws", () -> stack.pop());
        t.expectException("peek empty throws", () -> stack.peek());

        // a quick LIFO order demonstration with strings.
        MyStack<String> letters = new MyStack<>();
        letters.push("a");
        letters.push("b");
        letters.push("c");
        StringBuilder order = new StringBuilder();
        while (!letters.isEmpty()) {
            order.append(letters.pop()); // c, then b, then a
        }
        t.checkEquals("LIFO pop order is cba", "cba", order.toString());

        // capacity growth: push well past the default capacity (10).
        MyStack<Integer> big = new MyStack<>();
        for (int i = 0; i < 500; i++) {
            big.push(i);
        }
        t.checkEquals("size after 500 pushes", 500, big.size());
        t.checkEquals("top is 499", 499, big.peek());

        if (!t.summary()) {
            System.exit(1);
        }
    }
}
