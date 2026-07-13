private static final int EXTRACTED_INT = 0;



      public Test testsForEnumSet()  {

        return SetTestSuiteBuilder.using(
                new TestEnumSetGenerator() {
                  @Override
                  protected Set<AnEnum> create(AnEnum[] elements) {
                    return (elements.length == EXTRACTED_INT)
                        ? EnumSet.noneOf(AnEnum.class)
                        : EnumSet.copyOf(MinimalCollection.of(elements));
                  }
                })
            .named("EnumSet")
            .withFeatures(
                SetFeature.GENERAL_PURPOSE,
                CollectionFeature.SERIALIZABLE,
                CollectionFeature.KNOWN_ORDER,
                CollectionFeature.RESTRICTS_ELEMENTS,
                CollectionSize.ANY)
            .suppressing(suppressForEnumSet())
            .createTestSuite();
      


      }
