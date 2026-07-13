public class arthas_0084 {

        private ExitStatus processTopBusyThreads(CommandProcess process) {
            ThreadSampler threadSampler = new ThreadSampler();
            threadSampler.sample(ThreadUtil.getThreads());
            threadSampler.pause(sampleInterval);
            List<ThreadVO> threadStats = threadSampler.sample(ThreadUtil.getThreads());
    
            int limit = Math.min(threadStats.size(), topNBusy);
    
            List<ThreadVO> topNThreads = null;
            if (limit > 0) {
                topNThreads = threadStats.subList(0, limit);
            } else {
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
    
            List<BusyThreadInfo> busyThreadInfos = createBusyThreadInfos(topNThreads, threadInfos);
            process.appendResult(new ThreadModel(busyThreadInfos));
            return ExitStatus.success();
        }

        private List<BusyThreadInfo> createBusyThreadInfos(List<ThreadVO> topNThreads, ThreadInfo[] threadInfos) {
            List<BusyThreadInfo> busyThreadInfos = new ArrayList<BusyThreadInfo>(topNThreads.size());
            for (ThreadVO thread : topNThreads) {
                ThreadInfo threadInfo = findThreadInfoById(threadInfos, thread.getId());
                if (threadInfo != null) {
                    BusyThreadInfo busyThread = new BusyThreadInfo(thread, threadInfo);
                    busyThreadInfos.add(busyThread);
                }
            }
            return busyThreadInfos;
        }
}
