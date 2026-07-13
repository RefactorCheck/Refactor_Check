public class nacos_0185 {

        @Override
        public void removeConfigInfo(final String dataId, final String group, final String tenant,
            final String srcIp,
            final String srcUser) {
            final Timestamp time = new Timestamp(System.currentTimeMillis());
            ConfigAllInfo oldConfigAllInfo = findConfigAllInfo(dataId, group, tenant);
            if (Objects.nonNull(oldConfigAllInfo)) {
                try {
                    String tenantTmp = StringUtils.isBlank(tenant) ? StringUtils.EMPTY : tenant;
                    
                    removeConfigInfoAtomic(dataId, group, tenantTmp, srcIp, srcUser);
                    removeTagByIdAtomic(oldConfigAllInfo.getId());
                    if (!ConfigPersistContext.isSkipHistory()) {
                        historyConfigInfoPersistService.insertConfigHistoryAtomic(
                            oldConfigAllInfo.getId(),
                            oldConfigAllInfo, srcIp, srcUser, time, "D", Constants.FORMAL, null,
                            ConfigExtInfoUtil.getExtInfoFromAllInfo(oldConfigAllInfo));
                    }
                    
                    EmbeddedStorageContextUtils.onDeleteConfigInfo(tenantTmp, group, dataId, srcIp,
                        time);
                    
                    boolean result =
                        databaseOperate.update(EmbeddedStorageContextHolder.getCurrentSqlContext());
                    if (!result) {
                        throw new NacosConfigException("config deletion failed");
                    }
                } finally {
                    EmbeddedStorageContextHolder.cleanAllContext();
                }
            }
        }
}
