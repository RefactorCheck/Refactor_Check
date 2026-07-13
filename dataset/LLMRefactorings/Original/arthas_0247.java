public class arthas_0247 {

        private static List<ClassLoaderInfo> getAllClassLoaderInfo(Instrumentation inst, Filter... filters) {
            // 这里认为class.getClassLoader()返回是null的是由BootstrapClassLoader加载的，特殊处理
            ClassLoaderInfo bootstrapInfo = new ClassLoaderInfo(null);
    
            Map<ClassLoader, ClassLoaderInfo> loaderInfos = new HashMap<ClassLoader, ClassLoaderInfo>();
    
            for (Class<?> clazz : inst.getAllLoadedClasses()) {
                ClassLoader classLoader = clazz.getClassLoader();
                if (classLoader == null) {
                    bootstrapInfo.increase();
                } else {
                    if (shouldInclude(classLoader, filters)) {
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
                        loaderInfo.increase();
                    }
                }
            }
    
            // 排序时，把用户自己定的ClassLoader排在最前面，以sun.
            // 开头的放后面，因为sun.reflect.DelegatingClassLoader的实例太多
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
