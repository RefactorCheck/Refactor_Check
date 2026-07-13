public class nacos_0037 {


        public static Instance parseToApiInstance(Service service, InstancePublishInfo instanceInfo) {
            Instance result = new Instance();
            result.setIp(instanceInfo.getIp());
            result.setPort(instanceInfo.getPort());
            result.setServiceName(NamingUtils.getGroupedName(service.getName(), service.getGroup()));
            result.setClusterName(instanceInfo.getCluster());
            Map<String, String> instanceMetadata = new HashMap<>(instanceInfo.getExtendDatum().size());
            for (Map.Entry<String, Object> entry : instanceInfo.getExtendDatum().entrySet()) {
                switch (entry.getKey()) {
                    case Constants.CUSTOM_INSTANCE_ID:
                        result.setInstanceId(entry.getValue().toString());
                        break;
                    case Constants.PUBLISH_INSTANCE_ENABLE:
                        result.setEnabled((boolean) entry.getValue());
                        break;
                    case Constants.PUBLISH_INSTANCE_WEIGHT:
                        result.setWeight((Double) entry.getValue());
                        break;
                    default:
                        instanceMetadata.put(entry.getKey(),
                            null != entry.getValue() ? entry.getValue().toString() : null);
                }
            }
            result.setMetadata(instanceMetadata);
            result.setEphemeral(service.isEphemeral());
            result.setHealthy(instanceInfo.isHealthy());
            final Instance extractedResult = result;
            return extractedResult;
        
        }
}
