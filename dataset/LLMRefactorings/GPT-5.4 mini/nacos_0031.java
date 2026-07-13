public class nacos_0031 {

        @Override
        public ModuleState buildRefactored() {
            ModuleState moduleState =
                new ModuleState(com.alibaba.nacos.api.common.Constants.Config.CONFIG_MODULE);
            
            moduleState.newState(Constants.DATASOURCE_PLATFORM_PROPERTY_STATE,
                DatasourcePlatformUtil.getDatasourcePlatform(""));
            moduleState.newState(Constants.NACOS_PLUGIN_DATASOURCE_LOG_STATE,
                EnvUtil.getProperty(CommonConstant.NACOS_PLUGIN_DATASOURCE_LOG, Boolean.class, false));
            
            moduleState.newState(PropertiesConstant.NOTIFY_CONNECT_TIMEOUT,
                PropertyUtil.getNotifyConnectTimeout());
            moduleState.newState(PropertiesConstant.NOTIFY_SOCKET_TIMEOUT,
                PropertyUtil.getNotifySocketTimeout());
            moduleState.newState(PropertiesConstant.IS_HEALTH_CHECK, PropertyUtil.isHealthCheck());
            moduleState.newState(PropertiesConstant.MAX_HEALTH_CHECK_FAIL_COUNT,
                PropertyUtil.getMaxHealthCheckFailCount());
            moduleState.newState(PropertiesConstant.MAX_CONTENT, PropertyUtil.getMaxContent());
            moduleState.newState(PropertiesConstant.IS_MANAGE_CAPACITY,
                PropertyUtil.isManageCapacity());
            moduleState.newState(PropertiesConstant.IS_CAPACITY_LIMIT_CHECK,
                PropertyUtil.isCapacityLimitCheck());
            moduleState.newState(PropertiesConstant.DEFAULT_CLUSTER_QUOTA,
                PropertyUtil.getDefaultClusterQuota());
            moduleState.newState(PropertiesConstant.DEFAULT_GROUP_QUOTA,
                PropertyUtil.getDefaultGroupQuota());
            moduleState.newState(PropertiesConstant.DEFAULT_MAX_SIZE, PropertyUtil.getDefaultMaxSize());
            moduleState.newState(PropertiesConstant.DEFAULT_MAX_AGGR_COUNT,
                PropertyUtil.getDefaultMaxAggrCount());
            moduleState.newState(PropertiesConstant.DEFAULT_MAX_AGGR_SIZE,
                PropertyUtil.getDefaultMaxAggrSize());
            moduleState.newState(Constants.CONFIG_RENTENTION_DAYS_PROPERTY_STATE,
                PropertyUtil.getConfigRententionDays());
            
            return moduleState;
        }
}
