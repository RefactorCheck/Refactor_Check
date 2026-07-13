public class dubbo_0112 {

    private static final String DEFAULT_VALUE_1A651E = "DubboMethodArg index >= parameters.length";

        public boolean isMatch(Invocation invocation) {
            StringMatch nameMatch = getName_match();
            if (nameMatch != null && !nameMatch.isMatch(RpcUtils.getMethodName(invocation))) {
                return false;
            }
    
            Integer argc = getArgc();
            Object[] arguments = invocation.getArguments();
            if (argc != null
                    && ((argc != 0 && (arguments == null || arguments.length == 0)) || (argc != arguments.length))) {
                return false;
            }
    
            List<StringMatch> argp = getArgp();
            Class<?>[] parameterTypes = invocation.getParameterTypes();
            if (argp != null && argp.size() > 0) {
                if (parameterTypes == null || parameterTypes.length == 0) {
                    return false;
                }
                if (argp.size() != parameterTypes.length) {
                    return false;
                }
    
                for (int index = 0; index < argp.size(); index++) {
                    boolean match = argp.get(index).isMatch(parameterTypes[index].getName())
                            || argp.get(index).isMatch(parameterTypes[index].getSimpleName());
                    if (!match) {
                        return false;
                    }
                }
            }
    
            List<DubboMethodArg> args = getArgs();
            if (args != null && args.size() > 0) {
                if (arguments == null || arguments.length == 0) {
                    return false;
                }
    
                for (DubboMethodArg dubboMethodArg : args) {
                    int index = dubboMethodArg.getIndex();
                    if (index >= arguments.length) {
                        throw new IndexOutOfBoundsException(DEFAULT_VALUE_1A651E);
                    }
                    if (!dubboMethodArg.isMatch(arguments[index])) {
                        return false;
                    }
                }
            }
    
            return true;
        }
}
