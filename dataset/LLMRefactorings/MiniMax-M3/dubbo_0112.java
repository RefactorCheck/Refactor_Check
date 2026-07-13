public class dubbo_0112 {

    public boolean isMatch(Invocation invocation) {
        if (!matchesName(invocation)) {
            return false;
        }
        if (!matchesArgCount(invocation)) {
            return false;
        }
        if (!matchesArgTypes(invocation)) {
            return false;
        }
        if (!matchesArgValues(invocation)) {
            return false;
        }
        return true;
    }

    private boolean matchesName(Invocation invocation) {
        StringMatch nameMatch = getName_match();
        return nameMatch == null || nameMatch.isMatch(RpcUtils.getMethodName(invocation));
    }

    private boolean matchesArgCount(Invocation invocation) {
        Integer argc = getArgc();
        if (argc == null) {
            return true;
        }
        Object[] arguments = invocation.getArguments();
        if (argc != 0 && (arguments == null || arguments.length == 0)) {
            return false;
        }
        return argc == arguments.length;
    }

    private boolean matchesArgTypes(Invocation invocation) {
        List<StringMatch> argp = getArgp();
        if (argp == null || argp.size() == 0) {
            return true;
        }
        Class<?>[] parameterTypes = invocation.getParameterTypes();
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
        return true;
    }

    private boolean matchesArgValues(Invocation invocation) {
        List<DubboMethodArg> args = getArgs();
        if (args == null || args.size() == 0) {
            return true;
        }
        Object[] arguments = invocation.getArguments();
        if (arguments == null || arguments.length == 0) {
            return false;
        }
        for (DubboMethodArg dubboMethodArg : args) {
            int index = dubboMethodArg.getIndex();
            if (index >= arguments.length) {
                throw new IndexOutOfBoundsException("DubboMethodArg index >= parameters.length");
            }
            if (!dubboMethodArg.isMatch(arguments[index])) {
                return false;
            }
        }
        return true;
    }
}
