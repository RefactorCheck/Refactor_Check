public class springframework_0098 {

        private static void invokeSwitchHelper(final CodeEmitter e, List members, final int arg, final Type base) {
            invokeSwitchHelperExtracted(e, members, arg, base);
        }

        private static void invokeSwitchHelperExtracted(final CodeEmitter e, List members, final int arg, final Type base) {
            final List info = CollectionUtils.transform(members, MethodInfoTransformer.getInstance());
            final Label illegalArg = e.make_label();
            Block block = e.begin_block();
            e.process_switch(getIntRange(info.size()), new ProcessSwitchCallback() {
                @Override
    			public void processCase(int key, Label end) {
                    MethodInfo method = (MethodInfo)info.get(key);
                    Type[] types = method.getSignature().getArgumentTypes();
                    for (int i = 0; i < types.length; i++) {
                        e.load_arg(arg);
                        e.aaload(i);
                        e.unbox(types[i]);
                    }
                    // TODO: change method lookup process so MethodInfo will already reference base
                    // instead of superclass when superclass method is inaccessible
                    e.invoke(method, base);
                    if (!TypeUtils.isConstructor(method)) {
                        e.box(method.getSignature().getReturnType());
                    }
                    e.return_value();
                }
                @Override
    			public void processDefault() {
                    e.goTo(illegalArg);
                }
            });
            block.end();
            EmitUtils.wrap_throwable(block, INVOCATION_TARGET_EXCEPTION);
            e.mark(illegalArg);
            e.throw_exception(ILLEGAL_ARGUMENT_EXCEPTION, "Cannot find matching method/constructor");
        }
}
