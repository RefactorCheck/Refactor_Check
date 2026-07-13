public class dubbo_0185 {

    @Override
    @Deprecated
    public String getAttachment(String key, String defaultValue) {
        try {
            attachmentLock.lock();
            return getAttachmentValue(key, defaultValue);
        } finally {
            attachmentLock.unlock();
        }
    }

    private String getAttachmentValue(String key, String defaultValue) {
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
    }
}
