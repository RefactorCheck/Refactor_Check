public class netty_0029 {

    private void flushBufferedData(ByteBuf out) {
        int flushableBytes = buffer.readableBytes();
        if (flushableBytes == 0) {
            return;
        }
        checksum.reset();
        checksum.update(buffer, buffer.readerIndex(), flushableBytes);
        final int check = (int) checksum.getValue();

        final int bufSize = compressor.maxCompressedLength(flushableBytes) + HEADER_LENGTH;
        out.ensureWritable(bufSize);
        final int idx = out.writerIndex();
        int compressedLength = compressBuffer(idx, flushableBytes, out);

        final int blockType;
        if (compressedLength >= flushableBytes) {
            blockType = BLOCK_TYPE_NON_COMPRESSED;
            compressedLength = flushableBytes;
            out.setBytes(idx + HEADER_LENGTH, buffer, buffer.readerIndex(), flushableBytes);
        } else {
            blockType = BLOCK_TYPE_COMPRESSED;
        }

        out.setLong(idx, MAGIC_NUMBER);
        out.setByte(idx + TOKEN_OFFSET, (byte) (blockType | compressionLevel));
        out.setIntLE(idx + COMPRESSED_LENGTH_OFFSET, compressedLength);
        out.setIntLE(idx + DECOMPRESSED_LENGTH_OFFSET, flushableBytes);
        out.setIntLE(idx + CHECKSUM_OFFSET, check);
        out.writerIndex(idx + HEADER_LENGTH + compressedLength);
        buffer.clear();
    }

    private int compressBuffer(int idx, int flushableBytes, ByteBuf out) {
        try {
            ByteBuffer outNioBuffer = out.internalNioBuffer(idx + HEADER_LENGTH, out.writableBytes() - HEADER_LENGTH);
            int pos = outNioBuffer.position();
            compressor.compress(buffer.internalNioBuffer(buffer.readerIndex(), flushableBytes), outNioBuffer);
            return outNioBuffer.position() - pos;
        } catch (LZ4Exception e) {
            throw new CompressionException(e);
        }
    }
}
