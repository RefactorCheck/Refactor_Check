public class nacos_0152 {

        private void logToggleOperation(String type, String name, String version, boolean online) {
            String operation = online ? AiResourceTraceService.OP_ONLINE_VERSION
                : AiResourceTraceService.OP_OFFLINE_VERSION;
            AiResourceTraceService.logSuccess(type, name, version, operation,
                VisibilityHelper.resolveCurrentIdentity(), VisibilityHelper.resolveClientIp());
        }

        public AiResourceVersion toggleVersionOnlineStatus(String namespaceId, AiResource meta,
            ResourceVersionInfo info, String version, boolean online) throws NacosException {
            String name = meta.getName();
            String type = meta.getType();
            AiResourceVersion v =
                aiResourceVersionPersistService.find(namespaceId, name, type, version);
            if (v == null) {
                throw new NacosApiException(NacosException.NOT_FOUND, ErrorCode.RESOURCE_NOT_FOUND,
                    type + " version not found: " + name + "@" + version);
            }
            String targetStatus = online ? AiResourceConstants.VERSION_STATUS_ONLINE
                : AiResourceConstants.VERSION_STATUS_OFFLINE;
            if (targetStatus.equalsIgnoreCase(v.getStatus())) {
                return null;
            }
            aiResourceVersionPersistService.updateStatus(namespaceId, name, type, version,
                targetStatus);
            updateVersionInfoCas(namespaceId, meta, info, latestInfo -> {
                Integer cnt = latestInfo.getOnlineCnt() == null ? 0 : latestInfo.getOnlineCnt();
                latestInfo.setOnlineCnt(online ? cnt + 1 : Math.max(0, cnt - 1));
                refreshLatestLabelForOnlineVersions(namespaceId, name, type, latestInfo);
                return latestInfo;
            });
            logToggleOperation(type, name, version, online);
            return v;
        }
}
