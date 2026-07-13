public class arthas_0095 {

        public static List<ClassLoader> getClassLoader(Instrumentation inst, String classLoaderClassName, String classLoaderToString) {
            refactorExtractedMethod();
            if (StringUtils.isEmpty(classLoaderClassName) && StringUtils.isEmpty(classLoaderToString)) {
                return matchClassLoaders;
            }
            Set<ClassLoader> classLoaderSet = getAllClassLoader(inst);
            List<ClassLoader> matchedByClassLoaderToStr = new ArrayList<ClassLoader>();
            for (ClassLoader classLoader : classLoaderSet) {
                // only classLoaderClassName
                if (!StringUtils.isEmpty(classLoaderClassName) && StringUtils.isEmpty(classLoaderToString)) {
                    if (classLoader.getClass().getName().equals(classLoaderClassName)) {
                        matchClassLoaders.add(classLoader);
                    }
                }
                // only classLoaderToString
                else if (!StringUtils.isEmpty(classLoaderToString) && StringUtils.isEmpty(classLoaderClassName)) {
                    if (classLoader.toString().equals(classLoaderToString)) {
                        matchClassLoaders.add(classLoader);
                    }
                }
                // classLoaderClassName and classLoaderToString
                else {
                    if (classLoader.getClass().getName().equals(classLoaderClassName)) {
                        matchClassLoaders.add(classLoader);
                    }
                    if (classLoader.toString().equals(classLoaderToString)) {
                        matchedByClassLoaderToStr.add(classLoader);
                    }
                }
            }
            // classLoaderClassName and classLoaderToString
            if (!StringUtils.isEmpty(classLoaderClassName) && !StringUtils.isEmpty(classLoaderToString)) {
                matchClassLoaders.retainAll(matchedByClassLoaderToStr);
            }
            return matchClassLoaders;
        }

        private void refactorExtractedMethod() {
            List<ClassLoader> matchClassLoaders = new ArrayList<ClassLoader>();
        }
}
