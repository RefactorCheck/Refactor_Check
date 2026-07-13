public class springframework_0054 {

        @Override
    	public CodeEmitter begin_method(int access, Signature sig, Type[] exceptions) {
            return new CodeEmitter(super.begin_method(access, sig, exceptions)) {
                @Override
    			public void visitFieldInsn(int opcode, String owner, String name, String desc) {
                    Type towner = TypeUtils.fromInternalName(owner);
                    switch (opcode) {
                    case Constants.GETFIELD:
                        if (filter.acceptRead(towner, name)) {
                            invoke_virtual(towner, readMethodSig(name, desc));
                            return;
                        }
                        break;
                    case Constants.PUTFIELD:
                        if (filter.acceptWrite(towner, name)) {
                            invoke_virtual(towner, writeMethodSig(name, desc));
                            return;
                        }
                        break;
                    }
                    super.visitFieldInsn(opcode, owner, name, desc);
                }
            };
        }
}
