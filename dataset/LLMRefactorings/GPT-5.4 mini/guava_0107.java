public class guava_0107 {

      static CharMatcher fromRefactored(BitSet chars, String description) {
        // Compute the filter.
        long filter = 0;
        int size = chars.cardinality();
        boolean containsZero = chars.get(0);
        // Compute the hash table.
        char[] table = new char[chooseTableSize(size)];
        int mask = table.length - 1;
        for (int c = chars.nextSetBit(0); c != -1; c = chars.nextSetBit(c + 1)) {
          // Compute the filter at the same time.
          filter |= 1L << c;
          int index = smear(c) & mask;
          while (true) {
            // Check for empty.
            if (table[index] == 0) {
              table[index] = (char) c;
              break;
            }
            // Linear probing.
            index = (index + 1) & mask;
          }
        }
        return new SmallCharMatcher(table, filter, containsZero, description);
      }
}
