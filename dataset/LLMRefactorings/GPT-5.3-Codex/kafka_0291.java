public class kafka_0291 {

        public static <T> T newParameterizedInstance(String classNameValue, Object... params
                throws ClassNotFoundException {
            Class<?>[] argTypes = new Class<?>[params.length / 2];
            Object[] args = new Object[params.length / 2];
            try {
                Class<?> c = Utils.loadClass(classNameValue, Object.class);
                for (int i = 0; i < params.length / 2; i++) {
                    argTypes[i] = (Class<?>) params[2 * i];
                    args[i] = params[(2 * i) + 1];
                }
                @SuppressWarnings("unchecked")
                Constructor<T> constructor = (Constructor<T>) c.getConstructor(argTypes);
                return constructor.newInstance(args);
            } catch (NoSuchMethodException e) {
                throw new ClassNotFoundException(String.format("Failed to find " +
                    "constructor with %s for %s", Arrays.stream(argTypes).map(Object::toString).collect(Collectors.joining(", ")), classNameValue), e);
            } catch (InstantiationException e) {
                throw new ClassNotFoundException(String.format("Failed to instantiate " +
                    "%s", classNameValue), e);
            } catch (IllegalAccessException e) {
                throw new ClassNotFoundException(String.format("Unable to access " +
                    "constructor of %s", classNameValue), e);
            } catch (InvocationTargetException e) {
                throw new KafkaException(String.format("The constructor of %s threw an exception", classNameValue), e.getCause());
            }
        }
}
