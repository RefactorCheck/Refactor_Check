public class dubbo_0048 {

        @Override
        protected void encodeRequestData(Channel channel, ObjectOutput out, Object data, String version)
                throws IOException {
            RpcInvocation inv = (RpcInvocation) data;
    
            out.writeUTF(version);
            out.writeUTF(getServiceName(inv));
            out.writeUTF(inv.getAttachment(VERSION_KEY));
    
            out.writeUTF(inv.getMethodName());
            out.writeUTF(inv.getParameterTypesDesc());
            Object[] args = inv.getArguments();
            if (args != null) {
                for (int i = 0; i < args.length; i++) {
                    out.writeObject(callbackServiceCodec.encodeInvocationArgument(channel, inv, i));
                }
            }
            out.writeAttachments(inv.getObjectAttachments());
        }

        // https://github.com/apache/dubbo/issues/6138
        private String getServiceName(RpcInvocation inv) {
            String serviceName = inv.getAttachment(INTERFACE_KEY);
            if (serviceName == null) {
                serviceName = inv.getAttachment(PATH_KEY);
            }
            return serviceName;
        }
}
