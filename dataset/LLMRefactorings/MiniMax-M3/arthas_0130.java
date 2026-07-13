public class arthas_0130 {

    private static List<ClassLoaderVO> processClassLoaderTree(List<ClassLoaderVO> classLoaders) {
        List<ClassLoaderVO> rootClassLoaders = new ArrayList<>();
        Map<String, List<ClassLoaderVO>> childMap = new HashMap<>();

        for (ClassLoaderVO classLoaderVO : classLoaders) {
            if (classLoaderVO.getParent() == null) {
                rootClassLoaders.add(classLoaderVO);
            } else {
                childMap.computeIfAbsent(classLoaderVO.getParent(), k -> new ArrayList<>()).add(classLoaderVO);
            }
        }

        buildTrees(rootClassLoaders, childMap);

        return rootClassLoaders;
    }

    private static void buildTrees(List<ClassLoaderVO> rootClassLoaders, Map<String, List<ClassLoaderVO>> childMap) {
        for (ClassLoaderVO root : rootClassLoaders) {
            buildTree(root, childMap);
        }
    }
}
