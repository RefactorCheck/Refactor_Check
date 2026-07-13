public class guava_0254 {

    List<TestSuite> createDerivedSuites(SortedMultisetTestSuiteBuilder<E> parentBuilder) {
        List<TestSuite> derivedSuites = new ArrayList<>();

        if (!parentBuilder.getFeatures().contains(NoRecurse.DESCENDING)) {
            derivedSuites.add(createDescendingSuite(parentBuilder));
        }

        if (parentBuilder.getFeatures().contains(SERIALIZABLE)) {
            derivedSuites.add(createReserializedSuite(parentBuilder));
        }

        if (!parentBuilder.getFeatures().contains(NoRecurse.SUBMULTISET)) {
            derivedSuites.addAll(createSubMultisetSuites(parentBuilder));
        }

        return derivedSuites;
    }

    private List<TestSuite> createSubMultisetSuites(SortedMultisetTestSuiteBuilder<E> parentBuilder) {
        List<TestSuite> suites = new ArrayList<>();
        suites.add(createSubMultisetSuite(parentBuilder, Bound.NO_BOUND, Bound.EXCLUSIVE));
        suites.add(createSubMultisetSuite(parentBuilder, Bound.NO_BOUND, Bound.INCLUSIVE));
        suites.add(createSubMultisetSuite(parentBuilder, Bound.EXCLUSIVE, Bound.NO_BOUND));
        suites.add(createSubMultisetSuite(parentBuilder, Bound.EXCLUSIVE, Bound.EXCLUSIVE));
        suites.add(createSubMultisetSuite(parentBuilder, Bound.EXCLUSIVE, Bound.INCLUSIVE));
        suites.add(createSubMultisetSuite(parentBuilder, Bound.INCLUSIVE, Bound.NO_BOUND));
        suites.add(createSubMultisetSuite(parentBuilder, Bound.INCLUSIVE, Bound.EXCLUSIVE));
        suites.add(createSubMultisetSuite(parentBuilder, Bound.INCLUSIVE, Bound.INCLUSIVE));
        return suites;
    }
}
