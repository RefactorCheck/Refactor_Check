public class guava_0107 {

      static CharMatcher from(BitSet chars, String description) {
        long filter = 0;
        int size = chars.cardinality();
        boolean containsZero = chars.get(0);
        char[] table = buildHashTable(chars, size);
        for (int c = chars.nextSetBit(0); c != -1; c = chars.nextSetBit(c + 1)) {
          filter |= 1L << c;
        }
        return new SmallCharMatcher(table, filter, containsZero, description);
      }

      private static char[] buildHashTable(BitSet chars, int size) {
        char[] table = new char[chooseTableSize(size)];
        int mask = table.length - 1;
        for (int c = chars.nextSetBit(0); c != -1; c = chars.nextSetBit(c + 1)) {
          int index = smear(c) & mask;
          while (true) {
            if (table[index] == 0) {
              table[index] = (char) c;
              break;
            }
            index = (index + 1) & mask;
          }
        }
        return table;
      }
}
