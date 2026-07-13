public class springframework_0228 {

		@Override
		public void setBlobAsBinaryStream(
				PreparedStatement ps, int paramIndex, @Nullable InputStream binaryStream, int contentLength)
				throws SQLException {

			if (streamAsLob) {
				setLobStream(ps, paramIndex, binaryStream, contentLength);
			}
			else if (wrapAsLob) {
				setWrappedLobStream(ps, paramIndex, binaryStream, contentLength);
			}
			else {
				setBinaryStream(ps, paramIndex, binaryStream, contentLength);
			}
			if (logger.isDebugEnabled()) {
				logger.debug(binaryStream != null ? "Set binary stream for BLOB with length " + contentLength :
						"Set BLOB to null");
			}
		}

		private void setLobStream(PreparedStatement ps, int paramIndex, InputStream binaryStream, int contentLength) throws SQLException {
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

		private void setWrappedLobStream(PreparedStatement ps, int paramIndex, InputStream binaryStream, int contentLength) throws SQLException {
			if (binaryStream != null) {
				ps.setBlob(paramIndex, new PassThroughBlob(binaryStream, contentLength));
			}
			else {
				ps.setBlob(paramIndex, (Blob) null);
			}
		}

		private void setBinaryStream(PreparedStatement ps, int paramIndex, InputStream binaryStream, int contentLength) throws SQLException {
			if (contentLength >= 0) {
				ps.setBinaryStream(paramIndex, binaryStream, contentLength);
			}
			else {
				ps.setBinaryStream(paramIndex, binaryStream);
			}
		}
}
