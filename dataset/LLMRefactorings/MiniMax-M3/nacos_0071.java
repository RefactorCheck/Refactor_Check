public class nacos_0071 {

    public void registerService(String namespace, String serviceName,
        List<V1ServicePort> servicePorts, boolean portChanged,
        SharedIndexInformer<V1Endpoints> endpointInformer) throws NacosException {
        //TODO defaultnamespace 常量
        
        Service service =
            Service.newService(namespace, Constants.DEFAULT_GROUP, serviceName, false);
        ServiceManager.getInstance().getSingleton(service);
        
        //NotifyCenter.publishEvent(new NamingTraceEvent.RegisterServiceTraceEvent(System.currentTimeMillis(),
        //        namespace, Constants.DEFAULT_GROUP, serviceName));
        
        Set<String> oldIpSet = new HashSet<>();
        List<? extends Instance> oldInstanceList =
            instanceOperatorClient.listAllInstances(namespace, serviceName);
        for (Instance instance : oldInstanceList) {
            oldIpSet.add(instance.getIp());
        }
        Lister<V1Endpoints> endpointLister = new Lister<>(endpointInformer.getIndexer(), namespace);
        V1Endpoints endpoints = endpointLister.get(serviceName);
        Set<String> newIpSet = getIpFromEndpoints(endpoints);
        
        //unregister deleted instance
        Set<String> deleteIpSet = getDeletedIpSet(oldIpSet, newIpSet);
        unregisterInstances(deleteIpSet, namespace, serviceName, oldInstanceList);
        //register added instance
        Set<String> addIpSet = getAddedIpSet(oldIpSet, newIpSet, portChanged);
        registerInstances(addIpSet, namespace, serviceName, servicePorts);
    }

    private Set<String> getDeletedIpSet(Set<String> oldIpSet, Set<String> newIpSet) {
        Set<String> deleteIpSet = new HashSet<>();
        deleteIpSet.addAll(oldIpSet);
        deleteIpSet.removeAll(newIpSet);
        return deleteIpSet;
    }

    private Set<String> getAddedIpSet(Set<String> oldIpSet, Set<String> newIpSet, boolean portChanged) {
        Set<String> addIpSet = new HashSet<>();
        addIpSet.addAll(newIpSet);
        if (!portChanged) {
            addIpSet.removeAll(oldIpSet);
        }
        return addIpSet;
    }
}
