package twitter;

import twitter4j.Status;
import util.ObjectSource;

public abstract class PlaybackTwitterThread extends Thread {
    private final double speedup;
    private final ObjectSource source;

    private long initialDelay = 1000;
    private long playbackStartTime = System.currentTimeMillis() + initialDelay;
    private long recordStartTime = 0;

    public PlaybackTwitterThread(double speedup, ObjectSource source) {
        this.speedup = speedup;
        this.source = source;
    }

    public void run() {
        long now;
        while (true) {
            Object timeObject = source.read();
            if (timeObject == null) {
                break;
            }
            Object statusObject = source.read();
            if (statusObject == null) {
                break;
            }
            long statusTime = (Long) timeObject;
            if (recordStartTime == 0) {
                recordStartTime = statusTime;
            }
            Status status = (Status) statusObject;
            long playbackTime = computePlaybackTime(statusTime);
            while ((now = System.currentTimeMillis()) < playbackTime) {
                pause(playbackTime - now);
            }
            if (status.getPlace() != null) {
                handleTweet(status);
            }
        }
    }

    private long computePlaybackTime(long statusTime) {
        long statusDelta = statusTime - recordStartTime;
        long targetDelta = Math.round(statusDelta / speedup);
        return playbackStartTime + targetDelta;
    }

    private void pause(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public abstract void handleTweet(Status status);
}
