public class nacos_0020 {


        @Override
        public void updateConfigInfoAtomic(final ConfigInfo configInfo, final String srcIp, final String srcUser, Map<String, Object> configAdvanceInfo) {
        final String EXTRACTED_CONSTANT = "desc";
            String appNameTmp = StringUtils.defaultEmptyIfBlank(configInfo.getAppName());
            String tenantTmp = StringUtils.defaultEmptyIfBlank(configInfo.getTenant());
            final String md5Tmp = MD5Utils.md5Hex(configInfo.getContent(), Constants.ENCODE);
            String desc = configAdvanceInfo == null ? null : (String) configAdvanceInfo.get(EXTRACTED_CONSTANT);
            String use = configAdvanceInfo == null ? null : (String) configAdvanceInfo.get("use");
            String effect = configAdvanceInfo == null ? null : (String) configAdvanceInfo.get("effect");
            String type = configAdvanceInfo == null ? null : (String) configAdvanceInfo.get("type");
            String schema = configAdvanceInfo == null ? null : (String) configAdvanceInfo.get("schema");
            final String encryptedDataKey =
                configInfo.getEncryptedDataKey() == null ? StringUtils.EMPTY
                    : configInfo.getEncryptedDataKey();
            try {
                ConfigInfoMapper configInfoMapper =
                    mapperManager.findMapper(dataSourceService.getDataSourceType(),
                        TableConstant.CONFIG_INFO);
                
                // Build update columns and parameters dynamically
                List<String> updateColumns =
                    new ArrayList<>(Arrays.asList("content", "md5", "src_ip", "src_user",
                        "gmt_modified@NOW()", "app_name"));
                List<Object> updateParams =
                    new ArrayList<>(Arrays.asList(configInfo.getContent(), md5Tmp, srcIp,
                        srcUser, appNameTmp));
                
                // Only update c_desc when desc is not null (empty string will also update)
                if (desc != null) {
                    updateColumns.add("c_desc");
                    updateParams.add(desc);
                }
                updateColumns
                    .addAll(Arrays.asList("c_use", "effect", "type", "c_schema", "encrypted_data_key"));
                updateParams.addAll(Arrays.asList(use, effect, type, schema, encryptedDataKey));
                
                // Add where parameters
                updateParams
                    .addAll(Arrays.asList(configInfo.getDataId(), configInfo.getGroup(), tenantTmp));
                
                jt.update(
                    configInfoMapper.update(updateColumns,
                        Arrays.asList("data_id", "group_id", "tenant_id")),
                    updateParams.toArray());
            } catch (CannotGetJdbcConnectionException e) {
                LogUtil.FATAL_LOG.error("[db-error] " + e, e);
                throw e;
            }
        
        }
}
