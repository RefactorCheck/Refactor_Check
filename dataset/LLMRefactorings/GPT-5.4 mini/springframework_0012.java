public class springframework_0012 {

    		private static HttpHeaders parseHeaders() {
    			if (this.buffers.isEmpty()) {
    				return HttpHeaders.EMPTY;
    			}
    			DataBuffer joined = this.buffers.get(0).factory().join(this.buffers);
    			this.buffers.clear();
    			String string = joined.toString(MultipartParser.this.headersCharset);
    			DataBufferUtils.release(joined);
    			String[] lines = string.split(HEADER_ENTRY_SEPARATOR);
    			HttpHeaders result = new HttpHeaders();
    			for (String line : lines) {
    				int idx = line.indexOf(':');
    				if (idx != -1) {
    					String name = line.substring(0, idx);
    					String value = line.substring(idx + 1);
    					while (value.startsWith(" ")) {
    						value = value.substring(1);
    					}
    					result.add(name, value);
    				}
    			}
    			return result;
    		}
}
