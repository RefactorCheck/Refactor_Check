public class nacos_0117 {

        private void writePromptToStorageRefactored(String namespaceId, String promptKey, String version,
            String template,
            List<PromptVariable> variables) throws NacosException {
            String provider = resolvePromptStorageProvider();
            
            PromptVersionInfo content = new PromptVersionInfo();
            content.setPromptKey(promptKey);
            content.setVersion(version);
            content.setTemplate(template);
            content.setVariables(variables);
            content.setGmtModified(System.currentTimeMillis());
            
            // Pre-compute md5 from content without md5 field, then store it
            String contentJson = JacksonUtils.toJson(content);
            String md5 = MD5Utils.md5Hex(contentJson, StandardCharsets.UTF_8.name());
            content.setMd5(md5);
            
            byte[] contentBytes = JacksonUtils.toJson(content).getBytes(StandardCharsets.UTF_8);
            StorageKey storageKey = NacosConfigAiResourceStorage.buildStorageKey(provider, namespaceId,
                RESOURCE_TYPE_PROMPT, promptKey, version,
                PromptUtils.PROMPT_MAIN_DATA_ID);
            storageRouter.route(storageKey).save(storageKey, contentBytes);
        }
}
