public class guava_0172 {

      @Override
      protected List<TestSuite> createDerivedSuites(
          FeatureSpecificTestSuiteBuilder<?, ? extends OneSizeTestContainerGenerator<Collection<E>, E>>
              parentBuilder) {
        List<TestSuite> derivedSuites = new ArrayList<>(super.createDerivedSuites(parentBuilder));
    
        derivedSuites.add(createElementSetTestSuite(parentBuilder));
    
        if (!parentBuilder.getFeatures().contains(NoRecurse.NO_ENTRY_SET)) {
          derivedSuites.add(
              SetTestSuiteBuilder.using(new EntrySetGenerator<E>(parentBuilder.getSubjectGenerator()))
                  .named(getName() + ".entrySet")
                  .withFeatures(computeEntrySetFeatures(parentBuilder.getFeatures()))
                  .suppressing(parentBuilder.getSuppressedTests())
                  .withSetUp(parentBuilder.getSetUp())
                  .withTearDown(parentBuilder.getTearDown())
                  .createTestSuite());
        }
    
        if (parentBuilder.getFeatures().contains(CollectionFeature.SERIALIZABLE)) {
          derivedSuites.add(
              using(new ReserializedMultisetGenerator<E>(parentBuilder.getSubjectGenerator()))
                  .named(getName() + " reserialized")
                  .withFeatures(computeReserializedMultisetFeatures(parentBuilder.getFeatures()))
                  .suppressing(parentBuilder.getSuppressedTests())
                  .withSetUp(parentBuilder.getSetUp())
                  .withTearDown(parentBuilder.getTearDown())
                  .createTestSuite());
        }
        return derivedSuites;
      }
}
