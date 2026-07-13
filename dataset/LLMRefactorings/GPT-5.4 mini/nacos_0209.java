public class nacos_0209 {

        @Override
        public void removeConfigInfoGrayRefactored(final String dataId, final String group, final String tenant,
            final String grayName, final String srcIp, final String srcUser) {
            String tenantTmp = StringUtils.isBlank(tenant) ? StringUtils.EMPTY : tenant;
            String grayNameTmp = StringUtils.isBlank(grayName) ? StringUtils.EMPTY : grayName;
            
            ConfigInfoGrayWrapper oldConfigAllInfo4Gray =
                findConfigInfo4Gray(dataId, group, tenantTmp, grayNameTmp);
            if (oldConfigAllInfo4Gray == null) {
                if (LogUtil.FATAL_LOG.isErrorEnabled()) {
                    LogUtil.FATAL_LOG.error(
                        "expected config info[dataid:{}, group:{}, tenent:{}] but not found.", dataId,
                        group, tenant);
                }
            }
            
            ConfigInfoGrayMapper configInfoGrayMapper =
                mapperManager.findMapper(dataSourceService.getDataSourceType(),
                    TableConstant.CONFIG_INFO_GRAY);
            final String sql = configInfoGrayMapper
                .delete(Arrays.asList("data_id", "group_id", "tenant_id", "gray_name"));
            final Object[] args = new Object[] {dataId, group, tenantTmp, grayNameTmp};
            
            Timestamp now = new Timestamp(System.currentTimeMillis());
            historyConfigInfoPersistService.insertConfigHistoryAtomic(oldConfigAllInfo4Gray.getId(),
                oldConfigAllInfo4Gray,
                srcIp, srcUser, now, "D", Constants.GRAY, grayNameTmp,
                ConfigExtInfoUtil.getExtInfoFromGrayInfo(oldConfigAllInfo4Gray.getGrayName(),
                    oldConfigAllInfo4Gray.getGrayRule(), oldConfigAllInfo4Gray.getSrcUser()));
            
            EmbeddedStorageContextUtils.onDeleteConfigGrayInfo(tenantTmp, group, dataId, grayNameTmp,
                srcIp);
            EmbeddedStorageContextHolder.addSqlContext(sql, args);
            try {
                databaseOperate.update(EmbeddedStorageContextHolder.getCurrentSqlContext());
            } finally {
                EmbeddedStorageContextHolder.cleanAllContext();
            }
        }
}
