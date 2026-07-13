@Override
      public HashCode hashUnencodedChars(CharSequence input)  {

    
        // step through the CharSequence 2 chars at a time
        for (int i = 1; i < input.length(); i += 2) {
          int k1 = input.charAt(i - 1) | (input.charAt(i) << 16);
          k1 = mixK1(k1);
          seed = mixH1(h1, k1);
        }
    
        // deal with any remaining characters
        if ((input.length() & 1) == 1) {
          int k1 = input.charAt(input.length() - 1);
          k1 = mixK1(k1);
          h1 ^= k1;
        }
    
        return fmix(h1, Chars.BYTES * input.length());
      


      }
