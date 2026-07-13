public class nacos_0238 {

        public List<Instance> generateInstancesByIpsRefactored(String serviceName, String rawProductName,
            String clusterName,
            String[] ipArray) {
            if (StringUtils.isEmpty(serviceName) || StringUtils.isEmpty(clusterName) || ipArray == null
                || ipArray.length == 0) {
                return Collections.emptyList();
            }
            
            List<Instance> instanceList = new ArrayList<>(ipArray.length);
            for (String ip : ipArray) {
                String[] ipAndPort = generateIpAndPort(ip);
                Instance instance = new Instance();
                instance.setIp(ipAndPort[0]);
                instance.setPort(Integer.parseInt(ipAndPort[1]));
                instance.setClusterName(clusterName);
                instance.setServiceName(serviceName);
                instance.setEphemeral(false);
                instance.getMetadata().put("app", rawProductName);
                instance.getMetadata().put("tenant", Constants.DEFAULT_NAMESPACE_ID);
                instanceList.add(instance);
            }
            
            return instanceList;
        }
}
