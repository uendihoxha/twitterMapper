package filters;

import twitter4j.Status;

import java.util.List;

/**
 * An interface that captures what it means to be a filter.
 */
public interface Filter {
    /**
     * Returns true if the filter matches the given tweet
     * @param s     the tweet to check
     * @return      whether or not the tweet matches this filter
     */
    static Filter parse(String queryString) {
        try {
            return new Parser(queryString).parse();
        } catch (IncorrectSyntaxException e) {
            return new BasicFilter(queryString);
        }
    }

    /**
     * Returns true if the filter matches the given tweet
     *
     * @param s the tweet to check
     * @return whether or not the tweet matches this filter
     */
    boolean matches(Status s);

    /**
     * Get all the getTerms (strings in basic filters) used in this filter.
     * When we query the Twitter API, we must indicate all the getTerms we are
     * interested in, and this allows us to collect them up for each active query.
     *
     * @return a list of getTerms mentioned in this filter
     */
    List<String> getTerms();
}
