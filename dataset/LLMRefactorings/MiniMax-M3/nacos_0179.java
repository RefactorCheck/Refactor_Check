public class nacos_0179 {

    @Override
    public List<ConfigAllInfo> doInTransaction(TransactionStatus status) {
        try {
            String idsStr = StringUtils.join(ids, StringUtils.COMMA);
            List<ConfigAllInfo> oldConfigAllInfoList =
                findAllConfigInfo4Export(null, null, null, null, ids);
            if (!CollectionUtils.isEmpty(oldConfigAllInfoList)) {
                removeConfigInfoByIdsAtomic(idsStr);
                for (ConfigAllInfo configAllInfo : oldConfigAllInfoList) {
                    persistRemovedConfigHistory(configAllInfo);
                }
            }
            return oldConfigAllInfoList;
        } catch (CannotGetJdbcConnectionException e) {
            LogUtil.FATAL_LOG.error("[db-error] " + e, e);
            throw e;
        }
    }

    private void persistRemovedConfigHistory(ConfigAllInfo configAllInfo) {
        removeTagByIdAtomic(configAllInfo.getId());
        historyConfigInfoPersistService.insertConfigHistoryAtomic(
            configAllInfo.getId(),
            configAllInfo, srcIp, srcUser, time, "D", Constants.FORMAL, null,
            ConfigExtInfoUtil.getExtInfoFromAllInfo(configAllInfo));
    }
}
