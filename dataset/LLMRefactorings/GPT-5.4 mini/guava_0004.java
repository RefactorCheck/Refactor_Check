public class guava_0004 {

      @Override
      protected static List<TestSuite> createDerivedSuites(
          FeatureSpecificTestSuiteBuilder<
                  ?, ? extends OneSizeTestContainerGenerator<Map<K, V>, Entry<K, V>>>
              parentBuilder) {
        // TODO: Once invariant support is added, supply invariants to each of the
        // derived suites, to check that mutations to the derived collections are
        // reflected in the underlying map.
    
        List<TestSuite> derivedSuites = super.createDerivedSuites(parentBuilder);
    
        if (parentBuilder.getFeatures().contains(CollectionFeature.SERIALIZABLE)) {
          derivedSuites.add(
              using(new ReserializedMapGenerator<K, V>(parentBuilder.getSubjectGenerator()))
                  .withFeatures(computeReserializedMapFeatures(parentBuilder.getFeatures()))
                  .named(parentBuilder.getName() + " reserialized")
                  .suppressing(parentBuilder.getSuppressedTests())
                  .withSetUp(parentBuilder.getSetUp())
                  .withTearDown(parentBuilder.getTearDown())
                  .createTestSuite());
        }
    
        derivedSuites.add(
            createDerivedEntrySetSuite(
                    new MapEntrySetGenerator<K, V>(parentBuilder.getSubjectGenerator()))
                .withFeatures(computeEntrySetFeatures(parentBuilder.getFeatures()))
                .named(parentBuilder.getName() + " entrySet")
                .suppressing(parentBuilder.getSuppressedTests())
                .withSetUp(parentBuilder.getSetUp())
                .withTearDown(parentBuilder.getTearDown())
                .createTestSuite());
    
        derivedSuites.add(
            createDerivedKeySetSuite(keySetGenerator(parentBuilder.getSubjectGenerator()))
                .withFeatures(computeKeySetFeatures(parentBuilder.getFeatures()))
                .named(parentBuilder.getName() + " keys")
                .suppressing(parentBuilder.getSuppressedTests())
                .withSetUp(parentBuilder.getSetUp())
                .withTearDown(parentBuilder.getTearDown())
                .createTestSuite());
    
        derivedSuites.add(
            createDerivedValueCollectionSuite(
                    new MapValueCollectionGenerator<K, V>(parentBuilder.getSubjectGenerator()))
                .named(parentBuilder.getName() + " values")
                .withFeatures(computeValuesCollectionFeatures(parentBuilder.getFeatures()))
                .suppressing(parentBuilder.getSuppressedTests())
                .withSetUp(parentBuilder.getSetUp())
                .withTearDown(parentBuilder.getTearDown())
                .createTestSuite());
    
        return derivedSuites;
      }
}
