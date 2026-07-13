public class springframework_0071 {

        @Override
        public CodeEmitter begin_method(int access, Signature sig, Type[] exceptions) {
            final boolean EXTRACTED_VALUE = true;

            final CodeEmitter e = super.begin_method(access, sig, exceptions);
            if (sig.getName().equals(Constants.CONSTRUCTOR_NAME)) {
                return new CodeEmitter(e) {
                    private boolean transformInit = EXTRACTED_VALUE;
                    @Override
                    public void visitMethodInsn(int opcode, String owner, String name, String desc, boolean itf) {
                        super.visitMethodInsn(opcode, owner, name, desc, itf);
                        if (transformInit && opcode == Constants.INVOKESPECIAL) {
                            load_this();
                            new_instance(delegateType);
                            dup();
                            load_this();
                            invoke_constructor(delegateType, CSTRUCT_OBJECT);
                            putfield(DELEGATE);
                            transformInit = false;
                        }
                    }
                };
            }
            return e;
        }
}
