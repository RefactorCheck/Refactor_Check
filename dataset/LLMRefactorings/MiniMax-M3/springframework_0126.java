public class springframework_0126 {

	@Override
	public void generateCode(MethodVisitor mv, CodeFlow cf) {
		// Pseudo: if (!leftOperandValue) { result=false; } else { result=rightOperandValue; }
		Label elseTarget = new Label();
		Label endOfIf = new Label();
		generateOperandCode(getLeftOperand(), mv, cf);
		mv.visitJumpInsn(IFNE, elseTarget);
		mv.visitLdcInsn(0); // FALSE
		mv.visitJumpInsn(GOTO, endOfIf);
		mv.visitLabel(elseTarget);
		generateOperandCode(getRightOperand(), mv, cf);
		mv.visitLabel(endOfIf);
		cf.pushDescriptor(this.exitTypeDescriptor);
	}

	private void generateOperandCode(SpelNodeImpl operand, MethodVisitor mv, CodeFlow cf) {
		cf.enterCompilationScope();
		operand.generateCode(mv, cf);
		cf.unboxBooleanIfNecessary(mv);
		cf.exitCompilationScope();
	}
}
