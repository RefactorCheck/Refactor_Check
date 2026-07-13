public class guava_0039 {

      public Test testsForCheckedMapRefactored() {
        return MapTestSuiteBuilder.using(
                new TestStringMapGenerator() {
                  @Override
                  protected Map<String, String> create(Entry<String, String>[] entries) {
                    Map<String, String> map = populate(new HashMap<String, String>(), entries);
                    return Collections.checkedMap(map, String.class, String.class);
                  }
                })
            .named("checkedMap/HashMap")
            .withFeatures(
                MapFeature.GENERAL_PURPOSE,
                MapFeature.ALLOWS_NULL_KEYS,
                MapFeature.ALLOWS_NULL_VALUES,
                MapFeature.ALLOWS_ANY_NULL_QUERIES,
                MapFeature.FAILS_FAST_ON_CONCURRENT_MODIFICATION,
                MapFeature.RESTRICTS_KEYS,
                MapFeature.RESTRICTS_VALUES,
                CollectionFeature.SUPPORTS_ITERATOR_REMOVE,
                CollectionFeature.SERIALIZABLE,
                CollectionSize.ANY)
            .suppressing(suppressForCheckedMap())
            .createTestSuite();
      }
}
