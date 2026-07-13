public class guava_0254 {

      static List<TestSuite> createDerivedSuites(SortedMultisetTestSuiteBuilder<E> parentBuilder) {
        List<TestSuite> derivedSuites = new ArrayList<>();
    
        if (!parentBuilder.getFeatures().contains(NoRecurse.DESCENDING)) {
          derivedSuites.add(createDescendingSuite(parentBuilder));
        }
    
        if (parentBuilder.getFeatures().contains(SERIALIZABLE)) {
          derivedSuites.add(createReserializedSuite(parentBuilder));
        }
    
        if (!parentBuilder.getFeatures().contains(NoRecurse.SUBMULTISET)) {
          derivedSuites.add(createSubMultisetSuite(parentBuilder, Bound.NO_BOUND, Bound.EXCLUSIVE));
          derivedSuites.add(createSubMultisetSuite(parentBuilder, Bound.NO_BOUND, Bound.INCLUSIVE));
          derivedSuites.add(createSubMultisetSuite(parentBuilder, Bound.EXCLUSIVE, Bound.NO_BOUND));
          derivedSuites.add(createSubMultisetSuite(parentBuilder, Bound.EXCLUSIVE, Bound.EXCLUSIVE));
          derivedSuites.add(createSubMultisetSuite(parentBuilder, Bound.EXCLUSIVE, Bound.INCLUSIVE));
          derivedSuites.add(createSubMultisetSuite(parentBuilder, Bound.INCLUSIVE, Bound.NO_BOUND));
          derivedSuites.add(createSubMultisetSuite(parentBuilder, Bound.INCLUSIVE, Bound.EXCLUSIVE));
          derivedSuites.add(createSubMultisetSuite(parentBuilder, Bound.INCLUSIVE, Bound.INCLUSIVE));
        }
    
        return derivedSuites;
      }
}
