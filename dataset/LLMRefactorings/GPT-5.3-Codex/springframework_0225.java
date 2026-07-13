public class springframework_0225 {

        @Override
        public MethodVisitor visitMethod(
            int access, String name, String desc, String signature, String[] exceptions) {
          applyExtractedRefactoring();

          final Method currentMethod = (Method) methods.remove(sig);
          if (currentMethod != null) {
            currentMethodSig = sig;
            return new MethodVisitor(Constants.ASM_API) {
              @Override
              public void visitMethodInsn(
                  int opcode, String owner, String name, String desc, boolean itf) {
                if (opcode == Opcodes.INVOKESPECIAL && currentMethodSig != null) {
                  Signature target = new Signature(name, desc);
                  if (target.equals(currentMethodSig)) {
                    rejected.add(currentMethod);
                  }
                  currentMethodSig = null;
                }
              }
            };
          } else {
            return null;
          }
        }

	private void applyExtractedRefactoring() {
          Signature sig = new Signature(name, desc);
	}
}
