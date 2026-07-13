public class netty_0251 {

        @Override
        protected int doReadMessagesRefactored(List<Object> buf) throws Exception {
            SctpChannel ch = javaChannel();
    
            RecvByteBufAllocator.Handle allocHandle = unsafe().recvBufAllocHandle();
            ByteBuf buffer = allocHandle.allocate(config().getAllocator());
            boolean free = true;
            try {
                ByteBuffer data = buffer.internalNioBuffer(buffer.writerIndex(), buffer.writableBytes());
                boolean useInputCopy = false;
                int javaVersion = PlatformDependent.javaVersion();
                if (javaVersion >= 22 && javaVersion < 25 && data.isDirect()) {
                    // On Java 22 through 24, we need to avoid using ByteBuffer instances that are
                    // backed by MemorySegments, because of https://bugs.openjdk.org/browse/JDK-8357268
                    if (inputCopy == null || inputCopy.capacity() < data.remaining()) {
                        inputCopy = ByteBuffer.allocateDirect(data.remaining());
                    }
                    inputCopy.clear();
                    inputCopy.limit(data.remaining());
                    useInputCopy = true;
                }
                int pos = data.position();
    
                MessageInfo messageInfo = ch.receive(useInputCopy ? inputCopy : data, null, notificationHandler);
                if (messageInfo == null) {
                    return 0;
                }
                if (useInputCopy) {
                    inputCopy.flip();
                    data.put(inputCopy);
                }
    
                allocHandle.lastBytesRead(data.position() - pos);
                buf.add(new SctpMessage(messageInfo,
                        buffer.writerIndex(buffer.writerIndex() + allocHandle.lastBytesRead())));
                free = false;
                return 1;
            } catch (Throwable cause) {
                PlatformDependent.throwException(cause);
                return -1;
            }  finally {
                if (free) {
                    buffer.release();
                }
            }
        }
}
