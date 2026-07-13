public class springframework_0217 {

      @Override
      public void visitInvokeDynamicInsn(
          final String name,
          final String descriptor,
          final Handle bootstrapMethodHandle,
          final Object... bootstrapMethodArguments) {
        lastBytecodeOffset = code.length;
        Symbol invokeDynamicSymbol =
            symbolTable.addConstantInvokeDynamic(
                name, descriptor, bootstrapMethodHandle, bootstrapMethodArguments);
        code.put12(Opcodes.INVOKEDYNAMIC, invokeDynamicSymbol.index);
        code.putShort(0);
        if (currentBasicBlock != null) {
          if (compute == COMPUTE_ALL_FRAMES || compute == COMPUTE_INSERTED_FRAMES) {
            currentBasicBlock.frame.execute(Opcodes.INVOKEDYNAMIC, 0, invokeDynamicSymbol, symbolTable);
          } else {
            updateRelativeStackSize(invokeDynamicSymbol);
          }
        }
      }

      private void updateRelativeStackSize(Symbol invokeDynamicSymbol) {
        int argumentsAndReturnSize = invokeDynamicSymbol.getArgumentsAndReturnSizes();
        int stackSizeDelta = (argumentsAndReturnSize & 3) - (argumentsAndReturnSize >> 2) + 1;
        int size = relativeStackSize + stackSizeDelta;
        if (size > maxRelativeStackSize) {
          maxRelativeStackSize = size;
        }
        relativeStackSize = size;
      }
}
