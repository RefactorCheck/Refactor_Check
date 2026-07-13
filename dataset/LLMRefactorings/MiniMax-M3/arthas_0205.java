public class arthas_0205 {

        @Override
        public void draw(CommandProcess process, DashboardModel result) {
            int width = process.width();
            int height = process.height();
    
            int totalHeight = height - 1;
            int threadTopHeight = calculateThreadTopHeight(totalHeight);
            int lowerHalf = totalHeight - threadTopHeight;
    
            int memoryInfoHeight = lowerHalf / 2;
            if (memoryInfoHeight < 8) {
                memoryInfoHeight = Math.min(8, lowerHalf);
            }
    
            TableElement runtimeInfoTable = drawRuntimeInfo(result.getRuntimeInfo());
            TableElement tomcatInfoTable = drawTomcatInfo(result.getTomcatInfo());
            int runtimeInfoHeight = Math.max(runtimeInfoTable.getRows().size(), tomcatInfoTable == null ? 0 : tomcatInfoTable.getRows().size());
            if (runtimeInfoHeight < lowerHalf - memoryInfoHeight) {
                memoryInfoHeight = lowerHalf - runtimeInfoHeight;
            } else {
                runtimeInfoHeight = lowerHalf - memoryInfoHeight;
            }
    
            int maxMemoryInfoHeight = getMemoryInfoHeight(result.getMemoryInfo());
            memoryInfoHeight = Math.min(memoryInfoHeight, maxMemoryInfoHeight);
            threadTopHeight = totalHeight - memoryInfoHeight - runtimeInfoHeight;
    
            String threadInfo = ViewRenderUtil.drawThreadInfo(result.getThreads(), width, threadTopHeight);
            String memoryAndGc = drawMemoryInfoAndGcInfo(result.getMemoryInfo(), result.getGcInfos(), width, memoryInfoHeight);
            String runTimeAndTomcat = drawRuntimeInfoAndTomcatInfo(runtimeInfoTable, tomcatInfoTable, width, runtimeInfoHeight);
    
            process.write(threadInfo + memoryAndGc + runTimeAndTomcat);
        }
        
        private int calculateThreadTopHeight(int totalHeight) {
            if (totalHeight <= 24) {
                return totalHeight / 2;
            }
            int threadTopHeight = totalHeight / 3;
            return Math.max(threadTopHeight, 12);
        }
}
