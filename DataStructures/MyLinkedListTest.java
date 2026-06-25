package DataStructures;

/* =====================================================================
 *  Course   : CSC263 - Data Structures (Java 2)
 *  Project  : Chapter 16 Data Structures -- Linked List TEST CLASS
 *  Author   : Timothy Astras
 *  Date     : 2026-06-24
 *
 *  AI ASSISTANCE DISCLOSURE: Developed with AI assistance using Claude
 *  Code (Anthropic). ALL thoughts, ideas, and innovation are my own
 *  (Timothy Astras); Claude Code only helped implement my ideas. I
 *  directed, reviewed, ran, and verified all work. See MyLinkedList.java
 *  for the full disclosure statement.
 * =====================================================================
 *
 *  This test class exercises MyLinkedList: adding at both ends and in
 *  the middle, getting/setting, removing by index and by value,
 *  searching, and the edge cases (empty list, bad indexes).
 *
 *  Run it on its own with:
 *      java DataStructures.MyLinkedListTest
 * =====================================================================
 */
public class MyLinkedListTest {

    public static void main(String[] args) {
        TestUtil t = new TestUtil("MyLinkedListTest");

        // A brand-new list should be empty.
        MyLinkedList<String> list = new MyLinkedList<>();
        t.check("new list isEmpty", list.isEmpty());
        t.checkEquals("new list size is 0", 0, list.size());

        // addLast / add appends to the end.
        list.add("B");
        list.add("C");
        list.addFirst("A");          // -> [A, B, C]
        t.checkEquals("size after 3 adds", 3, list.size());
        t.checkEquals("addFirst put A at front", "A", list.get(0));
        t.checkEquals("last element is C", "C", list.get(2));
        t.checkEquals("toString reflects order", "[A, B, C]", list.toString());

        // add(index, value) inserts in the middle, shifting the rest.
        list.add(1, "X");            // -> [A, X, B, C]
        t.checkEquals("inserted X at index 1", "X", list.get(1));
        t.checkEquals("size after middle insert", 4, list.size());

        // set replaces a value and returns the old one.
        String old = list.set(1, "Y"); // -> [A, Y, B, C]
        t.checkEquals("set returns replaced value", "X", old);
        t.checkEquals("set updated the slot", "Y", list.get(1));

        // search operations.
        t.checkEquals("indexOf B", 2, list.indexOf("B"));
        t.checkEquals("indexOf missing is -1", -1, list.indexOf("Z"));
        t.check("contains C", list.contains("C"));
        t.check("does not contain Z", !list.contains("Z"));

        // remove by index returns the removed value.
        String removed = list.remove(0); // removes A -> [Y, B, C]
        t.checkEquals("remove(0) returned A", "A", removed);
        t.checkEquals("front is now Y", "Y", list.get(0));

        // remove by value removes the first match and reports success.
        t.check("remove(\"B\") succeeds", list.remove("B")); // -> [Y, C]
        t.check("remove(\"nope\") fails", !list.remove("nope"));
        t.checkEquals("size after removals", 2, list.size());

        // removing past the end must be rejected.
        t.expectException("get(99) out of bounds", () -> list.get(99));
        t.expectException("remove(-1) out of bounds", () -> list.remove(-1));

        // clear empties the list.
        list.clear();
        t.check("isEmpty after clear", list.isEmpty());
        t.checkEquals("size 0 after clear", 0, list.size());

        // works with other types too (Integer here) to prove genericity.
        MyLinkedList<Integer> nums = new MyLinkedList<>();
        for (int i = 1; i <= 5; i++) {
            nums.add(i);
        }
        t.checkEquals("integer list size", 5, nums.size());
        t.checkEquals("integer list toString", "[1, 2, 3, 4, 5]", nums.toString());

        // Exit non-zero if anything failed (useful for automated checks).
        if (!t.summary()) {
            System.exit(1);
        }
    }
}
