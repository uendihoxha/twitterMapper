package filters;

/**
 * Parse a string in the filter language and return the filter.
 * Throws a IncorrectSyntaxException exception on failure.
 *
 * This is a top-down recursive descent parser (a.k.a., LL(1))
 *
 * The really short explanation is that an LL(1) grammar can be parsed by a collection
 * of mutually recursive methods, each of which is able to recognize a single grammar symbol.
 *
 * The grammar (EBNF) for our filter language is:
 *
 * goal    ::= getFilter
 * getFilter    ::= getFilterOR
 * getFilterOR  ::= getFilterAND ( "or" getFilterAND )*
 * getFilterAND ::= getFilterNOT ( "and" getFilterNOT )*
 * getFilterNOT ::= getPrim | "not" getFilterNOT
 * getPrim    ::= word | "(" getFilter ")"
 *
 * The reason for writing it this way is that it respects the "natural" precedence of boolean
 * expressions, where the precedence order (decreasing) is:
 *      parens
 *      not
 *      and
 *      or
 * This allows an expression like:
 *      blue or green and not red or yellow and purple
 * To be parsed like:
 *      blue or (green and (not red)) or (yellow and purple)
 */
public class Parser {
    private static final String LEFT_PARENTHESIS = "(";
    private static final String RIGHT_PARENTHESIS = ")";
    private static final String OR = "or";
    private static final String AND = "and";
    private static final String NOT = "not";
    private final Scanner scanner;

    public Parser(String input) {
        scanner = new Scanner(input);
    }

    public Filter parse() throws IncorrectSyntaxException {
        Filter filter = getFilter();
        if (scanner.peek() != null) {
            throw new IncorrectSyntaxException("Extra stuff at end of input");
        }
        return filter;
    }

    private Filter getFilter() throws IncorrectSyntaxException {
        return getFilterOR();
    }

    private Filter getFilterOR() throws IncorrectSyntaxException {
        Filter sub = getFilterAND();
        String token = scanner.peek();
        while (OR.equals(token)) {
            scanner.advance();
            Filter right = getFilterAND();
            // At this point we have two subexpressions ("sub" on the left and "right" on the right)
            // that are to be connected by "or"
            // TO DO: Construct the appropriate new Filter object
            // The new filter object should be assigned to the variable "sub"
            sub = new OrFilter(sub, right);
            token = scanner.peek();
        }
        return sub;
    }

    private Filter getFilterAND() throws IncorrectSyntaxException {
        Filter sub = getFilterNOT();
        String token = scanner.peek();
        while (AND.equals(token)) {
            scanner.advance();
            Filter right = getFilterNOT();
            // At this point we have two subexpressions ("sub" on the left and "right" on the right)
            // that are to be connected by "and"
            // TO DO: Construct the appropriate new Filter object
            // The new filter object should be assigned to the variable "sub"
            sub = new AndFilter(sub, right);
            token = scanner.peek();
        }
        return sub;
    }

    private Filter getFilterNOT() throws IncorrectSyntaxException {
        String token = scanner.peek();
        if (token.equals(NOT)) {
            scanner.advance();
            Filter sub = getFilterNOT();
            return new NotFilter(sub);
        } else {
            Filter sub = getPrim();
            return sub;
        }
    }

    private Filter getPrim() throws IncorrectSyntaxException {
        String token = scanner.peek();
        if (token.equals(LEFT_PARENTHESIS)) {
            scanner.advance();
            Filter sub = getFilter();
            if (!RIGHT_PARENTHESIS.equals(scanner.peek())) {
                throw new IncorrectSyntaxException("Expected ')'");
            }
            scanner.advance();
            return sub;
        } else {
            Filter sub = new BasicFilter(token);
            scanner.advance();
            return sub;
        }
    }
}
