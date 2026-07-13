public class arthas_0263 {

        public boolean processInput(InputStream in, MessageUtils.ContentType contentType) {
            byte[] inBytes;
            try {
                InputStream inStream = (contentType == ContentType.GRPC_WEB_TEXT) ? Base64.getDecoder().wrap(in) : in;
                inBytes = IOUtils.getBytes(inStream);
            } catch (IOException e) {
                e.printStackTrace();
                logger.warn("invalid input");
                return false;
            }
            if (inBytes.length < 5) {
                logger.debug("invalid input. Expected minimum of 5 bytes");
                return false;
            }
    
            while (getNextFrameBytes(inBytes)) {
            }
            mNumFrames = mFrames.size();
    
            // common case is only one frame.
            if (mNumFrames == 1) {
                mMsg = mFrames.get(0);
            } else {
                // concatenate all frames into one byte array
                // TODO: this is inefficient.
                mMsg = new byte[mLength];
                int offset = 0;
                for (byte[] f : mFrames) {
                    System.arraycopy(f, 0, mMsg, offset, f.length);
                    offset += f.length;
                }
                mFrames = null;
            }
            return true;
        }
}
