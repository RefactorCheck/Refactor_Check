public class dubbo_0110 {

    public synchronized void registerInterface(Class<?> clazz) {
        if (!autoTrustSerializeClass) {
            return;
        }

        if (!checkClass(clazz)) {
            return;
        }

        addToAllow(clazz);

        Method[] methodsToExport = clazz.getMethods();

        for (Method method : methodsToExport) {
            checkMethodParameters(method);
            checkMethodReturnType(method);
            checkMethodExceptions(method);
        }
    }

    private void checkMethodParameters(Method method) {
        Class<?>[] parameterTypes = method.getParameterTypes();
        for (Class<?> parameterType : parameterTypes) {
            checkClass(parameterType);
        }

        Type[] genericParameterTypes = method.getGenericParameterTypes();
        for (Type genericParameterType : genericParameterTypes) {
            checkType(genericParameterType);
        }
    }

    private void checkMethodReturnType(Method method) {
        Class<?> returnType = method.getReturnType();
        checkClass(returnType);

        Type genericReturnType = method.getGenericReturnType();
        checkType(genericReturnType);
    }

    private void checkMethodExceptions(Method method) {
        Class<?>[] exceptionTypes = method.getExceptionTypes();
        for (Class<?> exceptionType : exceptionTypes) {
            checkClass(exceptionType);
        }

        Type[] genericExceptionTypes = method.getGenericExceptionTypes();
        for (Type genericExceptionType : genericExceptionTypes) {
            checkType(genericExceptionType);
        }
    }
}
