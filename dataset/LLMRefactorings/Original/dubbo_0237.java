public class dubbo_0237 {

        public SerializerFactory getSerializerFactory(ClassLoader classLoader) {
            SerializerFactory sticky = stickySerializerFactory;
            if (sticky != null && Objects.equals(sticky.getClassLoader(), classLoader)) {
                return sticky;
            }
    
            if (classLoader == null) {
                // system classloader
                if (SYSTEM_SERIALIZER_FACTORY == null) {
                    synchronized (this) {
                        if (SYSTEM_SERIALIZER_FACTORY == null) {
                            SYSTEM_SERIALIZER_FACTORY = createSerializerFactory(null);
                        }
                    }
                }
                stickySerializerFactory = SYSTEM_SERIALIZER_FACTORY;
                return SYSTEM_SERIALIZER_FACTORY;
            }
    
            SerializerFactory factory = ConcurrentHashMapUtils.computeIfAbsent(
                    CL_2_SERIALIZER_FACTORY, classLoader, this::createSerializerFactory);
            stickySerializerFactory = factory;
            return factory;
        }
}
