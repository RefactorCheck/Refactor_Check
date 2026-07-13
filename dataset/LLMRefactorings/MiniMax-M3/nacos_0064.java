public class nacos_0064 {

        private void redoForInstance(InstanceRedoData redoData) throws NacosException {
            NamingRedoData.RedoType redoType = redoData.getRedoType();
            String serviceName = redoData.getServiceName();
            String groupName = redoData.getGroupName();
            LogUtils.NAMING_LOGGER.info("Redo instance operation {} for {}@@{}", redoType, groupName,
                serviceName);
            handleRedoType(redoData, redoType, serviceName, groupName);
        }

        private void handleRedoType(InstanceRedoData redoData, NamingRedoData.RedoType redoType,
                String serviceName, String groupName) throws NacosException {
            switch (redoType) {
                case REGISTER:
                    if (isClientDisabled()) {
                        return;
                    }
                    processRegisterRedoType(redoData, serviceName, groupName);
                    break;
                case UNREGISTER:
                    if (isClientDisabled()) {
                        return;
                    }
                    clientProxy.doDeregisterService(serviceName, groupName, redoData.get());
                    break;
                case REMOVE:
                    redoService.removeInstanceForRedo(serviceName, groupName);
                    break;
                default:
            }
        }
}
