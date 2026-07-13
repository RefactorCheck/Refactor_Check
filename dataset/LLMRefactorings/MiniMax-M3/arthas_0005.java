public class arthas_0005 {

    static Map<String, List<MemoryEntryVO>> memoryInfo() {
        List<MemoryPoolMXBean> memoryPoolMXBeans = ManagementFactory.getMemoryPoolMXBeans();
        Map<String, List<MemoryEntryVO>> memoryInfoMap = new LinkedHashMap<String, List<MemoryEntryVO>>();

        MemoryUsage heapMemoryUsage = ManagementFactory.getMemoryMXBean().getHeapMemoryUsage();
        memoryInfoMap.put(TYPE_HEAP, getMemoryPoolEntries(TYPE_HEAP, MemoryType.HEAP, heapMemoryUsage, memoryPoolMXBeans));

        MemoryUsage nonHeapMemoryUsage = ManagementFactory.getMemoryMXBean().getNonHeapMemoryUsage();
        memoryInfoMap.put(TYPE_NON_HEAP, getMemoryPoolEntries(TYPE_NON_HEAP, MemoryType.NON_HEAP, nonHeapMemoryUsage, memoryPoolMXBeans));

        addBufferPoolMemoryInfo(memoryInfoMap);
        return memoryInfoMap;
    }

    private static List<MemoryEntryVO> getMemoryPoolEntries(String type, MemoryType memoryType, MemoryUsage memoryUsage, List<MemoryPoolMXBean> memoryPoolMXBeans) {
        List<MemoryEntryVO> memEntries = new ArrayList<MemoryEntryVO>();
        memEntries.add(createMemoryEntryVO(type, type, memoryUsage));
        for (MemoryPoolMXBean poolMXBean : memoryPoolMXBeans) {
            if (memoryType.equals(poolMXBean.getType())) {
                MemoryUsage usage = getUsage(poolMXBean);
                if (usage != null) {
                    String poolName = StringUtils.beautifyName(poolMXBean.getName());
                    memEntries.add(createMemoryEntryVO(type, poolName, usage));
                }
            }
        }
        return memEntries;
    }
}
