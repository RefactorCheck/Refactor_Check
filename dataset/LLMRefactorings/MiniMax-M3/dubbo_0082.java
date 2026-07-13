public class dubbo_0082 {

        @SuppressWarnings("unchecked")
        public static Object getMockObject(ExtensionDirector extensionDirector, String mockService, Class serviceType) {
            boolean isDefault = ConfigUtils.isDefault(mockService);
            if (isDefault) {
                mockService = serviceType.getName() + "Mock";
            }
    
            Class<?> mockClass;
            try {
                mockClass = ReflectUtils.forName(mockService);
            } catch (Exception e) {
                Object obj = resolveMockInstance(extensionDirector, mockService, serviceType, isDefault);
                if (obj != null) {
                    return obj;
                }
                throw new IllegalStateException(
                        "Did not find mock class or instance "
                                + mockService
                                + ", please check if there's mock class or instance implementing interface "
                                + serviceType.getName(),
                        e);
            }
            if (mockClass == null || !serviceType.isAssignableFrom(mockClass)) {
                throw new IllegalStateException(
                        "The mock class " + mockClass.getName() + " not implement interface " + serviceType.getName());
            }
    
            try {
                return mockClass.newInstance();
            } catch (InstantiationException e) {
                throw new IllegalStateException("No default constructor from mock class " + mockClass.getName(), e);
            } catch (IllegalAccessException e) {
                throw new IllegalStateException(e);
            }
        }

        private static Object resolveMockInstance(ExtensionDirector extensionDirector, String mockService, Class serviceType, boolean isDefault) {
            if (!isDefault) {
                ExtensionInjector extensionFactory = extensionDirector
                        .getExtensionLoader(ExtensionInjector.class)
                        .getAdaptiveExtension();
                return extensionFactory.getInstance(serviceType, mockService);
            }
            return null;
        }
}
