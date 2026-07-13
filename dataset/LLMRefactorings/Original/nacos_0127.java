public class nacos_0127 {

        @Override
        public void onApplicationEvent(ContextRefreshedEvent event) {
            Map<String, RequestHandler> beansOfType =
                event.getApplicationContext().getBeansOfType(RequestHandler.class);
            Collection<RequestHandler> values = beansOfType.values();
            for (RequestHandler requestHandler : values) {
                
                Class<?> clazz = requestHandler.getClass();
                boolean skip = false;
                while (!clazz.getSuperclass().equals(RequestHandler.class)) {
                    if (clazz.getSuperclass().equals(Object.class)) {
                        skip = true;
                        break;
                    }
                    clazz = clazz.getSuperclass();
                }
                if (skip) {
                    continue;
                }
                //register tps control.
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
                
                Class tClass = (Class) ((ParameterizedType) clazz.getGenericSuperclass())
                    .getActualTypeArguments()[0];
                
                //register invoke source.
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
                
                registryHandlers.putIfAbsent(tClass.getSimpleName(), requestHandler);
            }
        }
}
