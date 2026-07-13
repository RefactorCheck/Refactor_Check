public class springframework_0280 {

        private void addDelegate(Method m) {
            Method delegate = findDelegate(m);
            final Signature sig = ReflectUtils.getSignature(m);
            Type[] exceptions = TypeUtils.getTypes(m.getExceptionTypes());
            CodeEmitter e = super.begin_method(Constants.ACC_PUBLIC, sig, exceptions);
            e.load_this();
            e.getfield(DELEGATE);
            e.load_args();
            e.invoke_virtual(delegateType, sig);
            e.return_value();
            e.end_method();
        }

        private Method findDelegate(Method m) {
            Method delegate;
            try {
                delegate = delegateImpl.getMethod(m.getName(), m.getParameterTypes());
                if (!delegate.getReturnType().getName().equals(m.getReturnType().getName())) {
                    throw new IllegalArgumentException("Invalid delegate signature " + delegate);
                }
            } catch (NoSuchMethodException e) {
                throw new CodeGenerationException(e);
            }
            return delegate;
        }
}
