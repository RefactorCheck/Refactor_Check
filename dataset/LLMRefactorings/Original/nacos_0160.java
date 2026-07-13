public class nacos_0160 {

        @Override
        public List<ConfigAllInfo> removeConfigInfoByIds(final List<Long> ids, final String srcIp,
            final String srcUser) {
            if (CollectionUtils.isEmpty(ids)) {
                return null;
            }
            ids.removeAll(Collections.singleton(null));
            return tjt.execute(new TransactionCallback<List<ConfigAllInfo>>() {
                
                final Timestamp time = new Timestamp(System.currentTimeMillis());
                
                @Override
                public List<ConfigAllInfo> doInTransaction(TransactionStatus status) {
                    try {
                        String idsStr = StringUtils.join(ids, StringUtils.COMMA);
                        List<ConfigAllInfo> oldConfigAllInfoList =
                            findAllConfigInfo4Export(null, null, null, null, ids);
                        if (!CollectionUtils.isEmpty(oldConfigAllInfoList)) {
                            removeConfigInfoByIdsAtomic(idsStr);
                            for (ConfigAllInfo configAllInfo : oldConfigAllInfoList) {
                                removeTagByIdAtomic(configAllInfo.getId());
                                historyConfigInfoPersistService.insertConfigHistoryAtomic(
                                    configAllInfo.getId(),
                                    configAllInfo, srcIp, srcUser, time, "D", Constants.FORMAL, null,
                                    ConfigExtInfoUtil.getExtInfoFromAllInfo(configAllInfo));
                            }
                        }
                        return oldConfigAllInfoList;
                    } catch (CannotGetJdbcConnectionException e) {
                        LogUtil.FATAL_LOG.error("[db-error] " + e, e);
                        throw e;
                    }
                }
            });
        }
}
