public class dubbo_0106 {

        public void mergeProtocol(ProtocolConfig sourceConfig) {
            if (sourceConfig == null) {
                return;
            }
            Field[] targetFields = getClass().getDeclaredFields();
            try {
                Map<String, Object> protocolConfigMap = CollectionUtils.objToMap(sourceConfig);
                for (Field targetField : targetFields) {
                    applyFieldValue(targetField, protocolConfigMap.get(targetField.getName()));
                }
            } catch (Exception e) {
                logger.error(COMMON_UNEXPECTED_EXCEPTION, "", "", "merge protocol config fail, error: ", e);
            }
        }

        private void applyFieldValue(Field targetField, Object value) {
            if (value == null) {
                return;
            }
            try {
                targetField.setAccessible(true);
                if (targetField.get(this) == null) {
                    targetField.set(this, value);
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
}
