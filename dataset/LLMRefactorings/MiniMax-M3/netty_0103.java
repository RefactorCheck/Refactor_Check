public class netty_0103 {

        @Setup(Level.Trial)
        public void setup() {
            ByteBuf testContent = createTestContent();
    
            StompHeadersSubframe headersSubframe = ExampleStompHeadersSubframe.EXAMPLES.get(headersType);
            stompFrame = new DefaultStompFrame(headersSubframe.command(), testContent);
            stompFrame.headers().setAll(headersSubframe.headers());
    
            stompEncoder = new StompSubframeEncoder();
            context = new EmbeddedChannelWriteReleaseHandlerContext(
                    pooledAllocator? PooledByteBufAllocator.DEFAULT : UnpooledByteBufAllocator.DEFAULT, stompEncoder) {
                @Override
                protected void handleException(Throwable t) {
                    handleUnexpectedException(t);
                }
            };
        }
        
        private ByteBuf createTestContent() {
            byte[] bytes = new byte[contentLength];
            ThreadLocalRandom.current().nextBytes(bytes);
            content = Unpooled.wrappedBuffer(bytes);
            return Unpooled.unreleasableBuffer(content.asReadOnly());
        }
}
