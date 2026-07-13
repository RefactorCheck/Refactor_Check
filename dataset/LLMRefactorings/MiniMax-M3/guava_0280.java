public class guava_0280 {

    static Splitter onPatternInternal(CommonPattern separatorPattern) {
        checkNotMatchesEmpty(separatorPattern);
        return new Splitter(
            (splitter, toSplit) -> {
                CommonMatcher matcher = separatorPattern.matcher(toSplit);
                return new SplittingIterator(splitter, toSplit) {
                    @Override
                    int separatorStart(int start) {
                        return matcher.find(start) ? matcher.start() : -1;
                    }

                    @Override
                    int separatorEnd(int separatorPosition) {
                        return matcher.end();
                    }
                };
            });
    }

    private static void checkNotMatchesEmpty(CommonPattern separatorPattern) {
        checkArgument(
            !separatorPattern.matcher("").matches(),
            "The pattern may not match the empty string: %s",
            separatorPattern);
    }
}
