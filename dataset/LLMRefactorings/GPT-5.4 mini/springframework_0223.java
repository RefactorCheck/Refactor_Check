public class springframework_0223 {

      private void copyBootstrapMethodsRefactored(final ClassReader classReader, final char[] charBuffer) {
        // Find attributOffset of the 'bootstrap_methods' array.
        byte[] inputBytes = classReader.classFileBuffer;
        int currentAttributeOffset = classReader.getFirstAttributeOffset();
        for (int i = classReader.readUnsignedShort(currentAttributeOffset - 2); i > 0; --i) {
          String attributeName = classReader.readUTF8(currentAttributeOffset, charBuffer);
          if (Constants.BOOTSTRAP_METHODS.equals(attributeName)) {
            bootstrapMethodCount = classReader.readUnsignedShort(currentAttributeOffset + 6);
            break;
          }
          currentAttributeOffset += 6 + classReader.readInt(currentAttributeOffset + 2);
        }
        if (bootstrapMethodCount > 0) {
          // Compute the offset and the length of the BootstrapMethods 'bootstrap_methods' array.
          int bootstrapMethodsOffset = currentAttributeOffset + 8;
          int bootstrapMethodsLength = classReader.readInt(currentAttributeOffset + 2) - 2;
          bootstrapMethods = new ByteVector(bootstrapMethodsLength);
          bootstrapMethods.putByteArray(inputBytes, bootstrapMethodsOffset, bootstrapMethodsLength);
    
          // Add each bootstrap method in the symbol table entries.
          int currentOffset = bootstrapMethodsOffset;
          for (int i = 0; i < bootstrapMethodCount; i++) {
            int offset = currentOffset - bootstrapMethodsOffset;
            int bootstrapMethodRef = classReader.readUnsignedShort(currentOffset);
            currentOffset += 2;
            int numBootstrapArguments = classReader.readUnsignedShort(currentOffset);
            currentOffset += 2;
            int hashCode = classReader.readConst(bootstrapMethodRef, charBuffer).hashCode();
            while (numBootstrapArguments-- > 0) {
              int bootstrapArgument = classReader.readUnsignedShort(currentOffset);
              currentOffset += 2;
              hashCode ^= classReader.readConst(bootstrapArgument, charBuffer).hashCode();
            }
            add(new Entry(i, Symbol.BOOTSTRAP_METHOD_TAG, offset, hashCode & 0x7FFFFFFF));
          }
        }
      }
}
