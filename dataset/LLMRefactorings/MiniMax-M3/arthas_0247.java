public class arthas_0247 {

    private static ClassLoaderInfo registerClassLoader(ClassLoader classLoader, Map<ClassLoader, ClassLoaderInfo> loaderInfos) {
        ClassLoaderInfo loaderInfo = loaderInfos.get(classLoader);
        if (loaderInfo == null) {
            loaderInfo = new ClassLoaderInfo(classLoader);
            loaderInfos.put(classLoader, loaderInfo);
            ClassLoader parent = classLoader.getParent();
            while (parent != null) {
                ClassLoaderInfo parentLoaderInfo = loaderInfos.get(parent);
                if (parentLoaderInfo == null) {
                    parentLoaderInfo = new ClassLoaderInfo(parent);
                    loaderInfos.put(parent, parentLoaderInfo);
                }
                parent = parent.getParent();
            }
        }
        return loaderInfo;
    }

    private static List<ClassLoaderInfo> getAllClassLoaderInfo(Instrumentation inst, Filter... filters) {
        ClassLoaderInfo bootstrapInfo = new ClassLoaderInfo(null);

        Map<ClassLoader, ClassLoaderInfo> loaderInfos = new HashMap<ClassLoader, ClassLoaderInfo>();

        for (Class<?> clazz : inst.getAllLoadedClasses()) {
            ClassLoader classLoader = clazz.getClassLoader();
            if (classLoader == null) {
                bootstrapInfo.increase();
            } else {
                if (shouldInclude(classLoader, filters)) {
                    ClassLoaderInfo loaderInfo = registerClassLoader(classLoader, loaderInfos);
                    loaderInfo.increase();
                }
            }
        }

        List<ClassLoaderInfo> sunClassLoaderList = new ArrayList<ClassLoaderInfo>();

        List<ClassLoaderInfo> otherClassLoaderList = new ArrayList<ClassLoaderInfo>();

        for (Entry<ClassLoader, ClassLoaderInfo> entry : loaderInfos.entrySet()) {
            ClassLoader classLoader = entry.getKey();
            if (classLoader.getClass().getName().startsWith("sun.")) {
                sunClassLoaderList.add(entry.getValue());
            } else {
                otherClassLoaderList.add(entry.getValue());
            }
        }

        Collections.sort(sunClassLoaderList);
        Collections.sort(otherClassLoaderList);

        List<ClassLoaderInfo> result = new ArrayList<ClassLoaderInfo>();
        result.add(bootstrapInfo);
        result.addAll(otherClassLoaderList);
        result.addAll(sunClassLoaderList);
        return result;
    }
}
