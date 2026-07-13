public class springframework_0095 {

        @Override
    	public void generate(ClassEmitter ce, Context context, List methods) {
            for (Iterator it = methods.iterator(); it.hasNext();) {

                Signature impl = context.getImplSignature(((MethodInfo)it.next()));
                ce.declare_field(Constants.PRIVATE_FINAL_STATIC, impl.getName(), METHOD, null);
    
                CodeEmitter e = context.beginMethod(ce, ((MethodInfo)it.next()));
                Block handler = e.begin_block();
                context.emitCallback(e, context.getIndex(((MethodInfo)it.next())));
                e.load_this();
                e.getfield(impl.getName());
                e.create_arg_array();
                e.invoke_interface(INVOCATION_HANDLER, INVOKE);
                e.unbox(((MethodInfo)it.next()).getSignature().getReturnType());
                e.return_value();
                handler.end();
                EmitUtils.wrap_undeclared_throwable(e, handler, ((MethodInfo)it.next()).getExceptionTypes(), UNDECLARED_THROWABLE_EXCEPTION);
                e.end_method();
            }
        }
}
