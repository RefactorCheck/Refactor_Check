public class guava_0004 {

      @Override
      protected List<TestSuite> createDerivedSuites(
          FeatureSpecificTestSuiteBuilder<
                  ?, ? extends OneSizeTestContainerGenerator<Map<K, V>, Entry<K, V>>>
              parentBuilder) {
        // TODO: Once invariant support is added, supply invariants to each of the
        // derived suites, to check that mutations to the derived collections are
        // reflected in the underlying map.
    
        List<TestSuite> derivedSuites = super.createDerivedSuites(parentBuilder);
    
        final String name = parentBuilder.getName();
        final Set<Feature<?>> features = parentBuilder.getFeatures();
        final Runnable setUp = parentBuilder.getSetUp();
        final Runnable tearDown = parentBuilder.getTearDown();
        final Set<Method> suppressedTests = parentBuilder.getSuppressedTests();
    
        if (features.contains(CollectionFeature.SERIALIZABLE)) {
          derivedSuites.add(
              using(new ReserializedMapGenerator<K, V>(parentBuilder.getSubjectGenerator()))
                  .withFeatures(computeReserializedMapFeatures(features))
                  .named(name + " reserialized")
                  .suppressing(suppressedTests)
                  .withSetUp(setUp)
                  .withTearDown(tearDown)
                  .createTestSuite());
        }
    
        derivedSuites.add(
            createDerivedEntrySetSuite(
                    new MapEntrySetGenerator<K, V>(parentBuilder.getSubjectGenerator()))
                .withFeatures(computeEntrySetFeatures(features))
                .named(name + " entrySet")
                .suppressing(suppressedTests)
                .withSetUp(setUp)
                .withTearDown(tearDown)
                .createTestSuite());
    
        derivedSuites.add(
            createDerivedKeySetSuite(keySetGenerator(parentBuilder.getSubjectGenerator()))
                .withFeatures(computeKeySetFeatures(features))
                .named(name + " keys")
                .suppressing(suppressedTests)
                .withSetUp(setUp)
                .withTearDown(tearDown)
                .createTestSuite());
    
        derivedSuites.add(
            createDerivedValueCollectionSuite(
                    new MapValueCollectionGenerator<K, V>(parentBuilder.getSubjectGenerator()))
                .named(name + " values")
                .withFeatures(computeValuesCollectionFeatures(features))
                .suppressing(suppressedTests)
                .withSetUp(setUp)
                .withTearDown(tearDown)
                .createTestSuite());
    
        return derivedSuites;
      }
}
