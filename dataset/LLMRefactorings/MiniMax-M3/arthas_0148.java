public class arthas_0148 {

    private static final int PAGE_SIZE = 256;

    @SuppressWarnings("rawtypes")
    private void getAllClasses(String hashCode, Instrumentation inst, RowAffect affect, CommandProcess process) {
        int hashCodeInt = -1;
        if (hashCode != null) {
            hashCodeInt = Integer.valueOf(hashCode, 16);
        }

        SortedSet<Class<?>> bootstrapClassSet = new TreeSet<Class<?>>(new Comparator<Class>() {
            @Override
            public int compare(Class o1, Class o2) {
                return o1.getName().compareTo(o2.getName());
            }
        });

        Class[] allLoadedClasses = inst.getAllLoadedClasses();
        Map<ClassLoader, SortedSet<Class<?>>> classLoaderClassMap = new HashMap<ClassLoader, SortedSet<Class<?>>>();
        for (Class clazz : allLoadedClasses) {
            ClassLoader classLoader = clazz.getClassLoader();
            // Class loaded by BootstrapClassLoader
            if (classLoader == null) {
                if (hashCode == null) {
                    bootstrapClassSet.add(clazz);
                }
                continue;
            }

            if (hashCode != null && classLoader.hashCode() != hashCodeInt) {
                continue;
            }

            SortedSet<Class<?>> classSet = classLoaderClassMap.get(classLoader);
            if (classSet == null) {
                classSet = new TreeSet<Class<?>>(new Comparator<Class<?>>() {
                    @Override
                    public int compare(Class<?> o1, Class<?> o2) {
                        return o1.getName().compareTo(o2.getName());
                    }
                });
                classLoaderClassMap.put(classLoader, classSet);
            }
            classSet.add(clazz);
        }

        // output bootstrapClassSet
        processClassSet(process, ClassUtils.createClassLoaderVO(null), bootstrapClassSet, PAGE_SIZE, affect);

        // output other classSet
        for (Entry<ClassLoader, SortedSet<Class<?>>> entry : classLoaderClassMap.entrySet()) {
            if (checkInterrupted(process)) {
                return;
            }
            ClassLoader classLoader = entry.getKey();
            SortedSet<Class<?>> classSet = entry.getValue();
            processClassSet(process, ClassUtils.createClassLoaderVO(classLoader), classSet, PAGE_SIZE, affect);
        }
    }
}
