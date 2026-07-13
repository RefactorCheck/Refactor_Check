public class springframework_0266 {

      @Override
      public void visitLocalVariable(
          final String name,
          final String descriptor,
          final String signature,
          final Label start,
          final Label end,
          final int index) {
        if (signature != null) {
          if (localVariableTypeTable == null) {
            localVariableTypeTable = new ByteVector();
          }
          ++localVariableTypeTableLength;
          putLocalVariableEntry(localVariableTypeTable, start, end, name, signature, index);
        }
        if (localVariableTable == null) {
          localVariableTable = new ByteVector();
        }
        ++localVariableTableLength;
        putLocalVariableEntry(localVariableTable, start, end, name, descriptor, index);
        if (compute != COMPUTE_NOTHING) {
          char firstDescChar = descriptor.charAt(0);
          int currentMaxLocals = index + (firstDescChar == 'J' || firstDescChar == 'D' ? 2 : 1);
          if (currentMaxLocals > maxLocals) {
            maxLocals = currentMaxLocals;
          }
        }
      }

      private void putLocalVariableEntry(
          final ByteVector vector,
          final Label start,
          final Label end,
          final String name,
          final String descriptorOrSignature,
          final int index) {
        vector
            .putShort(start.bytecodeOffset)
            .putShort(end.bytecodeOffset - start.bytecodeOffset)
            .putShort(symbolTable.addConstantUtf8(name))
            .putShort(symbolTable.addConstantUtf8(descriptorOrSignature))
            .putShort(index);
      }
}
