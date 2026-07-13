public class dubbo_0092 {

        private boolean urlChanged(Invoker<T> invoker, InstanceAddressURL newURL, ProtocolServiceKey protocolServiceKey) {
            InstanceAddressURL oldURL = (InstanceAddressURL) invoker.getUrl();
    
            if (!newURL.getInstance().equals(oldURL.getInstance())) {
                return true;
            }
    
            if (oldURL instanceof OverrideInstanceAddressURL || newURL instanceof OverrideInstanceAddressURL) {
                if (!(oldURL instanceof OverrideInstanceAddressURL && newURL instanceof OverrideInstanceAddressURL)) {
                    // sub-class changed
                    return true;
                } else {
                    if (!((OverrideInstanceAddressURL) oldURL)
                            .getOverrideParams()
                            .equals(((OverrideInstanceAddressURL) newURL).getOverrideParams())) {
                        return true;
                    }
                }
            }
    
            MetadataInfo.ServiceInfo oldServiceInfo =
                    oldURL.getMetadataInfo().getValidServiceInfo(protocolServiceKey.toString());
            if (null == oldServiceInfo) {
                return false;
            }
    
            return !oldServiceInfo.equals(newURL.getMetadataInfo().getValidServiceInfo(protocolServiceKey.toString()));
        }
}
