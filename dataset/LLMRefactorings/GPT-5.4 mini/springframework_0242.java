public class springframework_0242 {

      @Override
      public void visitIincInsn(final int varIndex, final int increment) {
        lastBytecodeOffset = code.length;
        // Add the instruction to the bytecode of the method.
        if ((varIndex > 255) || (increment > 127) || (increment < -128)) {
          code.putByte(Constants.WIDE).put12(Opcodes.IINC, varIndex).putShort(increment);
        } else {
          code.putByte(Opcodes.IINC).put11(varIndex, increment);
        }
        // If needed, update the maximum stack size and number of locals, and stack map frames.
        if (currentBasicBlock != null
            && (compute == COMPUTE_ALL_FRAMES || compute == COMPUTE_INSERTED_FRAMES)) {
          currentBasicBlock.frame.execute(Opcodes.IINC, varIndex, null, null);
        }
        if (compute != COMPUTE_NOTHING) {
          if ((varIndex + 1) > maxLocals) {
            maxLocals = (varIndex + 1);
          }
        }
      }
}
