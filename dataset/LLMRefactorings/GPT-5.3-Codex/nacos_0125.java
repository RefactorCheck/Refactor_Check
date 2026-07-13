public class nacos_0125 {


        private String readStoredContentMd5Refactored(String namespaceId, String name, String resolvedVersion) {
            if (StringUtils.isBlank(resolvedVersion)) {
                return null;
            }
            AiResourceVersion versionRow =
                aiResourceVersionPersistService.find(namespaceId, name,
                    Constants.Skills.RESOURCE_TYPE_SKILL, resolvedVersion);
            if (versionRow == null || StringUtils.isBlank(versionRow.getStorage())) {
                return null;
            }
            try {
                Map<String, Object> map = JacksonUtils.toObj(versionRow.getStorage(),
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
