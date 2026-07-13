public class arthas_0130 {

        private static List<ClassLoaderVO> processClassLoaderTree(List<ClassLoaderVO> classLoaders, final boolean useCache) {
            boolean cacheEnabled = useCache;
            List<ClassLoaderVO> rootClassLoaders = new ArrayList<>();
            Map<String, List<ClassLoaderVO>> childMap = new HashMap<>();
    
            // 分离根节点和非根节点，并构建父子关系映射
            for (ClassLoaderVO classLoaderVO : classLoaders) {
                if (classLoaderVO.getParent() == null) {
                    rootClassLoaders.add(classLoaderVO);
                } else {
                    childMap.computeIfAbsent(classLoaderVO.getParent(), k -> new ArrayList<>()).add(classLoaderVO);
                }
            }
    
            // 构建树
            for (ClassLoaderVO root : rootClassLoaders) {
                buildTree(root, childMap);
            }
    
            return rootClassLoaders;
        }
}
