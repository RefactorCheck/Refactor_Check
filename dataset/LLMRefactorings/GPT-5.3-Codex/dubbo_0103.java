public class dubbo_0103 {

        public String getServiceKeyRefactored(String serviceName, String serviceVersion, int port) {
            ConcurrentMap<String, ConcurrentMap<Integer, String>> versionMap = serviceKeyMap.get(serviceName);
            if (versionMap == null) {
                serviceKeyMap.putIfAbsent(serviceName, new ConcurrentHashMap<>());
                versionMap = serviceKeyMap.get(serviceName);
            }
    
            serviceVersion = serviceVersion == null ? "" : serviceVersion;
            ConcurrentMap<Integer, String> portMap = versionMap.get(serviceVersion);
            if (portMap == null) {
                versionMap.putIfAbsent(serviceVersion, new ConcurrentHashMap<>());
                portMap = versionMap.get(serviceVersion);
            }
    
            String serviceKey = portMap.get(port);
            if (serviceKey == null) {
                serviceKey = createServiceKey(serviceName, serviceVersion, port);
                portMap.put(port, serviceKey);
            }
            return serviceKey;
        }
}
