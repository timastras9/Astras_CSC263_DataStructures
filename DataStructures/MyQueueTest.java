package DataStructures;

/* =====================================================================
 *  Course   : CSC263 - Data Structures (Java 2)
 *  Project  : Chapter 16 Data Structures -- Queue TEST CLASS
 *  Author   : Timothy Astras
 *  Date     : 2026-06-24
 *
 *  AI ASSISTANCE DISCLOSURE: Developed with AI assistance using Claude
 *  Code (Anthropic). ALL thoughts, ideas, and innovation are my own
 *  (Timothy Astras); Claude Code only helped implement my ideas. I
 *  directed, reviewed, ran, and verified all work. See MyQueue.java
 *  for the full disclosure statement.
 * =====================================================================
 *
 *  This test class exercises MyQueue and proves its FIFO (First In,
 *  First Out) behavior: enqueue, dequeue, peek, size, the empty-queue
 *  error case, and -- importantly -- the circular wrap-around plus
 *  resize that make the queue reuse freed slots correctly.
 *
 *  Run it on its own with:
 *      java DataStructures.MyQueueTest
 * =====================================================================
 */
public class MyQueueTest {

    public static void main(String[] args) {
        TestUtil t = new TestUtil("MyQueueTest");

        MyQueue<String> q = new MyQueue<>();
        t.check("new queue isEmpty", q.isEmpty());
        t.checkEquals("new queue size 0", 0, q.size());

        // enqueue three: A joins first, so A is at the front.
        q.enqueue("A");
        q.enqueue("B");
        q.enqueue("C");              // front -> [A, B, C]
        t.checkEquals("size after 3 enqueues", 3, q.size());

        // peek shows the FRONT without removing it.
        t.checkEquals("peek shows front (A)", "A", q.peek());
        t.checkEquals("peek did not change size", 3, q.size());

        // dequeue returns items in the SAME order they were added (FIFO).
        t.checkEquals("dequeue returns A", "A", q.dequeue());
        t.checkEquals("dequeue returns B", "B", q.dequeue());
        t.checkEquals("dequeue returns C", "C", q.dequeue());
        t.check("empty after dequeuing all", q.isEmpty());

        // dequeue/peek on an empty queue must throw.
        t.expectException("dequeue empty throws", () -> q.dequeue());
        t.expectException("peek empty throws", () -> q.peek());

        // CIRCULAR WRAP-AROUND TEST:
        // Interleave enqueue/dequeue so the head/tail indexes advance and
        // wrap around the backing array. A correct circular queue must
        // still return values in strict FIFO order.
        MyQueue<Integer> ring = new MyQueue<>();
        int expected = 0;   // next value we expect to dequeue
        int nextToAdd = 0;  // next value we will enqueue
        // prime the queue with a few items.
        for (int i = 0; i < 5; i++) {
            ring.enqueue(nextToAdd++);
        }
        // now alternately remove one and add two, many times.
        for (int round = 0; round < 50; round++) {
            t.checkEquals("FIFO value #" + expected, expected, ring.dequeue());
            expected++;
            ring.enqueue(nextToAdd++);
            ring.enqueue(nextToAdd++);
        }
        t.check("ring still has the remaining items", ring.size() > 0);

        // RESIZE TEST: fill far beyond the default capacity (10) without
        // any dequeues, forcing the backing array to grow.
        MyQueue<Integer> big = new MyQueue<>();
        for (int i = 0; i < 1000; i++) {
            big.enqueue(i);
        }
        t.checkEquals("size after 1000 enqueues", 1000, big.size());
        t.checkEquals("front is still 0 after growth", 0, big.peek());
        // draining must yield 0..999 in order.
        boolean ordered = true;
        for (int i = 0; i < 1000; i++) {
            if (big.dequeue() != i) {
                ordered = false;
                break;
            }
        }
        t.check("drained 1000 items in FIFO order", ordered);

        if (!t.summary()) {
            System.exit(1);
        }
    }
}
