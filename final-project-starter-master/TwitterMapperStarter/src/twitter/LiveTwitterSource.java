package twitter;

import twitter4j.*;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

/**
 * Encapsulates the connection to Twitter
 * <p>
 * Terms to include in the returned tweets can be set with setFilterTerms
 * <p>
 * Implements Observable - each received tweet is signalled to all observers
 */
public class LiveTwitterSource extends TwitterSource {
    private TwitterStream twitterStream;

    public LiveTwitterSource() {
        twitterStream = new TwitterStreamFactory(getConfiguration()).getInstance();
        twitterStream.addListener(getStatusAdapter());
    }

    protected void sync() {
        FilterQuery filter = new FilterQuery();
        String[] queriesArray = terms.toArray(new String[0]);
        filter.track(queriesArray);

        System.out.println("Syncing live Twitter stream with " + terms);

        twitterStream.filter(filter);
    }

    private StatusAdapter getStatusAdapter() {
        return new StatusAdapter() {
            @Override
            public void onStatus(Status status) {
                // This method is called each time a tweet is delivered by the twitter API
                if (status.getPlace() != null) {
                    handleTweet(status);
                }
            }
        };
    }

    private Configuration getConfiguration() {
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setOAuthConsumerKey("ooR089Dty29SlzZgqMWtdPtr9")
                .setOAuthConsumerSecret("VzOPVwCFxv0QajLWYCTcUBRytWmwsdM42moPWBpSnkJ8rOMZVg")
                .setOAuthAccessToken("1281304163863977987-fVvFvSRC7SZfswoIByPk5qoaHYjNE2")
                .setOAuthAccessTokenSecret("kpvIp2zeTLkhxPx9iMYpi6if7x418DGgsYoCYongH6Joi");
        return cb.build();
    }
}
