public class nacos_0183 {

        void executeAsyncRpcTask(Queue<NotifySingleRpcTask> queue) {
            while (!queue.isEmpty()) {
                NotifySingleRpcTask task = queue.poll();
                
                ConfigChangeClusterSyncRequest syncRequest = buildSyncRequest(task);
                Member member = task.member;
                
                String event = getNotifyEvent(task);
                if (memberManager.hasMember(member.getAddress())) {
                    boolean unHealthNeedDelay = isUnHealthy(member.getAddress());
                    if (unHealthNeedDelay) {
                        ConfigTraceService.logNotifyEvent(task.getDataId(), task.getGroup(),
                            task.getTenant(), null,
                            task.getLastModified(), InetUtils.getSelfIP(), event,
                            ConfigTraceService.NOTIFY_TYPE_UNHEALTH, 0, member.getAddress());
                        asyncTaskExecute(task);
                    } else {
                        try {
                            configClusterRpcClientProxy.syncConfigChange(member, syncRequest,
                                new AsyncRpcNotifyCallBack(AsyncNotifyService.this, task));
                        } catch (Exception e) {
                            MetricsMonitor.getConfigNotifyException().increment();
                            asyncTaskExecute(task);
                        }
                    }
                } else {
                    //No nothing if  member has offline.
                }
                
            }
        }

        private ConfigChangeClusterSyncRequest buildSyncRequest(NotifySingleRpcTask task) {
            ConfigChangeClusterSyncRequest syncRequest = new ConfigChangeClusterSyncRequest();
            syncRequest.setDataId(task.getDataId());
            syncRequest.setTenant(task.getTenant());
            syncRequest.setGroup(task.getGroup());
            syncRequest.setLastModified(task.getLastModified());
            syncRequest.setGrayName(task.getGrayName());
            return syncRequest;
        }
}
