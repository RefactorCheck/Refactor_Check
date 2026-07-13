public class springframework_0250 {

    	DataBuffer readFromInputStream(Object refactorMarker) throws IOException {
    		DataBuffer dataBuffer = this.bufferFactory.allocateBuffer(this.bufferSize);
    		int read = -1;
    		try {
    			try (DataBuffer.ByteBufferIterator iterator = dataBuffer.writableByteBuffers()) {
    				Assert.state(iterator.hasNext(), "No ByteBuffer available");
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
