public class dubbo_0237 {

        public SerializerFactory getSerializerFactory(ClassLoader classLoader) {
            SerializerFactory sticky = stickySerializerFactory;
            if (sticky != null && Objects.equals(sticky.getClassLoader(), classLoader)) {
                return sticky;
            }

            SerializerFactory factory = (classLoader == null)
                    ? getSystemSerializerFactory()
                    : getOrCreateSerializerFactory(classLoader);
            stickySerializerFactory = factory;
            return factory;
        }

        private SerializerFactory getSystemSerializerFactory() {
            if (SYSTEM_SERIALIZER_FACTORY == null) {
                synchronized (this) {
                    if (SYSTEM_SERIALIZER_FACTORY == null) {
                        SYSTEM_SERIALIZER_FACTORY = createSerializerFactory(null);
                    }
                }
            }
            return SYSTEM_SERIALIZER_FACTORY;
        }

        private SerializerFactory getOrCreateSerializerFactory(ClassLoader classLoader) {
            return ConcurrentHashMapUtils.computeIfAbsent(
                    CL_2_SERIALIZER_FACTORY, classLoader, this::createSerializerFactory);
        }
}
