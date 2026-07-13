public class netty_0187 {

                ByteBuf slice(int data, int padding, boolean endOfStream) {
                    // Since we're reusing the current frame header whenever possible, check if anything changed
                    // that requires a new header.
                    if (data != prevData || padding != prevPadding
                            || endOfStream != flags.endOfStream() || frameHeader == null) {
                        // Update the header state.
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
                    return frameHeader.slice();
                }
}
