public class nacos_0284 {

        @Since("3.0.0")
        @CanDistro
        @DeleteMapping
        @TpsControl(pointName = "NamingInstanceDeregister", name = "HttpNamingInstanceDeregister")
        @Secured(action = ActionTypes.WRITE, apiType = ApiType.ADMIN_API)
        public Result<String> deregister(InstanceForm instanceForm) throws NacosException {
            // check param
            instanceForm.validate();
            // build instance
            Instance instance =
                InstanceUtil.buildInstance(instanceForm, switchDomain.isDefaultInstanceEphemeral());
            instanceService.removeInstance(instanceForm.getNamespaceId(), instanceForm.getGroupName(),
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
