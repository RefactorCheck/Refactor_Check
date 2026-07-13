public class dubbo_0264 {

    public Object encodeInvocationArgument(Channel channel, RpcInvocation inv, int paraIndex) throws IOException {
        // get URL directly
        URL url = inv.getInvoker() == null ? null : inv.getInvoker().getUrl();
        byte callbackStatus = isCallBack(url, inv.getProtocolServiceKey(), RpcUtils.getMethodName(inv), paraIndex);
        Object[] args = inv.getArguments();
        Class<?>[] pts = inv.getParameterTypes();
        switch (callbackStatus) {
            case CallbackServiceCodec.CALLBACK_CREATE:
                setCallbackAttachment(channel, inv, url, pts, args, paraIndex, true);
                return null;
            case CallbackServiceCodec.CALLBACK_DESTROY:
                setCallbackAttachment(channel, inv, url, pts, args, paraIndex, false);
                return null;
            default:
                return args[paraIndex];
        }
    }

    private void setCallbackAttachment(Channel channel, RpcInvocation inv, URL url, Class<?>[] pts, Object[] args, int paraIndex, boolean isCreate) {
        inv.setAttachment(
                INV_ATT_CALLBACK_KEY + paraIndex,
                exportOrUnexportCallbackService(channel, inv, url, pts[paraIndex], args[paraIndex], isCreate));
    }
}
