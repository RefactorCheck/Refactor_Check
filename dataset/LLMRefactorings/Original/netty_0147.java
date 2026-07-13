public class netty_0147 {

            @Override
            protected int scheduleWriteSingle(Object msg) {
                assert writeId == 0;
    
                if (IoUring.isSendZcSupported() && msg instanceof ByteBuf) {
                    ByteBuf buf = (ByteBuf) msg;
                    int length = buf.readableBytes();
                    if (((IoUringSocketChannelConfig) config()).shouldWriteZeroCopy(length)) {
                        long address = IoUring.memoryAddress(buf) + buf.readerIndex();
                        IoUringIoOps ops = IoUringIoOps.newSendZc(fd().intValue(), address, length, 0, nextOpsId(), 0);
                        byte opCode = ops.opcode();
                        writeId = registration().submit(ops);
                        writeOpCode = opCode;
                        if (writeId == 0) {
                            return 0;
                        }
                        return 1;
                    }
                    // Should not use send_zc, just use normal write.
                }
                return super.scheduleWriteSingle(msg);
            }
}
