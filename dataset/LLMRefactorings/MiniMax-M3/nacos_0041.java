public class nacos_0041 {

        public AiResourceImportExecuteResponse execute(AiResourceImportExecuteRequest request)
            throws NacosException {
            requireRequest(request);
            requireSelectedItems(request.getSelectedItems());
            AiResourceImportSource source = null;
            try {
                source = sourceManager.resolveSource(request.getSourceId(), request.getResourceType());
                securityGuard.checkSourceEndpoint(source);
                AiResourceImportService importer =
                    pluginManager.resolveImporter(source, request.getResourceType());
                AiResourceImportContext context = buildItemContext(source, request.getNamespaceId(),
                    request.getResourceType(), request.getOptions());
                List<AiResourceImportResultItem> results = processSelectedItems(source, importer, context,
                    request);
                AiResourceImportExecuteResponse response = buildExecuteResponse(results);
                traceSourceOperation(source, request.getResourceType(),
                    AiResourceTraceService.OP_IMPORT_EXECUTE, executeTraceStatus(response),
                    executeTraceExt(source, request, response));
                return response;
            } catch (NacosException e) {
                traceSourceOperation(source, request.getResourceType(),
                    AiResourceTraceService.OP_IMPORT_EXECUTE, AiResourceTraceService.STATUS_FAILURE,
                    failureTraceExt(source, request.getSourceId(), e.getErrMsg()));
                throw e;
            }
        }

        private List<AiResourceImportResultItem> processSelectedItems(AiResourceImportSource source,
            AiResourceImportService importer, AiResourceImportContext context,
            AiResourceImportExecuteRequest request) {
            List<AiResourceImportResultItem> results = new ArrayList<>();
            for (AiResourceImportItem each : request.getSelectedItems()) {
                AiResourceImportResultItem result = executeItem(source, importer, context, each,
                    request.isOverwriteExisting(), request.isSkipInvalid());
                traceExecuteResult(source, context.getResourceType(), each, result);
                results.add(result);
            }
            return results;
        }
}
