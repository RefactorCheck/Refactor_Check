public class dubbo_0215 {

        @Override
        public MetadataReport getMetadataReport(URL url) {
            url = url.setPath(MetadataReport.class.getName()).removeParameters(EXPORT_KEY, REFER_KEY);
            String key = url.toServiceString(NAMESPACE_KEY);
    
            MetadataReport metadataReport = serviceStoreMap.get(key);
            if (metadataReport != null) {
                return metadataReport;
            }
    
            lock.lock();
            try {
                metadataReport = serviceStoreMap.get(key);
                if (metadataReport != null) {
                    return metadataReport;
                }
                boolean check = url.getParameter(CHECK_KEY, true) && url.getPort() != 0;
                metadataReport = initMetadataReport(url, check);
                if (metadataReport != null) {
                    serviceStoreMap.put(key, metadataReport);
                }
                return metadataReport;
            } finally {
                lock.unlock();
            }
        }

        private MetadataReport initMetadataReport(URL url, boolean check) {
            MetadataReport metadataReport;
            try {
                metadataReport = createMetadataReport(url);
            } catch (Exception e) {
                if (!check) {
                    logger.warn(PROXY_FAILED_EXPORT_SERVICE, "", "", "The metadata reporter failed to initialize", e);
                } else {
                    throw e;
                }
                return null;
            }
            if (check && metadataReport == null) {
                throw new IllegalStateException("Can not create metadata Report " + url);
            }
            return metadataReport;
        }
}
