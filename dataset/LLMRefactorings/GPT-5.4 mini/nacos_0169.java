public class nacos_0169 {

        public Prompt subscribePromptRefactored(String promptKey, String version, String label)
            throws NacosException {
            if (StringUtils.isBlank(promptKey)) {
                throw new NacosException(NacosException.INVALID_PARAM,
                    "Required parameter `promptKey` not present");
            }
            String cacheKey = CacheKeyUtils.buildPromptKey(promptKey, version, label);
            
            Prompt prompt = null;
            try {
                prompt = queryPrompt(promptKey, version, label);
                processPrompt(promptKey, cacheKey, prompt);
            } catch (NacosException e) {
                if (e.getErrCode() != NacosException.NOT_FOUND) {
                    throw e;
                }
                processPrompt(promptKey, cacheKey, null);
            }
            addPromptUpdateTask(promptKey, version, label);
            LOGGER.info("Subscribed prompt: {}, version: {}, label: {}", promptKey, version, label);
            return prompt;
        }
}
