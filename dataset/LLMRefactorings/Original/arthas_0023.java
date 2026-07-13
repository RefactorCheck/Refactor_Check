public class arthas_0023 {

        private void processResources(CommandProcess process, Instrumentation inst, ClassLoader targetClassLoader) {
            RowAffect affect = new RowAffect();
            int rowCount = 0;
            List<String> resources = new ArrayList<String>();
            if (targetClassLoader != null) {
                try {
                    Enumeration<URL> urls = targetClassLoader.getResources(resource);
                    while (urls.hasMoreElements()) {
                        URL url = urls.nextElement();
                        resources.add(url.toString());
                        rowCount++;
                    }
                } catch (Throwable e) {
                    logger.warn("get resource failed, resource: {}", resource, e);
                }
            }
            affect.rCnt(rowCount);
    
            process.appendResult(new ClassLoaderModel().setResources(resources));
            process.appendResult(new RowAffectModel(affect));
            process.end();
        }
}
