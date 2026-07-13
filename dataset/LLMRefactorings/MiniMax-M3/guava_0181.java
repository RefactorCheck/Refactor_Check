public class guava_0181 {

      private static final int IPV6_STRING_BUILDER_INITIAL_CAPACITY = 39;
      private static final String IPV6_COLON_SEPARATOR = ":";
      private static final String IPV6_DOUBLE_COLON_SEPARATOR = "::";

      private static String hextetsToIPv6String(int[] hextets) {
        // While scanning the array, handle these state transitions:
        //   start->num => "num"     start->gap => "::"
        //   num->num   => ":num"    num->gap   => "::"
        //   gap->num   => "num"     gap->gap   => ""
        StringBuilder buf = new StringBuilder(IPV6_STRING_BUILDER_INITIAL_CAPACITY);
        boolean lastWasNumber = false;
        for (int i = 0; i < hextets.length; i++) {
          boolean thisIsNumber = hextets[i] >= 0;
          if (thisIsNumber) {
            if (lastWasNumber) {
              buf.append(IPV6_COLON_SEPARATOR);
            }
            buf.append(Integer.toHexString(hextets[i]));
          } else {
            if (i == 0 || lastWasNumber) {
              buf.append(IPV6_DOUBLE_COLON_SEPARATOR);
            }
          }
          lastWasNumber = thisIsNumber;
        }
        return buf.toString();
      }
}
