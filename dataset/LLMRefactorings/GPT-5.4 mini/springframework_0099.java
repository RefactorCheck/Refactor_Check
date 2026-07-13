public class springframework_0099 {

            private static void emitProxy(ClassEmitter ce, final MethodInfo method) {
                int modifiers = Constants.ACC_PUBLIC;
                if ((method.getModifiers() & Constants.ACC_VARARGS) == Constants.ACC_VARARGS) {
                    modifiers |= Constants.ACC_VARARGS;
                }
                final CodeEmitter e = EmitUtils.begin_method(ce, method, modifiers);
                Type returnType = method.getSignature().getReturnType();
                final boolean returns = returnType != Type.VOID_TYPE;
                Local result = null;
                if (returns) {
                    result = e.make_local(returnType);
                    e.zero_or_null(returnType);
                    e.store_local(result);
                }
                e.load_this();
                e.super_getfield("targets", Constants.TYPE_OBJECT_ARRAY);
                final Local result2 = result;
                EmitUtils.process_array(e, Constants.TYPE_OBJECT_ARRAY, type -> {
    			    e.checkcast(Type.getType(iface));
    			    e.load_args();
    			    e.invoke(method);
    			    if (returns) {
    			        e.store_local(result2);
    			    }
    			});
                if (returns) {
                    e.load_local(result);
                }
                e.return_value();
                e.end_method();
            }
}
