public class nacos_0183 {

        void executeAsyncRpcTask(Queue<NotifySingleRpcTask> queue) {
            while (!queue.isEmpty()) {
                NotifySingleRpcTask task = queue.poll();
                
                ConfigChangeClusterSyncRequest syncRequest = new ConfigChangeClusterSyncRequest();
                syncRequest.setDataId(task.getDataId());
                syncRequest.setTenant(task.getTenant());
                syncRequest.setGroup(task.getGroup());
                syncRequest.setLastModified(task.getLastModified());
                syncRequest.setGrayName(task.getGrayName());
                Member member = task.member;
                
                String event = getNotifyEvent(task);
                if (memberManager.hasMember(member.getAddress())) {
                    // start the health check and there are ips that are not monitored, put them directly in the notification queue, otherwise notify
                    boolean unHealthNeedDelay = isUnHealthy(member.getAddress());
                    if (unHealthNeedDelay) {
                        // target ip is unhealthy, then put it in the notification list
                        ConfigTraceService.logNotifyEvent(task.getDataId(), task.getGroup(),
                            task.getTenant(), null,
                            task.getLastModified(), InetUtils.getSelfIP(), event,
                            ConfigTraceService.NOTIFY_TYPE_UNHEALTH, 0, member.getAddress());
                        // get delay time and set fail count to the task
                        asyncTaskExecute(task);
                    } else {
                        
                        // grpc report data change only
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
}
