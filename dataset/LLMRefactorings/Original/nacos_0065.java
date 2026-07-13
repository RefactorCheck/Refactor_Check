public class nacos_0065 {

        @Override
        @NamespaceValidation
        @ExtractorManager.Extractor(rpcExtractor = PromptRequestParamExtractor.class)
        @Secured(action = ActionTypes.READ, signType = SignType.AI)
        public QueryPromptResponse handle(QueryPromptRequest request, RequestMeta meta) {
            request.setNamespaceId(NamespaceUtil.processNamespaceParameter(request.getNamespaceId()));
            if (StringUtils.isBlank(request.getPromptKey())) {
                QueryPromptResponse errorResponse = new QueryPromptResponse();
                errorResponse.setErrorInfo(NacosException.INVALID_PARAM,
                    "parameters `promptKey` can't be empty or null");
                return errorResponse;
            }
            QueryPromptResponse response = new QueryPromptResponse();
            try {
                PromptVersionInfo result = promptOperationService.queryPrompt(
                    request.getNamespaceId(), request.getPromptKey(), request.getVersion(),
                    request.getLabel(), request.getMd5());
                response.setPromptInfo(PromptConvertUtils.toClientPrompt(result));
            } catch (NacosException e) {
                if (e.getErrCode() == NacosException.NOT_MODIFIED) {
                    response.setErrorInfo(NacosException.NOT_MODIFIED, "prompt data is up to date");
                    return response;
                }
                LOGGER.error("Query prompt {} error: {}", request.getPromptKey(), e.getErrMsg());
                response.setErrorInfo(e.getErrCode(), e.getErrMsg());
            }
            return response;
        }
}
