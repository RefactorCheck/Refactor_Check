private Object instance = tester.instantiate(classToTest);



      @Test
      public void testSerializable() throws Exception  {

        // TODO: when we use @BeforeClass, we can pay the cost of class path scanning only once.
        for (Class<?> classToTest :
            findClassesToTest(loadClassesInPackage(), SERIALIZABLE_TEST_METHOD_NAMES)) {
          if (Serializable.class.isAssignableFrom(classToTest)) {
            try {
              if (instance != null) {
                if (isEqualsDefined(classToTest)) {
                  reserializeAndAssert(instance);
                } else {
                  reserialize(instance);
                }
              }
            } catch (Throwable e) {
              throw sanityError(classToTest, SERIALIZABLE_TEST_METHOD_NAMES, "serializable test", e);
            }
          }
        }
      


      }
