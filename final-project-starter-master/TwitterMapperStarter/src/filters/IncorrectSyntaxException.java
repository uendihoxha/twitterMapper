package filters;

/**
 * The exception thrown when parsing a string fails.
 */
public class IncorrectSyntaxException extends Exception {
    public IncorrectSyntaxException() {
        super();
    }

    public IncorrectSyntaxException(String s) {
        super(s);
    }
}
