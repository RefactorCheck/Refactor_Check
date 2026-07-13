public class nacos_0025 {

        @Since("3.0.0")
        @GetMapping
        @Secured(signType = SignType.CONFIG, apiType = ApiType.ADMIN_API)
        public Result<ConfigListenerInfo> getAllSubClientConfigByIp(@RequestParam("ip") String ip,
            @RequestParam(value = "all", required = false) boolean all,
            @RequestParam(value = "namespaceId", required = false) String namespaceId,
            AggregationForm aggregationForm) {
            ConfigListenerInfo result = configListenerStateDelegate.getListenerStateByIp(ip,
                aggregationForm.isAggregation());
            result.setQueryType(ConfigListenerInfo.QUERY_TYPE_IP);
            Map<String, String> configMd5Status = new HashMap<>(100);
            if (result.getListenersStatus() == null || result.getListenersStatus().isEmpty()) {
                return Result.success(result);
            }
            filterConfigMd5Status(result.getListenersStatus(), configMd5Status, namespaceId, all);
            result.setListenersStatus(configMd5Status);
            return Result.success(result);
        }

        private void filterConfigMd5Status(Map<String, String> status, Map<String, String> configMd5Status,
                String namespaceId, boolean all) {
            for (Map.Entry<String, String> config : status.entrySet()) {
                if (!StringUtils.isBlank(namespaceId) && config.getKey().contains(namespaceId)) {
                    configMd5Status.put(config.getKey(), config.getValue());
                    continue;
                }
                if (all) {
                    configMd5Status.put(config.getKey(), config.getValue());
                } else {
                    String[] configKeys = GroupKey2.parseKey(config.getKey());
                    if (StringUtils.isBlank(configKeys[2])) {
                        configMd5Status.put(config.getKey(), config.getValue());
                    }
                }
            }
        }
}
