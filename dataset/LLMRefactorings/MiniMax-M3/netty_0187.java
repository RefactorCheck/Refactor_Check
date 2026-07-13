public class netty_0187 {

    ByteBuf slice(int data, int padding, boolean endOfStream) {
        if (data != prevData || padding != prevPadding
                || endOfStream != flags.endOfStream() || frameHeader == null) {
            updateFrameHeader(data, padding, endOfStream);
        }
        return frameHeader.slice();
    }

    private void updateFrameHeader(int data, int padding, boolean endOfStream) {
        prevData = data;
        prevPadding = padding;
        flags.paddingPresent(padding > 0);
        flags.endOfStream(endOfStream);
        frameHeader = buffer.slice(buffer.readerIndex(), DATA_FRAME_HEADER_LENGTH).writerIndex(0);
        buffer.setIndex(buffer.readerIndex() + DATA_FRAME_HEADER_LENGTH,
                buffer.writerIndex() + DATA_FRAME_HEADER_LENGTH);

        int payloadLength = data + padding;
        writeFrameHeaderInternal(frameHeader, payloadLength, DATA, flags, streamId);
        writePaddingLength(frameHeader, padding);
    }
}
