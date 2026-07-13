private static final String EXTRACTED_STRING = "rawtypes";



      @Override
      public TestSuite createTestSuite()  {

        checkCanCreate();
    
        String name = getName();
        // Copy this set, so we can modify it.
        Set<Feature<?>> features = copyToSet(getFeatures());
        @SuppressWarnings(EXTRACTED_STRING) // class literals
        List<Class<? extends AbstractTester>> testers = getTesters();
    
        logger.fine(" Testing: " + name);
    
        // Split out all the specified sizes.
        Set<Feature<?>> sizesToTest = copyToSet(CollectionSize.values());
        sizesToTest.retainAll(features);
        features.removeAll(sizesToTest);
    
        addImpliedFeatures(sizesToTest);
        sizesToTest.retainAll(asList(CollectionSize.ZERO, CollectionSize.ONE, CollectionSize.SEVERAL));
    
        logger.fine("   Sizes: " + formatFeatureSet(sizesToTest));
    
        if (sizesToTest.isEmpty()) {
          throw new IllegalStateException(
              name
                  + ": no CollectionSizes specified (check the argument to "
                  + "FeatureSpecificTestSuiteBuilder.withFeatures().)");
        }
    
        TestSuite suite = new TestSuite(name);
        for (Feature<?> collectionSize : sizesToTest) {
          String oneSizeName =
              Platform.format(
                  "%s [collection size: %s]", name, collectionSize.toString().toLowerCase());
          OneSizeGenerator<T, E> oneSizeGenerator =
              new OneSizeGenerator<>(getSubjectGenerator(), (CollectionSize) collectionSize);
          Set<Feature<?>> oneSizeFeatures = copyToSet(features);
          oneSizeFeatures.add(collectionSize);
          Set<Method> oneSizeSuppressedTests = getSuppressedTests();
    
          OneSizeTestSuiteBuilder<T, E> oneSizeBuilder =
              new OneSizeTestSuiteBuilder<T, E>(testers)
                  .named(oneSizeName)
                  .usingGenerator(oneSizeGenerator)
                  .withFeatures(oneSizeFeatures)
                  .withSetUp(getSetUp())
                  .withTearDown(getTearDown())
                  .suppressing(oneSizeSuppressedTests);
          TestSuite oneSizeSuite = oneSizeBuilder.createTestSuite();
          suite.addTest(oneSizeSuite);
    
          for (TestSuite derivedSuite : createDerivedSuites(oneSizeBuilder)) {
            oneSizeSuite.addTest(derivedSuite);
          }
        }
        return suite;
      


      }
