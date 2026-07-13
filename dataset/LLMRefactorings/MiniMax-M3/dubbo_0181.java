public class dubbo_0181 {

    Invoker<?> getInvoker(Channel channel, Invocation inv) throws RemotingException {
        boolean isCallBackServiceInvoke;
        boolean isStubServiceInvoke;
        int port = channel.getLocalAddress().getPort();
        String path = (String) inv.getObjectAttachmentWithoutConvert(PATH_KEY);
        if (path == null) {
            throw new RemotingException(
                    channel,
                    "Service path is missing from the invocation, which indicates the invocation metadata is "
                            + "missing or corrupted. Possible causes include a request decode failure "
                            + "(e.g. parameter types that failed to deserialize), an incompatible protocol "
                            + "version, or a custom codec/invocation implementation that does not set the path, "
                            + "channel: " + channel.getRemoteAddress() + " --> " + channel.getLocalAddress());
        }

        // if it's stub service on client side(after enable stubevent, usually is set up onconnect or ondisconnect
        // method)
        isStubServiceInvoke = Boolean.TRUE.toString().equals(inv.getObjectAttachmentWithoutConvert(STUB_EVENT_KEY));
        if (isStubServiceInvoke) {
            // when a stub service export to local, it usually can't be exposed to port
            port = 0;
        }

        // if it's callback service on client side
        isCallBackServiceInvoke = isClientSide(channel) && !isStubServiceInvoke;
        if (isCallBackServiceInvoke) {
            path += "." + inv.getObjectAttachmentWithoutConvert(CALLBACK_SERVICE_KEY);
            inv.setObjectAttachment(IS_CALLBACK_SERVICE_INVOKE, Boolean.TRUE.toString());
        }

        String serviceKey = serviceKey(port, path, (String) inv.getObjectAttachmentWithoutConvert(VERSION_KEY), (String)
                inv.getObjectAttachmentWithoutConvert(GROUP_KEY));
        DubboExporter<?> exporter = getExporter(channel, serviceKey, inv);

        Invoker<?> invoker = exporter.getInvoker();
        inv.setServiceModel(invoker.getUrl().getServiceModel());
        return invoker;
    }

    private DubboExporter<?> getExporter(Channel channel, String serviceKey, Invocation inv) throws RemotingException {
        DubboExporter<?> exporter = (DubboExporter<?>) exporterMap.get(serviceKey);
        if (exporter == null) {
            throw new RemotingException(
                    channel,
                    "Not found exported service: " + serviceKey + " in " + exporterMap.keySet()
                            + ", may be version or group mismatch " + ", channel: consumer: "
                            + channel.getRemoteAddress() + " --> provider: " + channel.getLocalAddress() + ", message:"
                            + getInvocationWithoutData(inv));
        }
        return exporter;
    }
}
