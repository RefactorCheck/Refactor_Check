public class nacos_0196 {

        @Override
        public ModuleState build() {
            ModuleState moduleState = new ModuleState(DistroConstants.DISTRO_MODULE);
            setState(moduleState, DistroConstants.DATA_SYNC_DELAY_MILLISECONDS_STATE,
                    DistroConstants.DATA_SYNC_DELAY_MILLISECONDS,
                    DistroConstants.DEFAULT_DATA_SYNC_DELAY_MILLISECONDS);
            setState(moduleState, DistroConstants.DATA_SYNC_TIMEOUT_MILLISECONDS_STATE,
                    DistroConstants.DATA_SYNC_TIMEOUT_MILLISECONDS,
                    DistroConstants.DEFAULT_DATA_SYNC_TIMEOUT_MILLISECONDS);
            setState(moduleState, DistroConstants.DATA_SYNC_RETRY_DELAY_MILLISECONDS_STATE,
                    DistroConstants.DATA_SYNC_RETRY_DELAY_MILLISECONDS,
                    DistroConstants.DEFAULT_DATA_SYNC_RETRY_DELAY_MILLISECONDS);
            setState(moduleState, DistroConstants.DATA_VERIFY_INTERVAL_MILLISECONDS_STATE,
                    DistroConstants.DATA_VERIFY_INTERVAL_MILLISECONDS,
                    DistroConstants.DEFAULT_DATA_VERIFY_INTERVAL_MILLISECONDS);
            setState(moduleState, DistroConstants.DATA_VERIFY_TIMEOUT_MILLISECONDS_STATE,
                    DistroConstants.DATA_VERIFY_TIMEOUT_MILLISECONDS,
                    DistroConstants.DEFAULT_DATA_VERIFY_TIMEOUT_MILLISECONDS);
            setState(moduleState, DistroConstants.DATA_LOAD_RETRY_DELAY_MILLISECONDS_STATE,
                    DistroConstants.DATA_LOAD_RETRY_DELAY_MILLISECONDS,
                    DistroConstants.DEFAULT_DATA_LOAD_RETRY_DELAY_MILLISECONDS);
            setState(moduleState, DistroConstants.DATA_LOAD_TIMEOUT_MILLISECONDS_STATE,
                    DistroConstants.DATA_LOAD_TIMEOUT_MILLISECONDS,
                    DistroConstants.DEFAULT_DATA_LOAD_TIMEOUT_MILLISECONDS);
            return moduleState;
        }

        private void setState(ModuleState moduleState, String stateKey, String propertyKey, Long defaultValue) {
            moduleState.newState(stateKey,
                    EnvUtil.getProperty(propertyKey, Long.class, defaultValue));
        }
}
