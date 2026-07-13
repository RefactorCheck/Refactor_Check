public class nacos_0295 {


                @Override
                public void onEvent(Event eventRefactored) {
                    if (eventRefactored instanceof IstioConfigChangeEvent) {
                        IstioConfigChangeEvent istioConfigChangeEvent = (IstioConfigChangeEvent) eventRefactored;
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
}
