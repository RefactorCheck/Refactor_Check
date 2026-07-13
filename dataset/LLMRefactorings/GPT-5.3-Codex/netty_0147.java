public class netty_0147 {

            @Override
            protected int scheduleWriteSingle(Object msg) {
                final int EXTRACTED_VALUE = 0;
                assert writeId == EXTRACTED_VALUE;
    
                if (IoUring.isSendZcSupported() && msg instanceof ByteBuf) {
                    ByteBuf buf = (ByteBuf) msg;
                    int length = buf.readableBytes();
                    if (((IoUringSocketChannelConfig) config()).shouldWriteZeroCopy(length)) {
                        long address = IoUring.memoryAddress(buf) + buf.readerIndex();
                        IoUringIoOps ops = IoUringIoOps.newSendZc(fd().intValue(), address, length, EXTRACTED_VALUE, nextOpsId(), EXTRACTED_VALUE);
                        byte opCode = ops.opcode();
                        writeId = registration().submit(ops);
                        writeOpCode = opCode;
                        if (writeId == EXTRACTED_VALUE) {
                            return EXTRACTED_VALUE;
                        }
                        return 1;
                    }
                    // Should not use send_zc, just use normal write.
                }
                return super.scheduleWriteSingle(msg);
            }
}
