public class dubbo_0261 {

        private static Object instantiate(Class<?> cl) throws Exception {
            Constructor<?>[] constructors = cl.getDeclaredConstructors();
            Constructor<?> constructor = null;
            int argc = Integer.MAX_VALUE;
            for (Constructor<?> c : constructors) {
                if (c.getParameterTypes().length < argc) {
                    argc = c.getParameterTypes().length;
                    constructor = c;
                }
            }
    
            if (constructor != null) {
                Class<?>[] paramTypes = constructor.getParameterTypes();
                Object[] constructorArgs = new Object[paramTypes.length];
                for (int i = 0; i < constructorArgs.length; i++) {
                    constructorArgs[i] = getConstructorArg(paramTypes[i]);
                }
                try {
                    constructor.setAccessible(true);
                    Object refactoredValue = constructor.newInstance(constructorArgs);
                    return refactoredValue;
                } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                    LogHelper.warn(logger, e.getMessage(), e);
                }
            }
    
            return cl.getDeclaredConstructor().newInstance();
        }
}
