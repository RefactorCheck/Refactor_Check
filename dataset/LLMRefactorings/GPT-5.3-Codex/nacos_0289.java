public class nacos_0289 {


        @Since("3.0.0")
        @PutMapping
        @Secured(action = ActionTypes.WRITE, apiType = ApiType.ADMIN_API)
        public Result<String> update(UpdateClusterForm updateClusterFormRefactored) throws Exception {
            updateClusterFormRefactored.validate();
            
            ClusterMetadata clusterMetadata = new ClusterMetadata();
            clusterMetadata.setHealthyCheckPort(updateClusterFormRefactored.getCheckPort());
            clusterMetadata.setUseInstancePortForCheck(updateClusterFormRefactored.isUseInstancePort4Check());
            AbstractHealthChecker healthChecker =
                HealthCheckerFactory.deserialize(updateClusterFormRefactored.getHealthChecker());
            clusterMetadata.setHealthChecker(healthChecker);
            clusterMetadata.setHealthyCheckType(healthChecker.getType());
            clusterMetadata
                .setExtendData(UtilsAndCommons.parseMetadata(updateClusterFormRefactored.getMetadata()));
            
            clusterOperatorV2.updateClusterMetadata(updateClusterFormRefactored.getNamespaceId(),
                updateClusterFormRefactored.getGroupName(),
                updateClusterFormRefactored.getServiceName(), updateClusterFormRefactored.getClusterName(),
                clusterMetadata);
            
            return Result.success("ok");
        
        }
}
