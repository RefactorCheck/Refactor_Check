private static TestSuite createDescendingSuite(
          FeatureSpecificTestSuiteBuilder<?, ? extends OneSizeTestContainerGenerator<Collection<E>, E>>
              parentBuilder)  {

        TestSetGenerator<E> delegate =
            (TestSetGenerator<E>) parentBuilder.getSubjectGenerator().getInnerGenerator();
    
        List<Feature<?>> features = new ArrayList<>();
        features.add(DESCENDING_VIEW);
        features.addAll(parentBuilder.getFeatures());
    
        return NavigableSetTestSuiteBuilder.using(
                new TestSetGenerator<E>() {
    
                  @Override
                  public SampleElements<E> samples() {
                    return delegate.samples();
                  }
    
                  @Override
                  public E[] createArray(int length) {
                    return delegate.createArray(length);
                  }
    
                  @Override
                  public Iterable<E> order(List<E> insertionOrder) {
                    List<E> list = new ArrayList<>();
                    for (E e : delegate.order(insertionOrder)) {
                      list.add(e);
                    }
                    Collections.reverse(list);
                    return list;
                  }
    
                  @Override
                  public Set<E> create(Object... elements) {
                    NavigableSet<E> navigableSet = (NavigableSet<E>) delegate.create(elements);
                    return navigableSet.descendingSet();
                  }
                })
            .named(parentBuilder.getName() + " descending")
            .withFeatures(features)
            .suppressing(parentBuilder.getSuppressedTests())
            .createTestSuite();
      


      }
