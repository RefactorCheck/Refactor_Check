public class arthas_0023 {

        private void processResources(CommandProcess process, Instrumentation inst, ClassLoader targetClassLoader) {
            RowAffect affect = new RowAffect();
            List<String> resources = loadResources(targetClassLoader);
            affect.rCnt(resources.size());
    
            process.appendResult(new ClassLoaderModel().setResources(resources));
            process.appendResult(new RowAffectModel(affect));
            process.end();
        }

        private List<String> loadResources(ClassLoader targetClassLoader) {
            List<String> resources = new ArrayList<String>();
            if (targetClassLoader != null) {
                try {
                    Enumeration<URL> urls = targetClassLoader.getResources(resource);
                    while (urls.hasMoreElements()) {
                        URL url = urls.nextElement();
                        resources.add(url.toString());
                    }
                } catch (Throwable e) {
                    logger.warn("get resource failed, resource: {}", resource, e);
                }
            }
            return resources;
        }
}
