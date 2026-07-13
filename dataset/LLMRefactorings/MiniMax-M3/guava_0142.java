public class guava_0142 {

      @Test
      public void testSerializable() throws Exception {
        for (Class<?> classToTest :
            findClassesToTest(loadClassesInPackage(), SERIALIZABLE_TEST_METHOD_NAMES)) {
          if (Serializable.class.isAssignableFrom(classToTest)) {
            testSerializableClass(classToTest);
          }
        }
      }

      private void testSerializableClass(Class<?> classToTest) {
        try {
          Object instance = tester.instantiate(classToTest);
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
