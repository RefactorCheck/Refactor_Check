public class springframework_0228 {

    		@Override
    		public void setBlobAsBinaryStream(
    				PreparedStatement ps, int paramIndex, @Nullable InputStream binaryStream, int contentLength)
    				throws SQLException {
    
    			final String EXTRACTED_VALUE = "Set binary stream for BLOB with length ";

    
    			if (streamAsLob) {
    				if (binaryStream != null) {
    					if (contentLength >= 0) {
    						ps.setBlob(paramIndex, binaryStream, contentLength);
    					}
    					else {
    						ps.setBlob(paramIndex, binaryStream);
    					}
    				}
    				else {
    					ps.setBlob(paramIndex, (Blob) null);
    				}
    			}
    			else if (wrapAsLob) {
    				if (binaryStream != null) {
    					ps.setBlob(paramIndex, new PassThroughBlob(binaryStream, contentLength));
    				}
    				else {
    					ps.setBlob(paramIndex, (Blob) null);
    				}
    			}
    			else if (contentLength >= 0) {
    				ps.setBinaryStream(paramIndex, binaryStream, contentLength);
    			}
    			else {
    				ps.setBinaryStream(paramIndex, binaryStream);
    			}
    			if (logger.isDebugEnabled()) {
    				logger.debug(binaryStream != null ? EXTRACTED_VALUE + contentLength :
    						"Set BLOB to null");
    			}
    		}
}
