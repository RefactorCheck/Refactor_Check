public class nacos_0127 {

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        Map<String, RequestHandler> beansOfType =
            event.getApplicationContext().getBeansOfType(RequestHandler.class);
        Collection<RequestHandler> values = beansOfType.values();
        for (RequestHandler requestHandler : values) {
            
            Class<?> clazz = resolveHandlerClass(requestHandler.getClass());
            if (clazz == null) {
                continue;
            }
            registerTpsControl(clazz);
            
            Class tClass = (Class) ((ParameterizedType) clazz.getGenericSuperclass())
                .getActualTypeArguments()[0];
            
            registerInvokeSource(clazz, tClass);
            
            registryHandlers.putIfAbsent(tClass.getSimpleName(), requestHandler);
        }
    }

    private Class<?> resolveHandlerClass(Class<?> clazz) {
        while (!clazz.getSuperclass().equals(RequestHandler.class)) {
            if (clazz.getSuperclass().equals(Object.class)) {
                return null;
            }
            clazz = clazz.getSuperclass();
        }
        return clazz;
    }

    private void registerTpsControl(Class<?> clazz) {
        try {
            Method method = clazz.getMethod("handle", Request.class, RequestMeta.class);
            if (method.isAnnotationPresent(TpsControl.class)
                && TpsControlConfig.isTpsControlEnabled()) {
                TpsControl tpsControl = method.getAnnotation(TpsControl.class);
                String pointName = tpsControl.pointName();
                ControlManagerCenter.getInstance().getTpsControlManager()
                    .registerTpsPoint(pointName);
            }
        } catch (Exception e) {
            //ignore.
        }
    }

    private void registerInvokeSource(Class<?> clazz, Class<?> tClass) {
        try {
            if (clazz.isAnnotationPresent(InvokeSource.class)) {
                InvokeSource tpsControl = clazz.getAnnotation(InvokeSource.class);
                String[] sources = tpsControl.source();
                if (sources != null && sources.length > 0) {
                    sourceRegistry.put(tClass.getSimpleName(), Sets.newHashSet(sources));
                }
            }
        } catch (Exception e) {
            //ignore.
        }
    }
}
