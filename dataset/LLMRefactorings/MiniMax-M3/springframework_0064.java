public class springframework_0064 {

        private void emitGet(Class type, final Map getters) {
            final CodeEmitter e = begin_method(Constants.ACC_PUBLIC, BEAN_MAP_GET, null);
            e.load_arg(0);
            e.checkcast(Type.getType(type));
            e.load_arg(1);
            e.checkcast(Constants.TYPE_STRING);
            EmitUtils.string_switch(e, getNames(getters), Constants.SWITCH_STYLE_HASH, new ObjectSwitchCallback() {
                @Override
                public void processCase(Object key, Label end) {
                    PropertyDescriptor pd = (PropertyDescriptor)getters.get(key);
                    MethodInfo method = ReflectUtils.getMethodInfo(pd.getReadMethod());
                    e.invoke(method);
                    e.box(method.getSignature().getReturnType());
                    e.return_value();
                }
                @Override
                public void processDefault() {
                    e.aconst_null();
                    e.return_value();
                }
            });
            e.end_method();
        }
}
