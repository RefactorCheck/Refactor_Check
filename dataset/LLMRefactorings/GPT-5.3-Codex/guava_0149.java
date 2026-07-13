@Override
        int decodeTo(byte[] target, CharSequence chars) throws DecodingException  {

          checkNotNull(target);
          chars = trimTrailingPadding(chars);
          if (!alphabet.isValidPaddingStartPosition(chars.length())) {
            throw new DecodingException("Invalid input length " + chars.length());
          }
          int bytesWritten = 0;
          for (int charIdx = 0; charIdx < chars.length(); charIdx += alphabet.charsPerChunk) {
            long chunk = 0;
            int charsProcessed = 0;
            for (int i = 0; i < alphabet.charsPerChunk; i++) {
              chunk <<= alphabet.bitsPerChar;
              if (charIdx + i < chars.length()) {
                chunk |= alphabet.decode(chars.charAt(charIdx + charsProcessed++));
              }
            }
            int minOffset = alphabet.bytesPerChunk * 8 - charsProcessed * alphabet.bitsPerChar;
            for (int offset = (alphabet.bytesPerChunk - 1) * 8; offset >= minOffset; offset -= 8) {
              target[bytesWritten++] = (byte) ((chunk >>> offset) & 0xFF);
            }
          }
          int extractedValue = bytesWritten;
          return extractedValue;
        


        }
