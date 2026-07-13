public Test testsForCheckedSortedMap()  {


        return testsForCheckedSortedMapRefactor();


      }



      public Test testsForCheckedSortedMapRefactor()  {

        return SortedMapTestSuiteBuilder.using(
                new TestStringSortedMapGenerator() {
                  @Override
                  protected SortedMap<String, String> create(Entry<String, String>[] entries) {
                    SortedMap<String, String> map = populate(new TreeMap<String, String>(), entries);
                    return Collections.checkedSortedMap(map, String.class, String.class);
                  }
                })
            .named("checkedSortedMap/TreeMap, natural")
            .withFeatures(
                MapFeature.GENERAL_PURPOSE,
                MapFeature.ALLOWS_NULL_VALUES,
                MapFeature.FAILS_FAST_ON_CONCURRENT_MODIFICATION,
                MapFeature.RESTRICTS_KEYS,
                MapFeature.RESTRICTS_VALUES,
                CollectionFeature.KNOWN_ORDER,
                CollectionFeature.SUPPORTS_ITERATOR_REMOVE,
                CollectionFeature.SERIALIZABLE,
                CollectionSize.ANY)
            .suppressing(suppressForCheckedSortedMap())
            .createTestSuite();
      


      }
