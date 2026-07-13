public class springframework_0027 {

      static void putAttributes(
          final SymbolTable symbolTable,
          final int accessFlags,
          final int signatureIndex,
          final ByteVector output) {
        if ((accessFlags & Opcodes.ACC_SYNTHETIC) != 0
            && symbolTable.getMajorVersion() < Opcodes.V1_5) {
          putEmptyAttribute(symbolTable, Constants.SYNTHETIC, output);
        }
        if (signatureIndex != 0) {
          output
              .putShort(symbolTable.addConstantUtf8(Constants.SIGNATURE))
              .putInt(2)
              .putShort(signatureIndex);
        }
        if ((accessFlags & Opcodes.ACC_DEPRECATED) != 0) {
          putEmptyAttribute(symbolTable, Constants.DEPRECATED, output);
        }
      }

      private static void putEmptyAttribute(
          final SymbolTable symbolTable,
          final String name,
          final ByteVector output) {
        output.putShort(symbolTable.addConstantUtf8(name)).putInt(0);
      }
}
