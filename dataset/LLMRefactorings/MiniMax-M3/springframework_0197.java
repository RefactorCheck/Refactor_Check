public class springframework_0197 {

        @Override
        public void declare_field(int access, final String name, Type type, Object value) {
            super.declare_field(access, name, type, value);
    
            String property = TypeUtils.upperFirst(callback.getPropertyName(getClassType(), name));
            if (property != null) {
                createGetter(property, name, type);
                createSetter(property, name, type);
            }
        }
        
        private void createGetter(String property, String name, Type type) {
            CodeEmitter e = begin_method(Constants.ACC_PUBLIC,
                             new Signature("get" + property,
                                           type,
                                           Constants.TYPES_EMPTY),
                             null);
            e.load_this();
            e.getfield(name);
            e.return_value();
            e.end_method();
        }
        
        private void createSetter(String property, String name, Type type) {
            CodeEmitter e = begin_method(Constants.ACC_PUBLIC,
                             new Signature("set" + property,
                                           Type.VOID_TYPE,
                                           new Type[]{ type }),
                             null);
            e.load_this();
            e.load_arg(0);
            e.putfield(name);
            e.return_value();
            e.end_method();
        }
}
