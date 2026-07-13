public class guava_0004 {

      @Override
      protected List<TestSuite> createDerivedSuites(
          FeatureSpecificTestSuiteBuilder<
                  ?, ? extends OneSizeTestContainerGenerator<Map<K, V>, Entry<K, V>>>
              suiteBuilder) {
        List<TestSuite> derivedSuites = super.createDerivedSuites(suiteBuilder);

        if (suiteBuilder.getFeatures().contains(CollectionFeature.SERIALIZABLE)) {
          derivedSuites.add(
              using(new ReserializedMapGenerator<K, V>(suiteBuilder.getSubjectGenerator()))
                  .withFeatures(computeReserializedMapFeatures(suiteBuilder.getFeatures()))
                  .named(suiteBuilder.getName() + " reserialized")
                  .suppressing(suiteBuilder.getSuppressedTests())
                  .withSetUp(suiteBuilder.getSetUp())
                  .withTearDown(suiteBuilder.getTearDown())
                  .createTestSuite());
        }

        derivedSuites.add(
            createDerivedEntrySetSuite(
                    new MapEntrySetGenerator<K, V>(suiteBuilder.getSubjectGenerator()))
                .withFeatures(computeEntrySetFeatures(suiteBuilder.getFeatures()))
                .named(suiteBuilder.getName() + " entrySet")
                .suppressing(suiteBuilder.getSuppressedTests())
                .withSetUp(suiteBuilder.getSetUp())
                .withTearDown(suiteBuilder.getTearDown())
                .createTestSuite());

        derivedSuites.add(
            createDerivedKeySetSuite(keySetGenerator(suiteBuilder.getSubjectGenerator()))
                .withFeatures(computeKeySetFeatures(suiteBuilder.getFeatures()))
                .named(suiteBuilder.getName() + " keys")
                .suppressing(suiteBuilder.getSuppressedTests())
                .withSetUp(suiteBuilder.getSetUp())
                .withTearDown(suiteBuilder.getTearDown())
                .createTestSuite());

        derivedSuites.add(
            createDerivedValueCollectionSuite(
                    new MapValueCollectionGenerator<K, V>(suiteBuilder.getSubjectGenerator()))
                .named(suiteBuilder.getName() + " values")
                .withFeatures(computeValuesCollectionFeatures(suiteBuilder.getFeatures()))
                .suppressing(suiteBuilder.getSuppressedTests())
                .withSetUp(suiteBuilder.getSetUp())
                .withTearDown(suiteBuilder.getTearDown())
                .createTestSuite());

        return derivedSuites;
      }
}
