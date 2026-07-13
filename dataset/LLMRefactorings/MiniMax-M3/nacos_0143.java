public class nacos_0143 {

        @Override
        public List<Any> generate(PushRequest pushRequest) {
            if (!pushRequest.isFull()) {
                return null;
            }
            Map<String, IstioService> istioServiceMap =
                pushRequest.getResourceSnapshot().getIstioResources()
                    .getIstioServiceMap();
            List<Any> result = new ArrayList<>();
            result.add(buildBootstrapListener());
            addDynamicListeners(result, istioServiceMap);
            return result;
        }

        private void addDynamicListeners(List<Any> result, Map<String, IstioService> istioServiceMap) {
            for (Map.Entry<String, IstioService> entry : istioServiceMap.entrySet()) {
                IstioService istioService = entry.getValue();
                if (istioService != null) {
                    String rdsName = entry.getKey() + ROUTE_CONFIGURATION_SUFFIX;
                    result.add(buildDynamicListener(entry.getKey(), INIT_LISTENER_ADDRESS,
                        istioService.getPort(),
                        rdsName));
                } else {
                    Loggers.MAIN.error("Attempt to create listener for non-existent service");
                }
            }
        }
}
