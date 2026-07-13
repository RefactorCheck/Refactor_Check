public class dubbo_0282 {

        @Override
        public String getServiceMethodParameter(String protocolServiceKey, String method, String key) {
            if (consumerParamFirst(key)) {
                URL consumerUrl = RpcContext.getServiceContext().getConsumerUrl();
                if (consumerUrl != null) {
                    String v = consumerUrl.getServiceMethodParameter(protocolServiceKey, method, key);
                    if (StringUtils.isNotEmpty(v)) {
                        return v;
                    }
                }
            }
    
            MetadataInfo.ServiceInfo serviceInfo = getServiceInfo(protocolServiceKey);
            if (null == serviceInfo) {
                return getParameter(key);
            }
    
            String value = serviceInfo.getMethodParameter(method, key, null);
            if (StringUtils.isNotEmpty(value)) {
                return value;
            }
            return getParameter(key);
        }
}
