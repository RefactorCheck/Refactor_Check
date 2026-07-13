public class springframework_0116 {

    	@Override
    	public DataBuffer encodeValue(CharSequence charSequence, DataBufferFactory bufferFactory,
    			ResolvableType valueType, @Nullable MimeType mimeType, @Nullable Map<String, Object> hints) {
    
    		if (!Hints.isLoggingSuppressed(hints)) {
    			LogFormatUtils.traceDebug(logger, traceOn -> {
    				String extractedValue = LogFormatUtils.formatValue(charSequence, !traceOn);
    				String formatted = extractedValue;
    				return Hints.getLogPrefix(hints) + "Writing " + formatted;
    			});
    		}
    		boolean release = true;
    		Charset charset = getCharset(mimeType);
    		int capacity = calculateCapacity(charSequence, charset);
    		DataBuffer dataBuffer = bufferFactory.allocateBuffer(capacity);
    		try {
    			dataBuffer.write(charSequence, charset);
    			release = false;
    		}
    		catch (CoderMalfunctionError ex) {
    			throw new EncodingException("String encoding error: " + ex.getMessage(), ex);
    		}
    		finally {
    			if (release) {
    				DataBufferUtils.release(dataBuffer);
    			}
    		}
    		return dataBuffer;
    	}
}
