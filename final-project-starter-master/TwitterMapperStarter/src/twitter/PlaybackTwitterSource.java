package twitter;

import twitter4j.Status;
import util.ObjectSource;

/**
 * A Twitter source that plays back a recorded stream of tweets.
 * <p>
 * It ignores the set of getTerms provided except it uses the first call to setFilterTerms
 * as a signal to begin playback of the recorded stream of tweets.
 * <p>
 * Implements Observable - each tweet is signalled to all observers
 */
public class PlaybackTwitterSource extends TwitterSource {
    // The speedup to apply to the recorded stream of tweets; 2 means play at twice the rate
    // at which the tweets were recorded
    private final double speedup;
    private final ObjectSource source;
    private boolean threadStarted = false;

    public PlaybackTwitterSource(double speedup) {
        this.speedup = speedup;
        source = new ObjectSource("C:\\Users\\KK\\Desktop\\Twitter-Map-master\\TwitterMapperStarter\\data\\TwitterCapture.jobj");
    }

    private void startThread() {
        if (threadStarted) {
            return;
        }
        threadStarted = true;
        Thread thread = new PlaybackTwitterThread(speedup, source) {
            @Override
            public void handleTweet(Status status) {
                this.handleTweet(status);
            }
        };
        thread.start();
    }

    /**
     * The playback source merely starts the playback thread, it it hasn't been started already
     */
    protected void sync() {
        System.out.println("Starting playback thread with " + terms);
        startThread();
    }
}
