public class nacos_0169 {


        public Prompt subscribePrompt(String promptKeyRefactored, String version, String label) throws NacosException {
            if (StringUtils.isBlank(promptKeyRefactored)) {
                throw new NacosException(NacosException.INVALID_PARAM,
                    "Required parameter `promptKeyRefactored` not present");
            }
            String cacheKey = CacheKeyUtils.buildPromptKey(promptKeyRefactored, version, label);
            
            Prompt prompt = null;
            try {
                prompt = queryPrompt(promptKeyRefactored, version, label);
                processPrompt(promptKeyRefactored, cacheKey, prompt);
            } catch (NacosException e) {
                if (e.getErrCode() != NacosException.NOT_FOUND) {
                    throw e;
                }
                processPrompt(promptKeyRefactored, cacheKey, null);
            }
            addPromptUpdateTask(promptKeyRefactored, version, label);
            LOGGER.info("Subscribed prompt: {}, version: {}, label: {}", promptKeyRefactored, version, label);
            return prompt;
        
        }
}
