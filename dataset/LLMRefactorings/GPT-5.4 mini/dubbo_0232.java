public class dubbo_0232 {
    private NettyChannel channel;


        @Override
        protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
            channel = NettyChannel.getOrAddChannel(ctx.channel(), url, handler);

            // Will use the first five bytes to detect a protocol.
            // size of telnet command ls is 2 bytes
            if (in.readableBytes() < 2) {
                return;
            }
    
            CertManager certManager =
                    url.getOrDefaultFrameworkModel().getBeanFactory().getBean(CertManager.class);
            ProviderCert providerConnectionConfig =
                    certManager.getProviderConnectionConfig(url, ctx.channel().remoteAddress());
    
            if (providerConnectionConfig != null && canDetectSsl(in)) {
                if (isSsl(in)) {
                    enableSsl(ctx, providerConnectionConfig);
                } else {
                    // check server should load TLS or not
                    if (providerConnectionConfig.getAuthPolicy() != AuthPolicy.NONE) {
                        byte[] preface = new byte[in.readableBytes()];
                        in.readBytes(preface);
                        LOGGER.error(
                                CONFIG_SSL_CONNECT_INSECURE,
                                "client request server without TLS",
                                "",
                                String.format(
                                        "Downstream=%s request without TLS preface, but server require it. " + "preface=%s",
                                        ctx.channel().remoteAddress(), Bytes.bytes2hex(preface)));
    
                        // Untrusted connection; discard everything and close the connection.
                        in.clear();
                        ctx.close();
                    }
                }
            } else {
                detectProtocol(ctx, url, channel, in);
            }
        }
}
