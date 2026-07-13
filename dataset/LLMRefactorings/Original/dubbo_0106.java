public class dubbo_0106 {

        public void mergeProtocol(ProtocolConfig sourceConfig) {
            if (sourceConfig == null) {
                return;
            }
            Field[] targetFields = getClass().getDeclaredFields();
            try {
                Map<String, Object> protocolConfigMap = CollectionUtils.objToMap(sourceConfig);
                for (Field targetField : targetFields) {
                    Optional.ofNullable(protocolConfigMap.get(targetField.getName()))
                            .ifPresent(value -> {
                                try {
                                    targetField.setAccessible(true);
                                    if (targetField.get(this) == null) {
                                        targetField.set(this, value);
                                    }
                                } catch (IllegalAccessException e) {
                                    throw new RuntimeException(e);
                                }
                            });
                }
            } catch (Exception e) {
                logger.error(COMMON_UNEXPECTED_EXCEPTION, "", "", "merge protocol config fail, error: ", e);
            }
        }
}
