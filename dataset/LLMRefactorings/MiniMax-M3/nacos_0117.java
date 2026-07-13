public class nacos_0117 {

        private void writePromptToStorage(String namespaceId, String promptKey, String version,
            String template,
            List<PromptVariable> variables) throws NacosException {
            String provider = resolvePromptStorageProvider();
            
            PromptVersionInfo content = buildPromptVersionInfo(promptKey, version, template, variables);
            
            String md5 = MD5Utils.md5Hex(JacksonUtils.toJson(content), StandardCharsets.UTF_8.name());
            content.setMd5(md5);
            
            byte[] contentBytes = JacksonUtils.toJson(content).getBytes(StandardCharsets.UTF_8);
            StorageKey storageKey = NacosConfigAiResourceStorage.buildStorageKey(provider, namespaceId,
                RESOURCE_TYPE_PROMPT, promptKey, version,
                PromptUtils.PROMPT_MAIN_DATA_ID);
            storageRouter.route(storageKey).save(storageKey, contentBytes);
        }

        private PromptVersionInfo buildPromptVersionInfo(String promptKey, String version,
            String template, List<PromptVariable> variables) {
            PromptVersionInfo content = new PromptVersionInfo();
            content.setPromptKey(promptKey);
            content.setVersion(version);
            content.setTemplate(template);
            content.setVariables(variables);
            content.setGmtModified(System.currentTimeMillis());
            return content;
        }
}
