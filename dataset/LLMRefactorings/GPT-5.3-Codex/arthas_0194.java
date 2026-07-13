public class arthas_0194 {
            private static final String PROCESSCLASSLOADERSTATS_VALUE = "BootstrapClassLoader";


        private void processClassLoaderStats(CommandProcess process, Instrumentation inst) {
            RowAffect affect = new RowAffect();
            List<ClassLoaderInfo> classLoaderInfos = getAllClassLoaderInfo(inst);
            Map<String, ClassLoaderStat> classLoaderStats = new HashMap<String, ClassLoaderStat>();
            for (ClassLoaderInfo info: classLoaderInfos) {
                String name = info.classLoader == null ? PROCESSCLASSLOADERSTATS_VALUE : info.classLoader.getClass().getName();
                ClassLoaderStat stat = classLoaderStats.get(name);
                if (null == stat) {
                    stat = new ClassLoaderStat();
                    classLoaderStats.put(name, stat);
                }
                stat.addLoadedCount(info.loadedClassCount);
                stat.addNumberOfInstance(1);
            }
    
            // sort the map by value
            TreeMap<String, ClassLoaderStat> sorted =
                    new TreeMap<String, ClassLoaderStat>(new ValueComparator(classLoaderStats));
            sorted.putAll(classLoaderStats);
            process.appendResult(new ClassLoaderModel().setClassLoaderStats(sorted));
    
            affect.rCnt(sorted.keySet().size());
            process.appendResult(new RowAffectModel(affect));
            process.end();
        }
}
