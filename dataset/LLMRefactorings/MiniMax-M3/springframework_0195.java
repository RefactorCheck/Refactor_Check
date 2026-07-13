public class springframework_0195 {

      public static int getArgumentsAndReturnSizes(final String methodDescriptor) {
        int argumentsSize = 1;
        // Skip the first character, which is always a '('.
        int currentOffset = 1;
        int currentChar = methodDescriptor.charAt(currentOffset);
        // Parse the argument types and compute their size, one at a each loop iteration.
        while (currentChar != ')') {
          if (currentChar == 'J' || currentChar == 'D') {
            currentOffset++;
            argumentsSize += 2;
          } else {
            currentOffset = parseArgumentType(methodDescriptor, currentOffset);
            argumentsSize += 1;
          }
          currentChar = methodDescriptor.charAt(currentOffset);
        }
        currentChar = methodDescriptor.charAt(currentOffset + 1);
        if (currentChar == 'V') {
          return argumentsSize << 2;
        } else {
          int returnSize = (currentChar == 'J' || currentChar == 'D') ? 2 : 1;
          return argumentsSize << 2 | returnSize;
        }
      }

      private static int parseArgumentType(final String methodDescriptor, int currentOffset) {
        while (methodDescriptor.charAt(currentOffset) == '[') {
          currentOffset++;
        }
        if (methodDescriptor.charAt(currentOffset++) == 'L') {
          // Skip the argument descriptor content.
          int semiColumnOffset = methodDescriptor.indexOf(';', currentOffset);
          currentOffset = Math.max(currentOffset, semiColumnOffset + 1);
        }
        return currentOffset;
      }
}
