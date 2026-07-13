public class arthas_0095 {

        public static List<ClassLoader> getClassLoader(Instrumentation inst, String classLoaderClassName, String classLoaderToString) {
            List<ClassLoader> matchClassLoaders = new ArrayList<ClassLoader>();
            if (StringUtils.isEmpty(classLoaderClassName) && StringUtils.isEmpty(classLoaderToString)) {
                return matchClassLoaders;
            }
            Set<ClassLoader> classLoaderSet = getAllClassLoader(inst);
            List<ClassLoader> matchedByClassLoaderToStr = new ArrayList<ClassLoader>();
            for (ClassLoader classLoader : classLoaderSet) {
                matchClassLoader(classLoader, classLoaderClassName, classLoaderToString, matchClassLoaders, matchedByClassLoaderToStr);
            }
            if (!StringUtils.isEmpty(classLoaderClassName) && !StringUtils.isEmpty(classLoaderToString)) {
                matchClassLoaders.retainAll(matchedByClassLoaderToStr);
            }
            return matchClassLoaders;
        }

        private static void matchClassLoader(ClassLoader classLoader, String classLoaderClassName, String classLoaderToString,
                                             List<ClassLoader> matchClassLoaders, List<ClassLoader> matchedByClassLoaderToStr) {
            if (!StringUtils.isEmpty(classLoaderClassName) && StringUtils.isEmpty(classLoaderToString)) {
                if (classLoader.getClass().getName().equals(classLoaderClassName)) {
                    matchClassLoaders.add(classLoader);
                }
            } else if (!StringUtils.isEmpty(classLoaderToString) && StringUtils.isEmpty(classLoaderClassName)) {
                if (classLoader.toString().equals(classLoaderToString)) {
                    matchClassLoaders.add(classLoader);
                }
            } else {
                if (classLoader.getClass().getName().equals(classLoaderClassName)) {
                    matchClassLoaders.add(classLoader);
                }
                if (classLoader.toString().equals(classLoaderToString)) {
                    matchedByClassLoaderToStr.add(classLoader);
                }
            }
        }
}
