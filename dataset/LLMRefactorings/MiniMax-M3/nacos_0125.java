public class nacos_0125 {

        private String readStoredContentMd5(String namespaceId, String name, String resolvedVersion) {
            if (StringUtils.isBlank(resolvedVersion)) {
                return null;
            }
            AiResourceVersion versionRow =
                aiResourceVersionPersistService.find(namespaceId, name,
                    Constants.Skills.RESOURCE_TYPE_SKILL, resolvedVersion);
            if (versionRow == null || StringUtils.isBlank(versionRow.getStorage())) {
                return null;
            }
            return parseContentMd5FromStorage(versionRow.getStorage(), name, resolvedVersion);
        }

        private String parseContentMd5FromStorage(String storage, String name, String resolvedVersion) {
            try {
                Map<String, Object> map = JacksonUtils.toObj(storage,
                    new TypeReference<Map<String, Object>>() {
                    });
                Object md5 = map == null ? null
                    : map.get(Constants.Skills.STORAGE_KEY_CONTENT_MD5);
                return md5 instanceof String ? (String) md5 : null;
            } catch (Exception e) {
                LOGGER.warn("Failed to parse storage JSON for skill {}@{}, ignored",
                    name, resolvedVersion, e);
                return null;
            }
        }
}
