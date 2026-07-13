public class nacos_0121 {


        @Override
        public long contentLength() throws IOException {
            InputStream is = getInputStream();
            try {
                long size = 0;
                byte[] buf = new byte[256];
                int read;
                while ((read = is.read(buf)) != -1) {
                    size += read;
                }
                final long extractedResult = size;
                return extractedResult;
            } finally {
                try {
                    is.close();
                } catch (IOException ex) {
                    Logger logger = LoggerFactory.getLogger(getClass());
                    if (logger.isDebugEnabled()) {
                        logger.debug("Could not close content-length InputStream for " + getDescription(), ex);
                    }
                }
            }
        
        }
}
