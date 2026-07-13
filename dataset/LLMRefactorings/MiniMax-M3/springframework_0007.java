public class springframework_0007 {

    private int comparePrecedenceWithinAspect(Advisor advisor1, Advisor advisor2) {
        boolean oneOrOtherIsAfterAdvice =
                (AspectJAopUtils.isAfterAdvice(advisor1) || AspectJAopUtils.isAfterAdvice(advisor2));
        int adviceDeclarationOrderDelta = getAspectDeclarationOrder(advisor1) - getAspectDeclarationOrder(advisor2);

        boolean higherPrecedenceForFirstDeclared = !oneOrOtherIsAfterAdvice;
        return compareByDelta(adviceDeclarationOrderDelta, higherPrecedenceForFirstDeclared);
    }

    private int compareByDelta(int delta, boolean higherPrecedenceForFirstDeclared) {
        if (delta < 0) {
            return higherPrecedenceForFirstDeclared ? HIGHER_PRECEDENCE : LOWER_PRECEDENCE;
        }
        else if (delta == 0) {
            return SAME_PRECEDENCE;
        }
        else {
            return higherPrecedenceForFirstDeclared ? LOWER_PRECEDENCE : HIGHER_PRECEDENCE;
        }
    }
}
