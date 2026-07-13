public class springframework_0070 {

        private void addWriteMethod(String name, Type type) {
            CodeEmitter e = super.begin_method(Constants.ACC_PUBLIC,
                                               writeMethodSig(name, type.getDescriptor()),
                                               null);
            e.load_this();
            e.dup();
            e.invoke_interface(ENABLED,ENABLED_GET);
            Label skip = e.make_label();
            e.ifnull(skip);
    
            emitCallback(e, name, type);
    
            Label go = e.make_label();
            e.goTo(go);
            e.mark(skip);
            e.load_arg(0);
            e.mark(go);
            e.putfield(name);
            e.return_value();
            e.end_method();
        }

        private void emitCallback(CodeEmitter e, String name, Type type) {
            e.load_this();
            e.invoke_interface(ENABLED,ENABLED_GET);
            e.load_this();
            e.push(name);
            e.load_this();
            e.getfield(name);
            e.load_arg(0);
            e.invoke_interface(CALLBACK, writeCallbackSig(type));
            if (!TypeUtils.isPrimitive(type)) {
                e.checkcast(type);
            }
        }
}
