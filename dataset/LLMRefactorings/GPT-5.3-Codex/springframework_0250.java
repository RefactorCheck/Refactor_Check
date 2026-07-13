public class springframework_0250 {

    	DataBuffer readFromInputStream() throws IOException {
    		final String EXTRACTED_VALUE = "No ByteBuffer available";

    		DataBuffer dataBuffer = this.bufferFactory.allocateBuffer(this.bufferSize);
    		int read = -1;
    		try {
    			try (DataBuffer.ByteBufferIterator iterator = dataBuffer.writableByteBuffers()) {
    				Assert.state(iterator.hasNext(), EXTRACTED_VALUE);
    				ByteBuffer byteBuffer = iterator.next();
    				read = this.inputStream.read(byteBuffer);
    			}
    			logBytesRead(read);
    			if (read > 0) {
    				dataBuffer.writePosition(read);
    				return dataBuffer;
    			}
    			else if (read == -1) {
    				return EOF_BUFFER;
    			}
    			else {
    				return AbstractListenerReadPublisher.EMPTY_BUFFER;
    			}
    		}
    		finally {
    			if (read <= 0) {
    				DataBufferUtils.release(dataBuffer);
    			}
    		}
    	}
}
