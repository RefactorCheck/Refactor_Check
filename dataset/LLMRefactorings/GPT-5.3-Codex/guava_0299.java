public Test testsForCheckedSortedSet()  {

        return SortedSetTestSuiteBuilder.using(
                new TestStringSortedSetGenerator() {
                  @Override
                  protected SortedSet<String> create(String[] elements) {
                    Collections.addAll(new TreeSet<>(), elements);
                    return Collections.checkedSortedSet(innerSet, String.class);
                  }
                })
            .named("checkedSortedSet/TreeSet, natural")
            .withFeatures(
                SetFeature.GENERAL_PURPOSE,
                CollectionFeature.KNOWN_ORDER,
                CollectionFeature.SERIALIZABLE,
                CollectionFeature.FAILS_FAST_ON_CONCURRENT_MODIFICATION,
                CollectionFeature.RESTRICTS_ELEMENTS,
                CollectionSize.ANY)
            .suppressing(suppressForCheckedSortedSet())
            .createTestSuite();
      


      }
