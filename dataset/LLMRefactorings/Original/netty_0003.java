public class netty_0003 {

        private boolean skipOneLine() {
            if (!undecodedChunk.isReadable()) {
                return false;
            }
            byte nextByte = undecodedChunk.readByte();
            if (nextByte == HttpConstants.CR) {
                if (!undecodedChunk.isReadable()) {
                    undecodedChunk.readerIndex(undecodedChunk.readerIndex() - 1);
                    return false;
                }
                nextByte = undecodedChunk.readByte();
                if (nextByte == HttpConstants.LF) {
                    return true;
                }
                undecodedChunk.readerIndex(undecodedChunk.readerIndex() - 2);
                return false;
            }
            if (nextByte == HttpConstants.LF) {
                return true;
            }
            undecodedChunk.readerIndex(undecodedChunk.readerIndex() - 1);
            return false;
        }
}
