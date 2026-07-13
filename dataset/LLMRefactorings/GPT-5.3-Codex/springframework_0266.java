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
          localVariableTypeTable
              .putShort(start.bytecodeOffset)
              .putShort(end.bytecodeOffset - start.bytecodeOffset)
              .putShort(symbolTable.addConstantUtf8(name))
              .putShort(symbolTable.addConstantUtf8(signature))
              .putShort(index);
        }
        if (localVariableTable == null) {
          localVariableTable = new ByteVector();
        }
        ++localVariableTableLength;
        localVariableTable
            .putShort(start.bytecodeOffset)
            .putShort(end.bytecodeOffset - start.bytecodeOffset)
            .putShort(symbolTable.addConstantUtf8(name))
            .putShort(symbolTable.addConstantUtf8(descriptor))
            .putShort(index);
        if (compute != COMPUTE_NOTHING) {
          char firstDescCharRenamed = descriptor.charAt(0);
          int currentMaxLocals = index + (firstDescCharRenamed == 'J' || firstDescCharRenamed == 'D' ? 2 : 1);
          if (currentMaxLocals > maxLocals) {
            maxLocals = currentMaxLocals;
          }
        }
      }
}
