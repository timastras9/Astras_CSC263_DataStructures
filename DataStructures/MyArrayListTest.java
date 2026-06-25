package DataStructures;

/* =====================================================================
 *  Course   : CSC263 - Data Structures (Java 2)
 *  Project  : Chapter 16 Data Structures -- Array List TEST CLASS
 *  Author   : Timothy Astras
 *  Date     : 2026-06-24
 *
 *  AI ASSISTANCE DISCLOSURE: Developed with AI assistance using Claude
 *  Code (Anthropic). I directed, reviewed, ran, and verified all work.
 *  See MyArrayList.java for the full disclosure statement.
 * =====================================================================
 *
 *  This test class exercises MyArrayList: appending, inserting in the
 *  middle, get/set, removing by index and value, searching, automatic
 *  growth past the initial capacity, and bounds checking.
 *
 *  Run it on its own with:
 *      java DataStructures.MyArrayListTest
 * =====================================================================
 */
public class MyArrayListTest {

    public static void main(String[] args) {
        TestUtil t = new TestUtil("MyArrayListTest");

        MyArrayList<String> list = new MyArrayList<>();
        t.check("new list isEmpty", list.isEmpty());
        t.checkEquals("new list size 0", 0, list.size());

        // append three values.
        list.add("A");
        list.add("B");
        list.add("C");               // -> [A, B, C]
        t.checkEquals("size after adds", 3, list.size());
        t.checkEquals("get(1)", "B", list.get(1));
        t.checkEquals("toString", "[A, B, C]", list.toString());

        // insert in the middle, shifting elements right.
        list.add(1, "X");            // -> [A, X, B, C]
        t.checkEquals("insert at 1", "X", list.get(1));
        t.checkEquals("element shifted right", "B", list.get(2));
        t.checkEquals("size after insert", 4, list.size());

        // set overwrites and returns the previous value.
        String old = list.set(0, "Z"); // -> [Z, X, B, C]
        t.checkEquals("set returns old value", "A", old);
        t.checkEquals("set updated slot", "Z", list.get(0));

        // search.
        t.checkEquals("indexOf B", 2, list.indexOf("B"));
        t.checkEquals("indexOf missing", -1, list.indexOf("Q"));
        t.check("contains C", list.contains("C"));

        // remove by index (shifts left) and by value.
        String removed = list.remove(1); // removes X -> [Z, B, C]
        t.checkEquals("remove(1) returns X", "X", removed);
        t.check("remove(\"B\") true", list.remove("B")); // -> [Z, C]
        t.check("remove(\"none\") false", !list.remove("none"));
        t.checkEquals("size after removals", 2, list.size());

        // bounds checking.
        t.expectException("get(5) out of bounds", () -> list.get(5));
        t.expectException("add at -1 out of bounds", () -> list.add(-1, "bad"));

        // GROWTH TEST: add far more than the default capacity (10) to
        // force the backing array to resize, and confirm nothing is lost.
        MyArrayList<Integer> big = new MyArrayList<>();
        for (int i = 0; i < 1000; i++) {
            big.add(i);
        }
        t.checkEquals("size after 1000 adds", 1000, big.size());
        t.checkEquals("first element intact", 0, big.get(0));
        t.checkEquals("last element intact", 999, big.get(999));

        // clear.
        big.clear();
        t.check("isEmpty after clear", big.isEmpty());

        if (!t.summary()) {
            System.exit(1);
        }
    }
}
