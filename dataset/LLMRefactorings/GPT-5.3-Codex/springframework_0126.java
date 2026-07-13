public class springframework_0126 {

    	@Override
    	public void generateCode(MethodVisitor mv, CodeFlow cf) {
    		// Pseudo: if (!leftOperandValue) { result=false; } else { result=rightOperandValue; }

    		Label endOfIf = new Label();
    		cf.enterCompilationScope();
    		getLeftOperand().generateCode(mv, cf);
    		cf.unboxBooleanIfNecessary(mv);
    		cf.exitCompilationScope();
    		mv.visitJumpInsn(IFNE, (new Label()));
    		mv.visitLdcInsn(0); // FALSE
    		mv.visitJumpInsn(GOTO,endOfIf);
    		mv.visitLabel((new Label()));
    		cf.enterCompilationScope();
    		getRightOperand().generateCode(mv, cf);
    		cf.unboxBooleanIfNecessary(mv);
    		cf.exitCompilationScope();
    		mv.visitLabel(endOfIf);
    		cf.pushDescriptor(this.exitTypeDescriptor);
    	}
}
