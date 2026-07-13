public class nacos_0027 {

    @Since("3.0.0")
    @CanDistro
    @DeleteMapping
    @TpsControl(pointName = "NamingInstanceDeregister", name = "HttpNamingInstanceDeregister")
    @Secured(action = ActionTypes.WRITE, apiType = ApiType.OPEN_API)
    public Result<String> deregister(InstanceForm instanceForm) throws NacosException {
        instanceForm.validate();
        Instance instance = removeInstance(instanceForm);
        publishDeregisterEvent(instanceForm, instance);
        return Result.success("ok");
    }

    private Instance removeInstance(InstanceForm instanceForm) throws NacosException {
        Instance instance = InstanceUtil.buildInstance(instanceForm, switchDomain.isDefaultInstanceEphemeral());
        instanceOperator.removeInstance(instanceForm.getNamespaceId(), instanceForm.getGroupName(),
            instanceForm.getServiceName(), instance);
        return instance;
    }

    private void publishDeregisterEvent(InstanceForm instanceForm, Instance instance) {
        NotifyCenter.publishEvent(
            new DeregisterInstanceTraceEvent(System.currentTimeMillis(),
                NamingRequestUtil.getSourceIp(), false,
                DeregisterInstanceReason.REQUEST, instanceForm.getNamespaceId(),
                instanceForm.getGroupName(),
                instanceForm.getServiceName(), instance.getIp(), instance.getPort()));
    }
}
