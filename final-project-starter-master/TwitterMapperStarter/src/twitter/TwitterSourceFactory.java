package twitter;

import java.security.InvalidParameterException;

public class TwitterSourceFactory {
    public static final int PLAYBACK = 0;
    public static final int LIVE = 1;

    private double speedup = 0;

    private TwitterSourceFactory() {
    }

    public static TwitterSourceFactory getInstance() {
        return LazySingleton.INSTANCE;
    }

    public void setSpeedup(double speedup) {
        this.speedup = speedup;
    }

    public TwitterSource getTwitterSource(int type) throws InvalidParameterException {
        if (type == PLAYBACK) {
            return new PlaybackTwitterSource(speedup);
        } else if (type == LIVE) {
            return new LiveTwitterSource();
        }
        throw new InvalidParameterException("Invalid TwitterSource type");
    }

    private static class LazySingleton {
        private static final TwitterSourceFactory INSTANCE = new TwitterSourceFactory();
    }
}
