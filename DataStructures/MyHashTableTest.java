package DataStructures;

/* =====================================================================
 *  Course   : CSC263 - Data Structures (Java 2)
 *  Project  : Chapter 16 Data Structures -- Hash Table TEST CLASS
 *  Author   : Timothy Astras
 *  Date     : 2026-06-24
 *
 *  AI ASSISTANCE DISCLOSURE: Developed with AI assistance using Claude
 *  Code (Anthropic). ALL thoughts, ideas, and innovation are my own
 *  (Timothy Astras); Claude Code only helped implement my ideas. I
 *  directed, reviewed, ran, and verified all work. See MyHashTable.java
 *  for the full disclosure statement.
 * =====================================================================
 *
 *  This test class exercises MyHashTable: put/get, updating an existing
 *  key, containsKey, remove, key collection, and -- importantly -- a
 *  large insertion run that forces the table to rehash (grow) while
 *  keeping every key/value pair correct.
 *
 *  Run it on its own with:
 *      java DataStructures.MyHashTableTest
 * =====================================================================
 */
public class MyHashTableTest {

    public static void main(String[] args) {
        TestUtil t = new TestUtil("MyHashTableTest");

        MyHashTable<String, Integer> table = new MyHashTable<>();
        t.check("new table isEmpty", table.isEmpty());
        t.checkEquals("new table size 0", 0, table.size());

        // put inserts new pairs and returns null (no previous value).
        t.checkEquals("put new key returns null", null, table.put("one", 1));
        table.put("two", 2);
        table.put("three", 3);
        t.checkEquals("size after 3 puts", 3, table.size());

        // get retrieves stored values.
        t.checkEquals("get(one)", 1, table.get("one"));
        t.checkEquals("get(two)", 2, table.get("two"));
        t.checkEquals("get(missing) is null", null, table.get("missing"));

        // putting an existing key UPDATES the value and returns the old one.
        Integer prev = table.put("one", 100);
        t.checkEquals("put existing returns old value", 1, prev);
        t.checkEquals("value was updated", 100, table.get("one"));
        t.checkEquals("size unchanged on update", 3, table.size());

        // containsKey.
        t.check("containsKey(two)", table.containsKey("two"));
        t.check("does not contain bogus", !table.containsKey("bogus"));

        // remove returns the removed value and shrinks the table.
        t.checkEquals("remove(two) returns 2", 2, table.remove("two"));
        t.check("two gone after remove", !table.containsKey("two"));
        t.checkEquals("remove(missing) returns null", null, table.remove("missing"));
        t.checkEquals("size after one remove", 2, table.size());

        // keys() should return exactly the remaining keys (order ignored).
        MyArrayList<String> keys = table.keys();
        t.checkEquals("keys() count", 2, keys.size());
        t.check("keys contains one", keys.contains("one"));
        t.check("keys contains three", keys.contains("three"));

        // REHASH STRESS TEST: insert many pairs to push the load factor
        // past 0.75 and trigger one or more internal resizes. Every value
        // must still be retrievable afterward.
        MyHashTable<Integer, Integer> big = new MyHashTable<>();
        int N = 5000;
        for (int i = 0; i < N; i++) {
            big.put(i, i * i);       // map i -> i squared
        }
        t.checkEquals("size after " + N + " puts", N, big.size());

        boolean allCorrect = true;
        for (int i = 0; i < N; i++) {
            Integer v = big.get(i);
            if (v == null || v != i * i) {
                allCorrect = false;
                break;
            }
        }
        t.check("all " + N + " values correct after rehash", allCorrect);

        // removing half the keys should leave the other half intact.
        for (int i = 0; i < N; i += 2) {
            big.remove(i);
        }
        t.checkEquals("size after removing evens", N / 2, big.size());
        t.check("even key gone", !big.containsKey(0));
        t.check("odd key present", big.containsKey(1));

        if (!t.summary()) {
            System.exit(1);
        }
    }
}
