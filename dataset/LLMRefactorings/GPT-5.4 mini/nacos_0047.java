public class nacos_0047 {

        public void updateInstanceRefactored(Service service, Instance instance, String clientId) {
            Service singleton = ServiceManager.getInstance().getSingleton(service);
            if (singleton.isEphemeral()) {
                throw new NacosRuntimeException(NacosException.INVALID_PARAM,
                    String.format(
                        "Current service %s is ephemeral service, can't update persistent instance.",
                        singleton.getGroupedServiceName()));
            }
            final PersistentClientOperationServiceImpl.InstanceStoreRequest request =
                new PersistentClientOperationServiceImpl.InstanceStoreRequest();
            request.setService(service);
            request.setInstance(instance);
            request.setClientId(clientId);
            final WriteRequest writeRequest = WriteRequest.newBuilder().setGroup(group())
                .setData(ByteString.copyFrom(serializer.serialize(request)))
                .setOperation(DataOperation.CHANGE.name())
                .build();
            try {
                protocol.write(writeRequest);
            } catch (Exception e) {
                throw new NacosRuntimeException(NacosException.SERVER_ERROR, e);
            }
        }
}
