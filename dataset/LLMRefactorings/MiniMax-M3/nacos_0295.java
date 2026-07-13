public class nacos_0295 {

    @Override
    public void onEvent(Event event) {
        if (event instanceof IstioConfigChangeEvent) {
            handleIstioConfigChangeEvent((IstioConfigChangeEvent) event);
        }
    }

    private void handleIstioConfigChangeEvent(IstioConfigChangeEvent istioConfigChangeEvent) {
        String content = istioConfigChangeEvent.content;
        if (isContentValid(content) && tryParseContent(content)) {
            PushRequest pushRequest = new PushRequest(content, true);
            if (null == nacosXdsService) {
                nacosXdsService = ApplicationUtils.getBean(NacosXdsService.class);
            }
            if (null == resourceManager) {
                resourceManager = ApplicationUtils.getBean(NacosResourceManager.class);
            }
            pushRequest.addReason(CONFIG_REASON);
            ResourceSnapshot snapshot = resourceManager.createResourceSnapshot();
            pushRequest.setResourceSnapshot(snapshot);
            nacosXdsService.handleConfigEvent(pushRequest);
        }
    }
}
