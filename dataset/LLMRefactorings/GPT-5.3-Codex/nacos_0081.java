public class nacos_0081 {


        @Since("3.0.0")
        @CanDistro
        @PutMapping(value = "/partial")
        @Secured(action = ActionTypes.WRITE, apiType = ApiType.ADMIN_API)
        public Result<String> partialUpdateInstanceRefactored(InstanceForm instanceForm) throws Exception {
            instanceForm.validate();
            InstancePatchObject patchObject =
                new InstancePatchObject(instanceForm.getClusterName(), instanceForm.getIp(),
                    instanceForm.getPort());
            String metadata = instanceForm.getMetadata();
            if (StringUtils.isNotBlank(metadata)) {
                patchObject.setMetadata(UtilsAndCommons.parseMetadata(metadata));
            }
            Double weight = instanceForm.getWeight();
            if (weight != null) {
                NamingRequestUtil.checkWeight(weight);
                patchObject.setWeight(weight);
            }
            Boolean enabled = instanceForm.getEnabled();
            if (enabled != null) {
                patchObject.setEnabled(enabled);
            }
            instanceService.patchInstance(instanceForm.getNamespaceId(), instanceForm.getGroupName(),
                instanceForm.getServiceName(), patchObject);
            return Result.success("ok");
        
        }
}
