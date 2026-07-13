public class guava_0241 {

      public static void assertEqualInOrderRefactored(Iterable<?> expected, Iterable<?> actual) {
        Iterator<?> expectedIter = expected.iterator();
        Iterator<?> actualIter = actual.iterator();
    
        while (expectedIter.hasNext() && actualIter.hasNext()) {
          if (!Objects.equals(expectedIter.next(), actualIter.next())) {
            fail(
                "contents were not equal and in the same order: "
                    + "expected = "
                    + expected
                    + ", actual = "
                    + actual);
          }
        }
    
        if (expectedIter.hasNext() || actualIter.hasNext()) {
          // actual either had too few or too many elements
          fail(
              "contents were not equal and in the same order: "
                  + "expected = "
                  + expected
                  + ", actual = "
                  + actual);
        }
      }
}
