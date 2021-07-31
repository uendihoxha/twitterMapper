package test.twitter;//package test.twitter;

import org.junit.jupiter.api.Test;
import twitter.TwitterSource;
import twitter.TwitterSourceFactory;

import java.security.InvalidParameterException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * Test the basic functionality of the TwitterSource
 */
public class TestPlaybackTwitterSource {
    @Test
    public void testSetup() {
        try {
            TwitterSource source = getTwitterSource();
            TestObserver to = new TestObserver();
            // TO DO: Once your TwitterSource class implements Observable, you must add the TestObserver as an observer to it here
            source.addObserver(to);
            source.setFilterTerms(set("food"));
            pause(3000);
            assertTrue(to.getTweetCount() > 0, "Expected getTweetCount() to be > 0, was " + to.getTweetCount());
            assertTrue(to.getTweetCount() <= 10, "Expected getTweetCount() to be <= 10, was " + to.getTweetCount());
            int firstBunch = to.getTweetCount();
            System.out.println("Now adding 'the'");
            source.setFilterTerms(set("food", "the"));
            pause(3000);
            assertTrue(to.getTweetCount() > 0, "Expected getTweetCount() to be > 0, was " + to.getTweetCount());
            assertTrue(to.getTweetCount() > firstBunch, "Expected getTweetCount() to be < firstBunch (" + firstBunch + "), was " + to.getTweetCount());
            assertTrue(to.getTweetCount() <= 10, "Expected getTweetCount() to be <= 10, was " + to.getTweetCount());
        } catch (InvalidParameterException e) {
            fail(e.getMessage());
        }
    }

    private void pause(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private <E> Set<E> set(E... entries) {
        Set<E> set = new HashSet<>();
        Collections.addAll(set, entries);
        return set;
    }

    private TwitterSource getTwitterSource() throws InvalidParameterException {
        TwitterSourceFactory.getInstance().setSpeedup(1.0);
        return TwitterSourceFactory.getInstance().getTwitterSource(TwitterSourceFactory.PLAYBACK);
    }
}
