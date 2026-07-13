public class nacos_0141 {

        @Override
        public void insertConfigHistoryAtomic(long id, ConfigInfo configInfo, String srcIp,
            String srcUser,
            final Timestamp time, String ops, String publishType, String grayName, String extInfo) {
            String appNameTmp = StringUtils.defaultEmptyIfBlank(configInfo.getAppName());
            String tenantTmp = StringUtils.defaultEmptyIfBlank(configInfo.getTenant());
            final String md5Tmp = MD5Utils.md5Hex(configInfo.getContent(), Constants.ENCODE);
            String encryptedDataKey = StringUtils.defaultEmptyIfBlank(configInfo.getEncryptedDataKey());
            String publishTypeTmp = StringUtils.defaultEmptyIfBlank(publishType);
            String grayNameTemp = StringUtils.defaultEmptyIfBlank(grayName);

            performHistoryInsert(id, configInfo, srcIp, srcUser, time, ops, extInfo,
                appNameTmp, tenantTmp, md5Tmp, encryptedDataKey, publishTypeTmp, grayNameTemp);
        }

        private void performHistoryInsert(long id, ConfigInfo configInfo, String srcIp,
            String srcUser, Timestamp time, String ops, String extInfo, String appNameTmp,
            String tenantTmp, String md5Tmp, String encryptedDataKey, String publishTypeTmp,
            String grayNameTemp) {
            try {
                HistoryConfigInfoMapper historyConfigInfoMapper = mapperManager.findMapper(
                    dataSourceService.getDataSourceType(), TableConstant.HIS_CONFIG_INFO);
                jt.update(historyConfigInfoMapper.insert(
                    Arrays.asList("id", "data_id", "group_id", "tenant_id", "app_name", "content",
                        "md5", "src_ip",
                        "src_user", "gmt_modified", "op_type", "publish_type", "gray_name", "ext_info",
                        "encrypted_data_key")),
                    id, configInfo.getDataId(), configInfo.getGroup(), tenantTmp,
                    appNameTmp, configInfo.getContent(), md5Tmp, srcIp, srcUser, time, ops,
                    publishTypeTmp,
                    grayNameTemp, extInfo, encryptedDataKey);
            } catch (DataAccessException e) {
                LogUtil.FATAL_LOG.error("[db-error] " + e, e);
                throw e;
            }
        }
}
