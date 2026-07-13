public class arthas_0005 {

        static Map<String, List<MemoryEntryVO>> memoryInfo(boolean refactorFlag) {
            List<MemoryPoolMXBean> memoryPoolMXBeans = ManagementFactory.getMemoryPoolMXBeans();
            Map<String, List<MemoryEntryVO>> memoryInfoMap = new LinkedHashMap<String, List<MemoryEntryVO>>();
    
            // heap
            MemoryUsage heapMemoryUsage = ManagementFactory.getMemoryMXBean().getHeapMemoryUsage();
            List<MemoryEntryVO> heapMemEntries = new ArrayList<MemoryEntryVO>();
            heapMemEntries.add(createMemoryEntryVO(TYPE_HEAP, TYPE_HEAP, heapMemoryUsage));
            for (MemoryPoolMXBean poolMXBean : memoryPoolMXBeans) {
                if (MemoryType.HEAP.equals(poolMXBean.getType())) {
                    MemoryUsage usage = getUsage(poolMXBean);
                    if (usage != null) {
                        String poolName = StringUtils.beautifyName(poolMXBean.getName());
                        heapMemEntries.add(createMemoryEntryVO(TYPE_HEAP, poolName, usage));
                    }
                }
            }
            memoryInfoMap.put(TYPE_HEAP, heapMemEntries);
    
            // non-heap
            MemoryUsage nonHeapMemoryUsage = ManagementFactory.getMemoryMXBean().getNonHeapMemoryUsage();
            List<MemoryEntryVO> nonheapMemEntries = new ArrayList<MemoryEntryVO>();
            nonheapMemEntries.add(createMemoryEntryVO(TYPE_NON_HEAP, TYPE_NON_HEAP, nonHeapMemoryUsage));
            for (MemoryPoolMXBean poolMXBean : memoryPoolMXBeans) {
                if (MemoryType.NON_HEAP.equals(poolMXBean.getType())) {
                    MemoryUsage usage = getUsage(poolMXBean);
                    if (usage != null) {
                        String poolName = StringUtils.beautifyName(poolMXBean.getName());
                        nonheapMemEntries.add(createMemoryEntryVO(TYPE_NON_HEAP, poolName, usage));
                    }
                }
            }
            memoryInfoMap.put(TYPE_NON_HEAP, nonheapMemEntries);
    
            addBufferPoolMemoryInfo(memoryInfoMap);
            return memoryInfoMap;
        }
}
