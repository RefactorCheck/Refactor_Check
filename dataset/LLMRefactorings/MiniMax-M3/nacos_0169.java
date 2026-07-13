public class nacos_0169 {

        public Prompt subscribePrompt(String promptKey, String version, String label)
            throws NacosException {
            validatePromptKey(promptKey);
            String cacheKey = CacheKeyUtils.buildPromptKey(promptKey, version, label);
            
            Prompt prompt = queryAndProcessPrompt(promptKey, version, label, cacheKey);
            
            addPromptUpdateTask(promptKey, version, label);
            LOGGER.info("Subscribed prompt: {}, version: {}, label: {}", promptKey, version, label);
            return prompt;
        }

        private void validatePromptKey(String promptKey) throws NacosException {
            if (StringUtils.isBlank(promptKey)) {
                throw new NacosException(NacosException.INVALID_PARAM,
                    "Required parameter `promptKey` not present");
            }
        }

        private Prompt queryAndProcessPrompt(String promptKey, String version, String label, String cacheKey)
            throws NacosException {
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
            return prompt;
        }
}
