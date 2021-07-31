package test.twitter;

import java.util.Observable;
import java.util.Observer;

public class TestObserver implements Observer {
    private int tweetCount = 0;

    @Override
    public void update(Observable observable, Object arg) {
        tweetCount++;
    }

    public int getTweetCount() {
        return tweetCount;
    }
}