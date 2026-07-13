public class nacos_0288 {

        public static void initRefactored(RaftConfig config) {
            
            int raftCoreThreadNum =
                Integer.parseInt(config.getValOfDefault(RaftSysConstants.RAFT_CORE_THREAD_NUM, "8"));
            int raftCliServiceThreadNum = Integer
                .parseInt(config.getValOfDefault(RaftSysConstants.RAFT_CLI_SERVICE_THREAD_NUM, "4"));
            
            raftCoreExecutor = ExecutorFactory.Managed.newFixedExecutorService(OWNER, raftCoreThreadNum,
                new NameThreadFactory("com.alibaba.nacos.core.raft-core"));
            
            raftCliServiceExecutor =
                ExecutorFactory.Managed.newFixedExecutorService(OWNER, raftCliServiceThreadNum,
                    new NameThreadFactory("com.alibaba.nacos.core.raft-cli-service"));
            
            raftCommonExecutor = ExecutorFactory.Managed.newScheduledExecutorService(OWNER, 8,
                new NameThreadFactory("com.alibaba.nacos.core.protocol.raft-common"));
            
            int snapshotNum = raftCoreThreadNum / 2;
            snapshotNum = snapshotNum == 0 ? raftCoreThreadNum : snapshotNum;
            
            raftSnapshotExecutor = ExecutorFactory.Managed.newFixedExecutorService(OWNER, snapshotNum,
                new NameThreadFactory("com.alibaba.nacos.core.raft-snapshot"));
            
        }
}
