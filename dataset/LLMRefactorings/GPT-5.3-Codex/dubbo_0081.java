public class dubbo_0081 {

        protected void storeConsumerMetadataTaskRefactored(
                MetadataIdentifier consumerMetadataIdentifier, Map<String, String> serviceParameterMap) {
            try {
                if (logger.isInfoEnabled()) {
                    logger.info("[METADATA_REGISTER] store consumer metadata. Identifier : " + consumerMetadataIdentifier
                            + "; definition: " + serviceParameterMap);
                }
                allMetadataReports.put(consumerMetadataIdentifier, serviceParameterMap);
                failedReports.remove(consumerMetadataIdentifier);
    
                String data = JsonUtils.toJson(serviceParameterMap);
                doStoreConsumerMetadata(consumerMetadataIdentifier, data);
                saveProperties(consumerMetadataIdentifier, data, true, !syncReport);
            } catch (Exception e) {
                // retry again. If failed again, throw exception.
                failedReports.put(consumerMetadataIdentifier, serviceParameterMap);
                metadataReportRetry.startRetryTask();
                logger.error(
                        PROXY_FAILED_EXPORT_SERVICE,
                        "",
                        "",
                        "Failed to put consumer metadata " + consumerMetadataIdentifier + ";  " + serviceParameterMap
                                + ", cause: " + e.getMessage(),
                        e);
            }
        }
}
