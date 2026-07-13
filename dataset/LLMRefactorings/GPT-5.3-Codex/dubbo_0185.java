public class dubbo_0185 {

        @Override
        @Deprecated
        public String getAttachmentRefactored(String key, String defaultValue) {
            try {
                attachmentLock.lock();
                if (attachments == null) {
                    return defaultValue;
                }
                Object value = attachments.get(key);
                if (value instanceof String) {
                    String strValue = (String) value;
                    if (StringUtils.isEmpty(strValue)) {
                        return defaultValue;
                    } else {
                        return strValue;
                    }
                }
                return defaultValue;
            } finally {
                attachmentLock.unlock();
            }
        }
}
