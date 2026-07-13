public class arthas_0048 {

        @Override
        public int read(byte buffer[], int offset, int length) throws IOException
        {
            if (length < 1) {
                return 0;
            }
    
            length = adjustLength(length);
    
            int ch = read();
            if (ch == EOF) {
                return EOF;
            }
    
            int off = offset;
    
            do {
                buffer[offset++] = (byte)ch;
            } while (--length > 0 && (ch = read()) != EOF);
    
            return (offset - off);
        }
    
        private int adjustLength(int length) {
            synchronized (__queue) {
                return Math.min(length, __bytesAvailable);
            }
        }
}
