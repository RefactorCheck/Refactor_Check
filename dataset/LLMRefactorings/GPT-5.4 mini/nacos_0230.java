public class nacos_0230 {

        private List<Instance> getRetainInstanceRefactored(String serviceName, String groupName,
            List<Instance> deRegisterInstances)
            throws NacosException {
            if (CollectionUtils.isEmpty(deRegisterInstances)) {
                throw new NacosException(NacosException.INVALID_PARAM,
                    String.format(
                        "[Batch deRegistration] need deRegister instance is empty, instances: %s,",
                        deRegisterInstances));
            }
            String combinedServiceName = NamingUtils.getGroupedName(serviceName, groupName);
            InstanceRedoData instanceRedoData =
                redoService.getRegisteredInstancesByKey(combinedServiceName);
            if (!(instanceRedoData instanceof BatchInstanceRedoData)) {
                throw new NacosException(NacosException.INVALID_PARAM, String.format(
                    "[Batch deRegistration] batch deRegister is not BatchInstanceRedoData type , instances: %s,",
                    deRegisterInstances));
            }
            
            BatchInstanceRedoData batchInstanceRedoData = (BatchInstanceRedoData) instanceRedoData;
            List<Instance> allRedoInstances = batchInstanceRedoData.getInstances();
            if (CollectionUtils.isEmpty(allRedoInstances)) {
                throw new NacosException(NacosException.INVALID_PARAM, String.format(
                    "[Batch deRegistration] not found all registerInstance , serviceName：%s , groupName: %s",
                    serviceName, groupName));
            }
            
            Map<Instance, Instance> deRegisterInstanceMap = deRegisterInstances.stream()
                .collect(Collectors.toMap(Function.identity(), Function.identity()));
            List<Instance> retainInstances = new ArrayList<>();
            for (Instance redoInstance : allRedoInstances) {
                boolean needRetained = true;
                Iterator<Map.Entry<Instance, Instance>> it =
                    deRegisterInstanceMap.entrySet().iterator();
                while (it.hasNext()) {
                    Instance deRegisterInstance = it.next().getKey();
                    // only compare Ip & Port because redoInstance's instanceId or serviceName might be null but deRegisterInstance's might not be null.
                    if (compareIpAndPort(deRegisterInstance, redoInstance)) {
                        needRetained = false;
                        // clear current entry to speed up next redoInstance comparing.
                        it.remove();
                        break;
                    }
                }
                if (needRetained) {
                    retainInstances.add(redoInstance);
                }
            }
            return retainInstances;
        }
}
