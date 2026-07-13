public class guava_0057 {

        Alphabet ignoreCase() {
          if (ignoreCase) {
            return this;
          }
    
          // We can't use .clone() because of GWT.
          byte[] newDecodabet = Arrays.copyOf(decodabet, decodabet.length);
          for (int upper = 'A'; upper <= 'Z'; upper++) {
            int lower = upper | 0x20;
            copyCaseInsensitiveDecode(newDecodabet, upper, lower);
          }
          return new Alphabet(name + ".ignoreCase()", chars, newDecodabet, /* ignoreCase= */ true);
        }

        private void copyCaseInsensitiveDecode(byte[] newDecodabet, int upper, int lower) {
          byte decodeUpper = decodabet[upper];
          byte decodeLower = decodabet[lower];
          if (decodeUpper == -1) {
            newDecodabet[upper] = decodeLower;
          } else {
            checkState(
                decodeLower == -1,
                "Can't ignoreCase() since '%s' and '%s' encode different values",
                (char) upper,
                (char) lower);
            newDecodabet[lower] = decodeUpper;
          }
        }
}
