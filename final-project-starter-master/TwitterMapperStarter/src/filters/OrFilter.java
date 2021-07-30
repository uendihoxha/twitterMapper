package filters;

import twitter4j.Status;

import java.util.List;

public class OrFilter implements Filter {
    private final Filter child1;
    private final Filter child2;

    public OrFilter(Filter child1, Filter child2) {
        this.child1 = child1;
        this.child2 = child2;
    }

    @Override
    public boolean matches(Status status) {
        return (child1.matches(status) || child2.matches(status));
    }

    @Override
    public List<String> getTerms() {
        List<String> terms = child1.getTerms();
        terms.addAll(child2.getTerms());
        return terms;
    }


    public String toString() {
        return "(" + child1.toString() + " or " + child2.toString() + ")";
    }
}
