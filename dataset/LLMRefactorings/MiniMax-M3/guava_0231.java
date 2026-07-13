public class guava_0231 {

      public static void assertEqualIgnoringOrder(Iterable<?> expected, Iterable<?> actual) {
        List<?> exp = copyToList(expected);
        List<?> act = copyToList(actual);
        verifyAllExpectedPresent(exp, act);
        assertTrue("unexpected elements: " + act, act.isEmpty());
      }

      private static void verifyAllExpectedPresent(List<?> exp, List<?> act) {
        String actString = act.toString();
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
      }
}
