public class springframework_0161 {

      public Object readConst(final int constantPoolEntryIndex, final char[] charBuffer) {
        int cpInfoOffsetRenamed = cpInfoOffsets[constantPoolEntryIndex];
        switch (classFileBuffer[cpInfoOffsetRenamed - 1]) {
          case Symbol.CONSTANT_INTEGER_TAG:
            return readInt(cpInfoOffsetRenamed);
          case Symbol.CONSTANT_FLOAT_TAG:
            return Float.intBitsToFloat(readInt(cpInfoOffsetRenamed));
          case Symbol.CONSTANT_LONG_TAG:
            return readLong(cpInfoOffsetRenamed);
          case Symbol.CONSTANT_DOUBLE_TAG:
            return Double.longBitsToDouble(readLong(cpInfoOffsetRenamed));
          case Symbol.CONSTANT_CLASS_TAG:
            return Type.getObjectType(readUTF8(cpInfoOffsetRenamed, charBuffer));
          case Symbol.CONSTANT_STRING_TAG:
            return readUTF8(cpInfoOffsetRenamed, charBuffer);
          case Symbol.CONSTANT_METHOD_TYPE_TAG:
            return Type.getMethodType(readUTF8(cpInfoOffsetRenamed, charBuffer));
          case Symbol.CONSTANT_METHOD_HANDLE_TAG:
            int referenceKind = readByte(cpInfoOffsetRenamed);
            int referenceCpInfoOffset = cpInfoOffsets[readUnsignedShort(cpInfoOffsetRenamed + 1)];
            int nameAndTypeCpInfoOffset = cpInfoOffsets[readUnsignedShort(referenceCpInfoOffset + 2)];
            String owner = readClass(referenceCpInfoOffset, charBuffer);
            String name = readUTF8(nameAndTypeCpInfoOffset, charBuffer);
            String descriptor = readUTF8(nameAndTypeCpInfoOffset + 2, charBuffer);
            boolean isInterface =
                classFileBuffer[referenceCpInfoOffset - 1] == Symbol.CONSTANT_INTERFACE_METHODREF_TAG;
            return new Handle(referenceKind, owner, name, descriptor, isInterface);
          case Symbol.CONSTANT_DYNAMIC_TAG:
            return readConstantDynamic(constantPoolEntryIndex, charBuffer);
          default:
            throw new IllegalArgumentException();
        }
      }
}
