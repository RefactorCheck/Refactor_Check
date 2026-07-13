public class springframework_0282 {

		@Override
		public int read(byte[] b, int off, int len) {
			if (off < 0 || len < 0 || len > b.length - off) {
				throw new IndexOutOfBoundsException();
			}
			else if (len == 0) {
				return 0;
			}
			else {
				if (this.currentBuffer == null) {
					return -1;
				}
				else {
					if (this.nextIndexInCurrentBuffer < this.currentBufferLength) {
						return copyFromCurrentBuffer(b, off, len);
					}
					else {
						advanceBuffer();
						return read(b, off, len);
					}
				}
			}
		}

		private int copyFromCurrentBuffer(byte[] b, int off, int len) {
			int bytesToCopy = Math.min(len, this.currentBufferLength - this.nextIndexInCurrentBuffer);
			System.arraycopy(this.currentBuffer, this.nextIndexInCurrentBuffer, b, off, bytesToCopy);
			this.totalBytesRead += bytesToCopy;
			this.nextIndexInCurrentBuffer += bytesToCopy;
			int remaining = read(b, off + bytesToCopy, len - bytesToCopy);
			return bytesToCopy + Math.max(remaining, 0);
		}

		private void advanceBuffer() {
			if (this.buffersIterator.hasNext()) {
				this.currentBuffer = this.buffersIterator.next();
				updateCurrentBufferLength();
				this.nextIndexInCurrentBuffer = 0;
			}
			else {
				this.currentBuffer = null;
			}
		}
}
