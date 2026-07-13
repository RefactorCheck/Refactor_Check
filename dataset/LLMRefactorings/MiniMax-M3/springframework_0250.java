public class springframework_0250 {

    DataBuffer readFromInputStream() throws IOException {
        DataBuffer dataBuffer = this.bufferFactory.allocateBuffer(this.bufferSize);
        int bytesRead = -1;
        try {
            try (DataBuffer.ByteBufferIterator iterator = dataBuffer.writableByteBuffers()) {
                Assert.state(iterator.hasNext(), "No ByteBuffer available");
                ByteBuffer byteBuffer = iterator.next();
                bytesRead = this.inputStream.read(byteBuffer);
            }
            logBytesRead(bytesRead);
            if (bytesRead > 0) {
                dataBuffer.writePosition(bytesRead);
                return dataBuffer;
            }
            else if (bytesRead == -1) {
                return EOF_BUFFER;
            }
            else {
                return AbstractListenerReadPublisher.EMPTY_BUFFER;
            }
        }
        finally {
            if (bytesRead <= 0) {
                DataBufferUtils.release(dataBuffer);
            }
        }
    }
}
