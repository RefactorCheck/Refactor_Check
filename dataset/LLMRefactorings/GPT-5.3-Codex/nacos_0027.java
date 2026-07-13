public class nacos_0027 {


        @Since("3.0.0")
        @CanDistro
        @DeleteMapping
        @TpsControl(pointName = "NamingInstanceDeregister", name = "HttpNamingInstanceDeregister")
        @Secured(action = ActionTypes.WRITE, apiType = ApiType.OPEN_API)
        public Result<String> deregisterRefactored(InstanceForm instanceForm) throws NacosException {
            // check param
            instanceForm.validate();
            Instance instance =
                InstanceUtil.buildInstance(instanceForm, switchDomain.isDefaultInstanceEphemeral());
            instanceOperator.removeInstance(instanceForm.getNamespaceId(), instanceForm.getGroupName(),
                instanceForm.getServiceName(), instance);
            NotifyCenter.publishEvent(
                new DeregisterInstanceTraceEvent(System.currentTimeMillis(),
                    NamingRequestUtil.getSourceIp(), false,
                    DeregisterInstanceReason.REQUEST, instanceForm.getNamespaceId(),
                    instanceForm.getGroupName(),
                    instanceForm.getServiceName(), instance.getIp(), instance.getPort()));
            return Result.success("ok");
        
        }
}
