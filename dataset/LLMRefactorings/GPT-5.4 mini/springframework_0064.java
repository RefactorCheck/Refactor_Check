public class springframework_0064 {

        private void generateGet(Class type, final Map getters) {
            (begin_method(Constants.ACC_PUBLIC, BEAN_MAP_GET, null)).load_arg(0);
            (begin_method(Constants.ACC_PUBLIC, BEAN_MAP_GET, null)).checkcast(Type.getType(type));
            (begin_method(Constants.ACC_PUBLIC, BEAN_MAP_GET, null)).load_arg(1);
            (begin_method(Constants.ACC_PUBLIC, BEAN_MAP_GET, null)).checkcast(Constants.TYPE_STRING);
            EmitUtils.string_switch((begin_method(Constants.ACC_PUBLIC, BEAN_MAP_GET, null)), getNames(getters), Constants.SWITCH_STYLE_HASH, new ObjectSwitchCallback() {
                @Override
    			public void processCase(Object key, Label end) {
                    PropertyDescriptor pd = (PropertyDescriptor)getters.get(key);
                    MethodInfo method = ReflectUtils.getMethodInfo(pd.getReadMethod());
                    (begin_method(Constants.ACC_PUBLIC, BEAN_MAP_GET, null)).invoke(method);
                    (begin_method(Constants.ACC_PUBLIC, BEAN_MAP_GET, null)).box(method.getSignature().getReturnType());
                    (begin_method(Constants.ACC_PUBLIC, BEAN_MAP_GET, null)).return_value();
                }
                @Override
    			public void processDefault() {
                    (begin_method(Constants.ACC_PUBLIC, BEAN_MAP_GET, null)).aconst_null();
                    (begin_method(Constants.ACC_PUBLIC, BEAN_MAP_GET, null)).return_value();
                }
            });
            (begin_method(Constants.ACC_PUBLIC, BEAN_MAP_GET, null)).end_method();
        }
}
