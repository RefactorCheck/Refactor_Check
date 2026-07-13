public class dubbo_0092 {

    private boolean urlChanged(Invoker<T> invoker, InstanceAddressURL newURL, ProtocolServiceKey protocolServiceKey) {
        InstanceAddressURL oldURL = (InstanceAddressURL) invoker.getUrl();

        if (!newURL.getInstance().equals(oldURL.getInstance())) {
            return true;
        }

        if (hasOverrideUrlChanged(oldURL, newURL)) {
            return true;
        }

        MetadataInfo.ServiceInfo oldServiceInfo =
                oldURL.getMetadataInfo().getValidServiceInfo(protocolServiceKey.toString());
        if (null == oldServiceInfo) {
            return false;
        }

        return !oldServiceInfo.equals(newURL.getMetadataInfo().getValidServiceInfo(protocolServiceKey.toString()));
    }

    private boolean hasOverrideUrlChanged(InstanceAddressURL oldURL, InstanceAddressURL newURL) {
        if (oldURL instanceof OverrideInstanceAddressURL || newURL instanceof OverrideInstanceAddressURL) {
            if (!(oldURL instanceof OverrideInstanceAddressURL && newURL instanceof OverrideInstanceAddressURL)) {
                return true;
            } else {
                return !((OverrideInstanceAddressURL) oldURL)
                        .getOverrideParams()
                        .equals(((OverrideInstanceAddressURL) newURL).getOverrideParams());
            }
        }
        return false;
    }
}
