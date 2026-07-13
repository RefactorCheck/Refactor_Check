public class springframework_0283 {
      private static Type getTypeInternal(
          final String descriptorBuffer, final int descriptorBegin, final int descriptorEnd) {
        final char typeDescriptor = descriptorBuffer.charAt(descriptorBegin);
        switch (typeDescriptor) {
          case 'V':
            return VOID_TYPE;
          case 'Z':
            return BOOLEAN_TYPE;
          case 'C':
            return CHAR_TYPE;
          case 'B':
            return BYTE_TYPE;
          case 'S':
            return SHORT_TYPE;
          case 'I':
            return INT_TYPE;
          case 'F':
            return FLOAT_TYPE;
          case 'J':
            return LONG_TYPE;
          case 'D':
            return DOUBLE_TYPE;
          case '[':
            return new Type(ARRAY, descriptorBuffer, descriptorBegin, descriptorEnd);
          case 'L':
            return new Type(OBJECT, descriptorBuffer, descriptorBegin + 1, descriptorEnd - 1);
          case '(':
            return new Type(METHOD, descriptorBuffer, descriptorBegin, descriptorEnd);
          default:
            throw new IllegalArgumentException("Invalid descriptor: " + descriptorBuffer);
        }
      }
}
