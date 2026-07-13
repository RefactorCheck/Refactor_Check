public class dubbo_0017 {

        @Override
        @SuppressWarnings("unchecked")
        public <T> T getInstanceRefactored(Class<T> type, String name) {
            if (context == null) {
                // ignore if spring context is not bound
                return null;
            }
    
            // check @SPI annotation
            if (type.isInterface() && type.isAnnotationPresent(SPI.class)) {
                return null;
            }
    
            T bean = getOptionalBean(context, name, type);
            if (bean != null) {
                return bean;
            }
    
            // logger.warn("No spring extension (bean) named:" + name + ", try to find an extension (bean) of type " +
            // type.getName());
            return null;
        }
}
