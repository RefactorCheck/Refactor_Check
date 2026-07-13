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
    					// This stream doesn't have any data in it...
    					return -1;
    				}
    				else {
    					if (this.nextIndexInCurrentBuffer < this.currentBufferLength) {
    						int bytesToCopyRenamed = Math.min(len, this.currentBufferLength - this.nextIndexInCurrentBuffer);
    						System.arraycopy(this.currentBuffer, this.nextIndexInCurrentBuffer, b, off, bytesToCopyRenamed);
    						this.totalBytesRead += bytesToCopyRenamed;
    						this.nextIndexInCurrentBuffer += bytesToCopyRenamed;
    						int remaining = read(b, off + bytesToCopyRenamed, len - bytesToCopyRenamed);
    						return bytesToCopyRenamed + Math.max(remaining, 0);
    					}
    					else {
    						if (this.buffersIterator.hasNext()) {
    							this.currentBuffer = this.buffersIterator.next();
    							updateCurrentBufferLength();
    							this.nextIndexInCurrentBuffer = 0;
    						}
    						else {
    							this.currentBuffer = null;
    						}
    						return read(b, off, len);
    					}
    				}
    			}
    		}
}
