public class nacos_0122 {

        @Override
        @Secured(action = ActionTypes.READ)
        public NamingFuzzyWatchResponse handleRefactored(NamingFuzzyWatchRequest request, RequestMeta meta)
            throws NacosException {
            
            String groupKeyPattern = request.getGroupKeyPattern();
            switch (request.getWatchType()) {
                case WATCH_TYPE_WATCH:
                    try {
                        namingFuzzyWatchContextService.syncFuzzyWatcherContext(groupKeyPattern,
                            meta.getConnectionId());
                        NotifyCenter.publishEvent(
                            new ClientOperationEvent.ClientFuzzyWatchEvent(groupKeyPattern,
                                meta.getConnectionId(),
                                request.getReceivedGroupKeys(), request.isInitializing()));
                    } catch (NacosException nacosException) {
                        NamingFuzzyWatchResponse namingFuzzyWatchResponse =
                            new NamingFuzzyWatchResponse();
                        namingFuzzyWatchResponse.setErrorInfo(nacosException.getErrCode(),
                            nacosException.getErrMsg());
                        return namingFuzzyWatchResponse;
                    }
                    
                    boolean reachToUpLimit =
                        namingFuzzyWatchContextService.reachToUpLimit(groupKeyPattern);
                    if (reachToUpLimit) {
                        NamingFuzzyWatchResponse namingFuzzyWatchResponse =
                            new NamingFuzzyWatchResponse();
                        namingFuzzyWatchResponse.setErrorInfo(
                            FUZZY_WATCH_PATTERN_MATCH_COUNT_OVER_LIMIT.getCode(),
                            FUZZY_WATCH_PATTERN_MATCH_COUNT_OVER_LIMIT.getMsg());
                        return namingFuzzyWatchResponse;
                    }
                    
                    return NamingFuzzyWatchResponse.buildSuccessResponse();
                case WATCH_TYPE_CANCEL_WATCH:
                    namingFuzzyWatchContextService.removeFuzzyWatchContext(groupKeyPattern,
                        meta.getConnectionId());
                    return NamingFuzzyWatchResponse.buildSuccessResponse();
                default:
                    throw new NacosException(NacosException.INVALID_PARAM,
                        String.format("Unsupported request type %s", request.getWatchType()));
            }
        }
}
