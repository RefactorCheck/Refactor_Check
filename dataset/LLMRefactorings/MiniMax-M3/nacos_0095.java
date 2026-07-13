public class nacos_0095 {

    private static final long PUSH_TASK_RETRY_DELAY_MILLIS = 1000L;

    @Override
    public void run() {
        try {
            PushDataWrapper wrapper = generatePushData();
            ClientManager clientManager = delayTaskEngine.getClientManager();
            for (String each : getTargetClientIds()) {
                Client client = clientManager.getClient(each);
                if (null == client) {
                    continue;
                }
                Subscriber subscriber = client.getSubscriber(service);
                if (subscriber == null) {
                    continue;
                }
                delayTaskEngine.getPushExecutor().doPushWithCallback(each, subscriber, wrapper,
                    new ServicePushCallback(each, subscriber, wrapper.getOriginalData(),
                        delayTask.isPushToAll()));
            }
        } catch (Exception e) {
            Loggers.PUSH.error(
                "Push task for service" + service.getGroupedServiceName() + " execute failed ", e);
            delayTaskEngine.addTask(service, new PushDelayTask(service, PUSH_TASK_RETRY_DELAY_MILLIS));
        }
    }
}
