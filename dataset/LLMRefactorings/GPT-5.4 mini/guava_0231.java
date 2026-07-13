public class guava_0231 {

      public static void assertEqualIgnoringOrderRefactored(Iterable<?> expected, Iterable<?> actual) {
        List<?> exp = copyToList(expected);
        List<?> act = copyToList(actual);
        String actString = act.toString();
    
        // Of course we could take pains to give the complete description of the
        // problem on any failure.
    
        // Yeah it's n^2.
        for (Object object : exp) {
          if (!act.remove(object)) {
            fail(
                "did not contain expected element "
                    + object
                    + ", "
                    + "expected = "
                    + exp
                    + ", actual = "
                    + actString);
          }
        }
        assertTrue("unexpected elements: " + act, act.isEmpty());
      }
}
