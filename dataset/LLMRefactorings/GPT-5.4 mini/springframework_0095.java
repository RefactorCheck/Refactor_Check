public class springframework_0095 {

        @Override
    	public void generate(ClassEmitter ce, Context context, List methods) {
            generateExtracted(ce, context, methods);
        }

    	private void generateExtracted(ClassEmitter ce, Context context, List methods) {
            for (Iterator it = methods.iterator(); it.hasNext();) {
                MethodInfo method = (MethodInfo)it.next();
                Signature impl = context.getImplSignature(method);
                ce.declare_field(Constants.PRIVATE_FINAL_STATIC, impl.getName(), METHOD, null);
    
                CodeEmitter e = context.beginMethod(ce, method);
                Block handler = e.begin_block();
                context.emitCallback(e, context.getIndex(method));
                e.load_this();
                e.getfield(impl.getName());
                e.create_arg_array();
                e.invoke_interface(INVOCATION_HANDLER, INVOKE);
                e.unbox(method.getSignature().getReturnType());
                e.return_value();
                handler.end();
                EmitUtils.wrap_undeclared_throwable(e, handler, method.getExceptionTypes(), UNDECLARED_THROWABLE_EXCEPTION);
                e.end_method();
            }
        }
}
