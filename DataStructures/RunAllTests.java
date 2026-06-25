package DataStructures;

/* =====================================================================
 *  Course   : CSC263 - Data Structures (Java 2)
 *  Project  : Chapter 16 Data Structures -- Run ALL Test Suites
 *  Author   : Timothy Astras
 *  Date     : 2026-06-24
 *
 *  AI ASSISTANCE DISCLOSURE: Developed with AI assistance using Claude
 *  Code (Anthropic). ALL thoughts, ideas, and innovation are my own
 *  (Timothy Astras); Claude Code only helped implement my ideas. I
 *  directed, reviewed, ran, and verified all work. See any
 *  implementation file for the full disclosure statement.
 * =====================================================================
 *
 *  Convenience entry point that runs every data-structure test class
 *  back to back, so the whole assignment can be verified with a single
 *  command:
 *      java DataStructures.RunAllTests
 *
 *  (Each test class also still has its own main() and can be run alone.)
 * =====================================================================
 */
public class RunAllTests {

    public static void main(String[] args) {
        // Each call below runs one full suite and prints its own report.
        MyLinkedListTest.main(args);
        MyArrayListTest.main(args);
        MyStackTest.main(args);
        MyQueueTest.main(args);
        MyHashTableTest.main(args);

        System.out.println("All five data-structure test suites finished.");
    }
}
