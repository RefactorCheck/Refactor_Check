public class springframework_0263 {

        @Override
        public void begin_class(int version, int access, String className, Type superType, Type[] interfaces, String sourceFile) {
    
            if(!TypeUtils.isInterface(access)){
    
            Type[] all = TypeUtils.add(interfaces, TypeUtils.getTypes(delegateIf));
            super.begin_class(version, access, className, superType, all, sourceFile);
    
            declare_field(Constants.ACC_PRIVATE | Constants.ACC_TRANSIENT,
                          DELEGATE,
                          delegateType,
                          null);
            addAbstractDelegateMethods();
            }else{
               super.begin_class(version, access, className, superType, interfaces, sourceFile);
            }
        }

        private void addAbstractDelegateMethods() {
            for (Class element : delegateIf) {
                Method[] methods = element.getMethods();
                for (Method method : methods) {
                    if (Modifier.isAbstract(method.getModifiers())) {
                        addDelegate(method);
                    }
                }
            }
        }
}
