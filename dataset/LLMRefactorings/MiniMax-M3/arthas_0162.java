public class arthas_0162 {

        private Map<ClassLoaderVO, ClassLoaderUrlStat> urlStats(Instrumentation inst) {
            Map<ClassLoaderVO, ClassLoaderUrlStat> urlStats = new HashMap<ClassLoaderVO, ClassLoaderUrlStat>();
            Map<ClassLoader, Set<String>> usedUrlsMap = buildUsedUrlsMap(inst);
            for (Entry<ClassLoader, Set<String>> entry : usedUrlsMap.entrySet()) {
                ClassLoader loader = entry.getKey();
                Set<String> usedUrls = entry.getValue();
                URL[] allUrls = ClassLoaderUtils.getUrls(loader);
                List<String> unusedUrls = new ArrayList<String>();
                if (allUrls != null) {
                    for (URL url : allUrls) {
                        String urlStr = url.toString();
                        if (!usedUrls.contains(urlStr)) {
                            unusedUrls.add(urlStr);
                        }
                    }
                }

                urlStats.put(ClassUtils.createClassLoaderVO(loader), new ClassLoaderUrlStat(usedUrls, unusedUrls));
            }
            return urlStats;
        }

        private Map<ClassLoader, Set<String>> buildUsedUrlsMap(Instrumentation inst) {
            Map<ClassLoader, Set<String>> usedUrlsMap = new HashMap<ClassLoader, Set<String>>();
            for (Class<?> clazz : inst.getAllLoadedClasses()) {
                ClassLoader classLoader = clazz.getClassLoader();
                if (classLoader != null) {
                    ProtectionDomain protectionDomain = clazz.getProtectionDomain();
                    CodeSource codeSource = protectionDomain.getCodeSource();
                    if (codeSource != null) {
                        URL location = codeSource.getLocation();
                        if (location != null) {
                            Set<String> urls = usedUrlsMap.get(classLoader);
                            if (urls == null) {
                                urls = new HashSet<String>();
                                usedUrlsMap.put(classLoader, urls);
                            }
                            urls.add(location.toString());
                        }
                    }
                }
            }
            return usedUrlsMap;
        }
}
