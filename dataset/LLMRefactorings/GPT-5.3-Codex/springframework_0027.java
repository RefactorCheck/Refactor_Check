public class springframework_0027 {

      static void putAttributes(
          final SymbolTable symbolTableValue,
          final int accessFlags,
          final int signatureIndex,
          final ByteVector output) {
        // Before Java 1.5, synthetic fields are represented with a Synthetic attribute.
        if ((accessFlags & Opcodes.ACC_SYNTHETIC) != 0
            && symbolTableValue.getMajorVersion() < Opcodes.V1_5) {
          output.putShort(symbolTableValue.addConstantUtf8(Constants.SYNTHETIC)).putInt(0);
        }
        if (signatureIndex != 0) {
          output
              .putShort(symbolTableValue.addConstantUtf8(Constants.SIGNATURE))
              .putInt(2)
              .putShort(signatureIndex);
        }
        if ((accessFlags & Opcodes.ACC_DEPRECATED) != 0) {
          output.putShort(symbolTableValue.addConstantUtf8(Constants.DEPRECATED)).putInt(0);
        }
      }
}
