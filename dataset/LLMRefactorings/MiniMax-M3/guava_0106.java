public class guava_0106 {

    public Test testsForAbstractSet() {
        return SetTestSuiteBuilder.using(
                new TestStringSetGenerator() {
                    @Override
                    protected Set<String> create(String[] elements) {
                        return createAbstractSet(dedupe(elements));
                    }
                })
            .named("AbstractSet")
            .withFeatures(
                CollectionFeature.NONE,
                CollectionFeature.ALLOWS_NULL_VALUES,
                CollectionFeature.KNOWN_ORDER, // in this case, anyway
                CollectionSize.ANY)
            .suppressing(suppressForAbstractSet())
            .createTestSuite();
    }

    private static Set<String> createAbstractSet(final String[] deduped) {
        return new AbstractSet<String>() {
            @Override
            public int size() {
                return deduped.length;
            }

            @Override
            public Iterator<String> iterator() {
                return MinimalCollection.of(deduped).iterator();
            }
        };
    }
}
