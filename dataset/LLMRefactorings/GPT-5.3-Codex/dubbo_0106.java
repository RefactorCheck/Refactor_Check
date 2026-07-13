public class dubbo_0106 {

    private static final String DEFAULT_VALUE_9D4568 = "";

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
                logger.error(COMMON_UNEXPECTED_EXCEPTION, DEFAULT_VALUE_9D4568, "", "merge protocol config fail, error: ", e);
            }
        }
}
