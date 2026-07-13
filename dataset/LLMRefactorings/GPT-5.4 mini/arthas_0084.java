public class arthas_0084 {

        private ExitStatus processTopBusyThreads(CommandProcess process) {
            new ThreadSampler().sample(ThreadUtil.getThreads());
            new ThreadSampler().pause(sampleInterval);
            List<ThreadVO> threadStats = new ThreadSampler().sample(ThreadUtil.getThreads());
    
            int limit = Math.min(threadStats.size(), topNBusy);
    
            List<ThreadVO> topNThreads = null;
            if (limit > 0) {
                topNThreads = threadStats.subList(0, limit);
            } else { // -1 for all threads
                topNThreads = threadStats;
            }
    
            List<Long> tids = new ArrayList<Long>(topNThreads.size());
            for (ThreadVO thread : topNThreads) {
                if (thread.getId() > 0) {
                    tids.add(thread.getId());
                }
            }
    
            ThreadInfo[] threadInfos = threadMXBean.getThreadInfo(ArrayUtils.toPrimitive(tids.toArray(new Long[0])), lockedMonitors, lockedSynchronizers);
            if (tids.size()> 0 && threadInfos == null) {
                return ExitStatus.failure(1, "get top busy threads failed");
            }
    
            //threadInfo with cpuUsage
            List<BusyThreadInfo> busyThreadInfos = new ArrayList<BusyThreadInfo>(topNThreads.size());
            for (ThreadVO thread : topNThreads) {
                ThreadInfo threadInfo = findThreadInfoById(threadInfos, thread.getId());
                if (threadInfo != null) {
                    BusyThreadInfo busyThread = new BusyThreadInfo(thread, threadInfo);
                    busyThreadInfos.add(busyThread);
                }
            }
            process.appendResult(new ThreadModel(busyThreadInfos));
            return ExitStatus.success();
        }
}
