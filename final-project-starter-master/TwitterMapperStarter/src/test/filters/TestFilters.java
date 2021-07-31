package test.filters;

import filters.*;
import org.junit.jupiter.api.Test;
import twitter4j.Status;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TestFilters {
    @Test
    public void testSimpleFilter() {
        Filter filter = new BasicFilter("fred");
        assertFilterOR(filter);
    }

    @Test
    public void testFilterNOT() {
        Filter filter = new NotFilter(new BasicFilter("fred"));
        assertFalse(filter.matches(getMockStatus("Fred Flintstone")));
        assertFalse(filter.matches(getMockStatus("fred Flintstone")));
        assertTrue(filter.matches(getMockStatus("Red Skelton")));
        assertTrue(filter.matches(getMockStatus("red Skelton")));
    }

    @Test
    public void testFilterAND() {
        Filter filter1 = new AndFilter(new BasicFilter("fre"), new BasicFilter("fre"));
        assertFilterOR(filter1);

        Filter filter2 = new AndFilter(new BasicFilter("Fred"), new BasicFilter("Marc"));
        assertFalse(filter2.matches(getMockStatus("Fred Flintstone")));
        assertFalse(filter2.matches(getMockStatus("fred Flintstone")));
        assertFalse(filter2.matches(getMockStatus("Red Skelton")));
        assertFalse(filter2.matches(getMockStatus("red Skelton")));

        Filter filter3 = new AndFilter(new BasicFilter("red"), new BasicFilter("red"));
        assertTrue(filter3.matches(getMockStatus("Fred Flintstone")));
        assertTrue(filter3.matches(getMockStatus("fred Flintstone")));
        assertTrue(filter3.matches(getMockStatus("Red Skelton")));
        assertTrue(filter3.matches(getMockStatus("red Skelton")));

        Filter filter4 = new AndFilter(new BasicFilter("douni"), new BasicFilter("matis"));
        assertEquals("(douni and matis)", filter4.toString());
    }

    @Test
    public void testFilterOR() {
        Filter filter1 = new OrFilter(new BasicFilter("zzzz"), new BasicFilter("Fred"));
        assertFilterOR(filter1);
        Filter filter2 = new OrFilter(new BasicFilter("fred"), new BasicFilter("zzzz"));
        assertFilterOR(filter2);
    }

    private void assertFilterOR(Filter filter) {
        assertTrue(filter.matches(getMockStatus("Fred Flintstone")));
        assertTrue(filter.matches(getMockStatus("fred Flintstone")));
        assertFalse(filter.matches(getMockStatus("Red Skelton")));
        assertFalse(filter.matches(getMockStatus("red Skelton")));
    }

    private Status getMockStatus(String text) {
        return new MockStatus(text);
    }

    @Test
    public void testSimpleFilterTerms() {
        Filter filter = new BasicFilter("fred");
        List<String> listOfTerms = filter.getTerms();
        assertTrue(listOfTerms.contains("fred"));
        assertFalse(listOfTerms.contains("fr"));
    }

    @Test
    public void testNotFilterTerms() {
        Filter filter = new NotFilter(new BasicFilter("fred"));
        List<String> listOfTerms = filter.getTerms();
        assertTrue(listOfTerms.contains("fred"));
        assertFalse(listOfTerms.contains("fr"));
    }

    @Test
    public void testAndFilterTerms() {
        Filter filter = new AndFilter(new BasicFilter("fred"), new BasicFilter("flinstone"));
        List<String> listOfTerms = filter.getTerms();
        System.out.println(listOfTerms);
        assertTrue(listOfTerms.contains("fred"));
        assertTrue(listOfTerms.contains("flinstone"));
    }

    @Test
    public void testOrFilterTerms() {
        Filter filter = new OrFilter(new BasicFilter("fred"), new BasicFilter("flinstone"));
        List<String> listOfTerms = filter.getTerms();
        assertTrue(listOfTerms.contains("fred"));
        assertTrue(listOfTerms.contains("flinstone"));
    }
}
