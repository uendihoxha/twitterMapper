package test.filters;

import filters.Scanner;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


public class TestScanner {
    @Test
    public void testBasic() {
        Scanner x = new Scanner("trump");
        assertEquals("trump", x.peek());
        assertNull(x.advance());
    }

    @Test
    public void testAnd() {
        Scanner x = new Scanner("trump and evil");
        assertEquals("trump", x.peek());
        assertEquals("and", x.advance());
        assertEquals("and", x.peek());
        assertEquals("evil", x.advance());
        assertEquals("evil", x.peek());
        assertNull(x.advance());
    }

    @Test
    public void testAll() {
        String[] expected = {"trump", "and", "(", "evil", "or", "not", "(", "good", ")", ")"};
        runTest("trump and (evil or not (good))", expected);
    }

    @Test
    public void testOr() {
        String[] expected = {"trump", "or", "evil"};
        runTest("trump or evil", expected);
    }

    private void runTest(String input, String[] expected) {
        Scanner x = new Scanner(input);
        boolean first = true;
        for (String token : expected) {
            if (first) {
                first = false;
            } else {
                assertEquals(x.advance(), token);
            }
            assertEquals(x.peek(), token);
        }
        assertNull(x.advance());
    }
}
