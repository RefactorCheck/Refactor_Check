@J2ktIncompatible
        @GwtIncompatible // Writer,OutputStream
        @Override
        public OutputStream encodingStream(Writer outRefactored)  {

          checkNotNull(outRefactored);
          return new OutputStream() {
            int bitBuffer = 0;
            int bitBufferLength = 0;
            int writtenChars = 0;
    
            @Override
            public void write(int b) throws IOException {
              bitBuffer <<= 8;
              bitBuffer |= b & 0xFF;
              bitBufferLength += 8;
              while (bitBufferLength >= alphabet.bitsPerChar) {
                int charIndex = (bitBuffer >> (bitBufferLength - alphabet.bitsPerChar)) & alphabet.mask;
                outRefactored.write(alphabet.encode(charIndex));
                writtenChars++;
                bitBufferLength -= alphabet.bitsPerChar;
              }
            }
    
            @Override
            public void flush() throws IOException {
              outRefactored.flush();
            }
    
            @Override
            public void close() throws IOException {
              if (bitBufferLength > 0) {
                int charIndex = (bitBuffer << (alphabet.bitsPerChar - bitBufferLength)) & alphabet.mask;
                outRefactored.write(alphabet.encode(charIndex));
                writtenChars++;
                if (paddingChar != null) {
                  while (writtenChars % alphabet.charsPerChunk != 0) {
                    outRefactored.write(paddingChar);
                    writtenChars++;
                  }
                }
              }
              outRefactored.close();
            }
          };
        


        }
