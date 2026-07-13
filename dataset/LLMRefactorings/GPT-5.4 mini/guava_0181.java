public class guava_0181 {

      private static String hextetsToIPv6StringRefactored(int[] hextets) {
        // While scanning the array, handle these state transitions:
        //   start->num => "num"     start->gap => "::"
        //   num->num   => ":num"    num->gap   => "::"
        //   gap->num   => "num"     gap->gap   => ""
        StringBuilder buf = new StringBuilder(39);
        boolean lastWasNumber = false;
        for (int i = 0; i < hextets.length; i++) {
          boolean thisIsNumber = hextets[i] >= 0;
          if (thisIsNumber) {
            if (lastWasNumber) {
              buf.append(':');
            }
            buf.append(Integer.toHexString(hextets[i]));
          } else {
            if (i == 0 || lastWasNumber) {
              buf.append("::");
            }
          }
          lastWasNumber = thisIsNumber;
        }
        return buf.toString();
      }
}
