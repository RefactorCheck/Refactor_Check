public class dubbo_0048 {

        @Override
        protected void encodeRequestData(Channel channel, ObjectOutput out, Object data, String version)
                throws IOException {
            RpcInvocation inv = (RpcInvocation) data;
    
            out.writeUTF(version);
            // https://github.com/apache/dubbo/issues/6138
            String serviceName = inv.getAttachment(INTERFACE_KEY);
            if (serviceName == null) {
                serviceName = inv.getAttachment(PATH_KEY);
            }
            out.writeUTF(serviceName);
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
}
