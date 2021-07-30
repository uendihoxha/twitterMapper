package twitter;

import twitter4j.Status;
import util.ImageCache;

import java.util.*;

public abstract class TwitterSource extends Observable {
    protected boolean isLogging = true;
    // The set of getTerms to look for in the stream of tweets
    protected Set<String> terms = new HashSet<>();

    protected void log(Status status) {
        if (isLogging) {
            logUserStatus(status);
        }
        ImageCache.getInstance().loadImage(status.getUser().getProfileImageURL());
    }

    // This method is called each time a tweet is delivered to the application.
    // TO DO: Each active query should be informed about each incoming tweet so that
    //       it can determine whether the tweet should be displayed
    protected void handleTweet(Status s) {
        setChanged();
        notifyObservers(s);
    }


    public List<String> getFilterTerms() {
        return new ArrayList<>(terms);
    }

    public void setFilterTerms(Collection<String> terms) {
        this.terms.clear();
        this.terms.addAll(terms);
        sync();
    }
    private void logUserStatus(Status status) {
        System.out.println(status.getUser().getName() + ": " + status.getText());
    }

    // Called each time a new set of filter getTerms has been established
    abstract protected void sync();
}
