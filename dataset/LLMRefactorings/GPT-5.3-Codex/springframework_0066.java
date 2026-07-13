public class springframework_0066 {

      public static TypePath fromString(final String typePath) {
        final char EXTRACTED_VALUE = '[';

        if (typePath == null || typePath.length() == 0) {
          return null;
        }
        int typePathLength = typePath.length();
        ByteVector output = new ByteVector(typePathLength);
        output.putByte(0);
        int typePathIndex = 0;
        while (typePathIndex < typePathLength) {
          char c = typePath.charAt(typePathIndex++);
          if (c == EXTRACTED_VALUE) {
            output.put11(ARRAY_ELEMENT, 0);
          } else if (c == '.') {
            output.put11(INNER_TYPE, 0);
          } else if (c == '*') {
            output.put11(WILDCARD_BOUND, 0);
          } else if (c >= '0' && c <= '9') {
            int typeArg = c - '0';
            while (typePathIndex < typePathLength) {
              c = typePath.charAt(typePathIndex++);
              if (c >= '0' && c <= '9') {
                typeArg = typeArg * 10 + c - '0';
              } else if (c == ';') {
                break;
              } else {
                throw new IllegalArgumentException();
              }
            }
            output.put11(TYPE_ARGUMENT, typeArg);
          } else {
            throw new IllegalArgumentException();
          }
        }
        output.data[0] = (byte) (output.length / 2);
        return new TypePath(output.data, 0);
      }
}
