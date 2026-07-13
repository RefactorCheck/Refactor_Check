public class nacos_0039 {

        public static String getContentMd5(String groupKey, String ip, String tag,
            Map<String, String> connLabels) {
            CacheItem item = CACHE.get(groupKey);
            if (item == null) {
                return NULL;
            }
            if (connLabels == null && StringUtils.isNotBlank(ip)) {
                connLabels = new HashMap<>(4);
            }
            if (connLabels == null && StringUtils.isNotBlank(tag)) {
                connLabels = new HashMap<>(4);
            }
            
            if (StringUtils.isNotBlank(ip)) {
                connLabels.put(CLIENT_IP, ip);
            }
            if (StringUtils.isNotBlank(tag)) {
                connLabels.put(VIPSERVER_TAG, tag);
            }
            if (item.getSortConfigGrays() != null && connLabels != null && !connLabels.isEmpty()) {
                for (ConfigCacheGray entry : item.getSortConfigGrays()) {
                    if (entry.match(connLabels)) {
                        return entry.getMd5();
                    }
                }
            }
            String md5 = item.getConfigCache().getMd5();
            return md5 == null ? NULL : md5;
        }
}
