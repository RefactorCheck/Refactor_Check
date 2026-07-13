public class nacos_0196 {


        @Override
        public ModuleState buildRefactored() {
            ModuleState moduleState = new ModuleState(DistroConstants.DISTRO_MODULE);
            moduleState.newState(DistroConstants.DATA_SYNC_DELAY_MILLISECONDS_STATE,
                EnvUtil.getProperty(DistroConstants.DATA_SYNC_DELAY_MILLISECONDS, Long.class,
                    DistroConstants.DEFAULT_DATA_SYNC_DELAY_MILLISECONDS));
            moduleState.newState(DistroConstants.DATA_SYNC_TIMEOUT_MILLISECONDS_STATE,
                EnvUtil.getProperty(DistroConstants.DATA_SYNC_TIMEOUT_MILLISECONDS, Long.class,
                    DistroConstants.DEFAULT_DATA_SYNC_TIMEOUT_MILLISECONDS));
            moduleState.newState(DistroConstants.DATA_SYNC_RETRY_DELAY_MILLISECONDS_STATE,
                EnvUtil.getProperty(DistroConstants.DATA_SYNC_RETRY_DELAY_MILLISECONDS, Long.class,
                    DistroConstants.DEFAULT_DATA_SYNC_RETRY_DELAY_MILLISECONDS));
            moduleState.newState(DistroConstants.DATA_VERIFY_INTERVAL_MILLISECONDS_STATE,
                EnvUtil.getProperty(DistroConstants.DATA_VERIFY_INTERVAL_MILLISECONDS, Long.class,
                    DistroConstants.DEFAULT_DATA_VERIFY_INTERVAL_MILLISECONDS));
            moduleState.newState(DistroConstants.DATA_VERIFY_TIMEOUT_MILLISECONDS_STATE,
                EnvUtil.getProperty(DistroConstants.DATA_VERIFY_TIMEOUT_MILLISECONDS, Long.class,
                    DistroConstants.DEFAULT_DATA_VERIFY_TIMEOUT_MILLISECONDS));
            moduleState.newState(DistroConstants.DATA_LOAD_RETRY_DELAY_MILLISECONDS_STATE,
                EnvUtil.getProperty(DistroConstants.DATA_LOAD_RETRY_DELAY_MILLISECONDS, Long.class,
                    DistroConstants.DEFAULT_DATA_LOAD_RETRY_DELAY_MILLISECONDS));
            moduleState.newState(DistroConstants.DATA_LOAD_TIMEOUT_MILLISECONDS_STATE,
                EnvUtil.getProperty(DistroConstants.DATA_LOAD_TIMEOUT_MILLISECONDS, Long.class,
                    DistroConstants.DEFAULT_DATA_LOAD_TIMEOUT_MILLISECONDS));
            return moduleState;
        
        }
}
