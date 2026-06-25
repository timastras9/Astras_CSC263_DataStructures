package DataStructures;

/* =====================================================================
 *  Course   : CSC263 - Data Structures (Java 2)
 *  Project  : Chapter 16 Data Structures -- Shared Test Helper
 *  Author   : Timothy Astras
 *  Date     : 2026-06-24
 *
 *  AI ASSISTANCE DISCLOSURE: Developed with AI assistance using Claude
 *  Code (Anthropic). I directed, reviewed, ran, and verified all work.
 *  See any implementation file for the full disclosure statement.
 * =====================================================================
 *
 *  WHAT THIS FILE IS
 *  -----------------
 *  A tiny, self-contained testing helper so each test class can check
 *  conditions and print readable PASS/FAIL lines WITHOUT needing an
 *  external library such as JUnit. This keeps the whole project
 *  runnable with nothing but the standard "javac" and "java" commands.
 *
 *  HOW TO USE IT (inside a test class):
 *      TestUtil t = new TestUtil("MyThingTest");
 *      t.check("description", actualBoolean);
 *      t.checkEquals("description", expected, actual);
 *      t.expectException("description", () -> something.thatThrows());
 *      t.summary();   // prints the pass/fail totals; returns true if all passed
 * =====================================================================
 */
public class TestUtil {

    private final String suiteName; // name shown in the report header
    private int passed;             // running count of passing checks
    private int failed;             // running count of failing checks

    public TestUtil(String suiteName) {
        this.suiteName = suiteName;
        System.out.println("==================================================");
        System.out.println("  Running test suite: " + suiteName);
        System.out.println("==================================================");
    }

    /* ----------------------------------------------------------------
     *  check: pass if 'condition' is true, fail otherwise.
     * ---------------------------------------------------------------- */
    public void check(String description, boolean condition) {
        if (condition) {
            pass(description);
        } else {
            fail(description, "expected condition to be true");
        }
    }

    /* ----------------------------------------------------------------
     *  checkEquals: pass if expected and actual are equal (null-safe).
     *  On failure it prints BOTH values so the problem is easy to see.
     * ---------------------------------------------------------------- */
    public void checkEquals(String description, Object expected, Object actual) {
        boolean equal = (expected == null) ? (actual == null) : expected.equals(actual);
        if (equal) {
            pass(description);
        } else {
            fail(description, "expected <" + expected + "> but got <" + actual + ">");
        }
    }

    /* ----------------------------------------------------------------
     *  expectException: pass ONLY if running the given code throws.
     *  Used to confirm that, e.g., popping an empty stack is an error.
     *  Runnable is a standard Java functional interface, so callers can
     *  pass a lambda: () -> stack.pop().
     * ---------------------------------------------------------------- */
    public void expectException(String description, Runnable code) {
        try {
            code.run();
            fail(description, "expected an exception but none was thrown");
        } catch (Exception ex) {
            pass(description + " (threw " + ex.getClass().getSimpleName() + ")");
        }
    }

    /* ----------------------------------------------------------------
     *  summary: print totals. Returns true if every check passed, which
     *  lets a test's main() set the process exit code accordingly.
     * ---------------------------------------------------------------- */
    public boolean summary() {
        System.out.println("--------------------------------------------------");
        System.out.println("  " + suiteName + " results: "
                + passed + " passed, " + failed + " failed");
        System.out.println("==================================================");
        System.out.println();
        return failed == 0;
    }

    // ---- PRIVATE: formatting of individual result lines -------------
    private void pass(String description) {
        passed++;
        System.out.println("  [PASS] " + description);
    }

    private void fail(String description, String detail) {
        failed++;
        System.out.println("  [FAIL] " + description + "  -- " + detail);
    }
}
