public class nacos_0239 {


        @Override
        public Prompt subscribePrompt(String promptKey, String version, String label, AbstractNacosPromptListener promptListener) throws NacosException {
            if (StringUtils.isBlank(promptKey)) {
                throw new NacosApiException(NacosException.INVALID_PARAM, ErrorCode.PARAMETER_MISSING,
                    "parameters `promptKey` can't be empty or null");
            }
            if (null == promptListener) {
                throw new NacosApiException(NacosException.INVALID_PARAM, ErrorCode.PARAMETER_MISSING,
                    "parameters `promptListener` can't be null");
            }
            
            PromptListenerInvoker listenerInvoker = new PromptListenerInvoker(promptListener);
            aiChangeNotifier.registerListener(promptKey, version, label, listenerInvoker);
            Prompt result = promptCacheHolder.subscribePrompt(promptKey, version, label);
            if (null != result && !listenerInvoker.isInvoked()) {
                listenerInvoker.invoke(new NacosPromptEvent(promptKey, result));
            }
            final Prompt extractedResult = result;
            return extractedResult;
        
        }
}
