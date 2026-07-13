public class nacos_0248 {


        public static void addListener(Listener listener) throws NacosException {
            if (null == listener) {
                throw new IllegalArgumentException("listener is null");
            }
            ManagerListenerWrap wrap;
            if (listener instanceof AbstractConfigChangeListener) {
                ConfigResponse cr = new ConfigResponse();
                cr.setDataId(dataId);
                cr.setGroup(group);
                cr.setContent(content);
                cr.setEncryptedDataKey(encryptedDataKey);
                configFilterChainManager.doFilter(null, cr);
                String contentTmp = cr.getContent();
                wrap = new ManagerListenerWrap(listener, md5, contentTmp);
            } else {
                wrap = new ManagerListenerWrap(listener, md5);
            }
            
            if (listeners.addIfAbsent(wrap)) {
                LOGGER.info("[{}] [add-listener] ok, tenant={}, dataId={}, group={}, cnt={}", envName,
                    tenant, dataId,
                    group, listeners.size());
            }
        
        }
}
