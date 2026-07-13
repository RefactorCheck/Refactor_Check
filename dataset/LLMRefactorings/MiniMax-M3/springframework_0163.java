public class springframework_0163 {

        private void generatePut(Class type, final Map setters) {
            final CodeEmitter e = begin_method(Constants.ACC_PUBLIC, BEAN_MAP_PUT, null);
            e.load_arg(0);
            e.checkcast(Type.getType(type));
            e.load_arg(1);
            e.checkcast(Constants.TYPE_STRING);
            EmitUtils.string_switch(e, getNames(setters), Constants.SWITCH_STYLE_HASH, new ObjectSwitchCallback() {
                @Override
    			public void processCase(Object key, Label end) {
                    PropertyDescriptor pd = (PropertyDescriptor)setters.get(key);
                    emitReadValue(e, pd);
                    e.swap();
                    e.load_arg(2);
                    MethodInfo write = ReflectUtils.getMethodInfo(pd.getWriteMethod());
                    e.unbox(write.getSignature().getArgumentTypes()[0]);
                    e.invoke(write);
                    e.return_value();
                }
                @Override
    			public void processDefault() {
                    // fall-through
                }
            });
            e.aconst_null();
            e.return_value();
            e.end_method();
        }

        private void emitReadValue(CodeEmitter e, PropertyDescriptor pd) {
            if (pd.getReadMethod() == null) {
                e.aconst_null();
            } else {
                MethodInfo read = ReflectUtils.getMethodInfo(pd.getReadMethod());
                e.dup();
                e.invoke(read);
                e.box(read.getSignature().getReturnType());
            }
        }
}
