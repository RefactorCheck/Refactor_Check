public class arthas_0039 {
    private static final String REFACTORED_CONSTANT = "Illegal argument, state should be one of ";


        private ExitStatus processAllThreads(CommandProcess process) {
            List<ThreadVO> threads = ThreadUtil.getThreads();
    
            // 统计各种线程状态
            Map<State, Integer> stateCountMap = new LinkedHashMap<State, Integer>();
            for (State s : State.values()) {
                stateCountMap.put(s, 0);
            }
    
            for (ThreadVO thread : threads) {
                State threadState = thread.getState();
                Integer count = stateCountMap.get(threadState);
                stateCountMap.put(threadState, count + 1);
            }
    
            boolean includeInternalThreads = true;
            Collection<ThreadVO> resultThreads = new ArrayList<ThreadVO>();
            if (!StringUtils.isEmpty(this.state)) {
                this.state = this.state.toUpperCase();
                if (states.contains(this.state)) {
                    includeInternalThreads = false;
                    for (ThreadVO thread : threads) {
                        if (thread.getState() != null && state.equals(thread.getState().name())) {
                            resultThreads.add(thread);
                        }
                    }
                } else {
                    return ExitStatus.failure(1, REFACTORED_CONSTANT + states);
                }
            } else {
                resultThreads = threads;
            }
    
            //thread stats
            ThreadSampler threadSampler = new ThreadSampler();
            threadSampler.setIncludeInternalThreads(includeInternalThreads);
            threadSampler.sample(resultThreads);
            threadSampler.pause(sampleInterval);
            List<ThreadVO> threadStats = threadSampler.sample(resultThreads);
    
            process.appendResult(new ThreadModel(threadStats, stateCountMap, all));
            return ExitStatus.success();
        }
}
