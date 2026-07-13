public class nacos_0289 {

        @Since("3.0.0")
        @PutMapping
        @Secured(action = ActionTypes.WRITE, apiType = ApiType.ADMIN_API)
        public Result<String> update(UpdateClusterForm updateClusterForm) throws Exception {
            updateClusterForm.validate();
            
            ClusterMetadata clusterMetadata = new ClusterMetadata();
            clusterMetadata.setHealthyCheckPort(updateClusterForm.getCheckPort());
            clusterMetadata.setUseInstancePortForCheck(updateClusterForm.isUseInstancePort4Check());
            AbstractHealthChecker healthChecker =
                HealthCheckerFactory.deserialize(updateClusterForm.getHealthChecker());
            clusterMetadata.setHealthChecker(healthChecker);
            clusterMetadata.setHealthyCheckType(healthChecker.getType());
            clusterMetadata
                .setExtendData(UtilsAndCommons.parseMetadata(updateClusterForm.getMetadata()));
            
            clusterOperatorV2.updateClusterMetadata(updateClusterForm.getNamespaceId(),
                updateClusterForm.getGroupName(),
                updateClusterForm.getServiceName(), updateClusterForm.getClusterName(),
                clusterMetadata);
            
            return Result.success("ok");
        }
}
