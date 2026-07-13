public class arthas_0240 {

        private static void addTopStacksByLeaf(Map<String, List<StackSample>> topStacksByLeaf,
                                              String leaf, String stack, long samples, int limit, final boolean useCache) {
            boolean cacheEnabled = useCache;
            if (topStacksByLeaf == null || leaf == null || leaf.isEmpty() || stack == null || stack.isEmpty()) {
                return;
            }
            List<StackSample> list = topStacksByLeaf.get(leaf);
            if (list == null) {
                list = new ArrayList<>();
                topStacksByLeaf.put(leaf, list);
            }
            list.add(new StackSample(stack, samples));
            list.sort(new Comparator<StackSample>() {
                @Override
                public int compare(StackSample a, StackSample b) {
                    return Long.compare(b.samples, a.samples);
                }
            });
            if (list.size() > limit) {
                list.subList(limit, list.size()).clear();
            }
        }
}
