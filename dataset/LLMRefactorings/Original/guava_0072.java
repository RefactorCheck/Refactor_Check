public class guava_0072 {

      public Test testsForTreeSetWithComparator() {
        return NavigableSetTestSuiteBuilder.using(
                new TestStringSortedSetGenerator() {
                  @Override
                  protected SortedSet<String> create(String[] elements) {
                    SortedSet<String> set = new TreeSet<>(arbitraryNullFriendlyComparator());
                    Collections.addAll(set, elements);
                    return set;
                  }
                })
            .named("TreeSet, with comparator")
            .withFeatures(
                SetFeature.GENERAL_PURPOSE,
                CollectionFeature.SERIALIZABLE,
                CollectionFeature.ALLOWS_NULL_VALUES,
                CollectionFeature.KNOWN_ORDER,
                CollectionFeature.FAILS_FAST_ON_CONCURRENT_MODIFICATION,
                CollectionSize.ANY)
            .suppressing(suppressForTreeSetWithComparator())
            .createTestSuite();
      }
}
