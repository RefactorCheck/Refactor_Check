public class arthas_0187 {

        private boolean writeResponse(byte[] out, MessageFramer.Type type) {
            if (isTrailerSent) {
                logger.error("grpcweb trailer sented, writeResponse can not be called, framer type: {}", type);
                return false;
            }
    
            try {
                // PUNT multiple frames not handled
                byte[] prefix = new MessageFramer().getPrefix(out, type);
                ByteArrayOutputStream oStream = new ByteArrayOutputStream();
                // binary encode if it is "text" content type
                if (MessageUtils.getContentType(contentType) == ContentType.GRPC_WEB_TEXT) {
                    byte[] concated = new byte[out.length + 5];
                    System.arraycopy(prefix, 0, concated, 0, 5);
                    System.arraycopy(out, 0, concated, 5, out.length);
                    oStream.write(Base64.getEncoder().encode(concated));
                } else {
                    oStream.write(prefix);
                    oStream.write(out);
                }
    
                byte[] byteArray = oStream.toByteArray();
    
                InputStream dataStream = new ByteArrayInputStream(byteArray);
                ChunkedStream chunkedStream = new ChunkedStream(dataStream);
                SingleHttpChunkedInput httpChunkedInput = new SingleHttpChunkedInput(chunkedStream);
                ChannelFuture channelFuture = ctx.writeAndFlush(httpChunkedInput);
                ChannelFutureListener channelFutureListener = new ChannelFutureListener() {
                    @Override
                    public void operationComplete(ChannelFuture future) {
                        if (!future.isSuccess()) {
                            // 写入操作失败
                            isSuccessSendData = false;
                        }
                    }
                };
                channelFuture.addListener(channelFutureListener);
                return isSuccessSendData;
    
            } catch (IOException e) {
                logger.error("write grpcweb response error, framer type: {}", type, e);
                return false;
            }
        }
}
