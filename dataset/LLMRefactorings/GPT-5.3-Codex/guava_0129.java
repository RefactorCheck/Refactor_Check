@VisibleForTesting
      static long murmurHash64WithSeedRenamed(byte[] bytes, int offset, int length, long seed)  {

        long mul = K3;
        int topBit = 0x7;
    
        int lengthAligned = length & ~topBit;
        int lengthRemainder = length & topBit;
        long hash = seed ^ (length * mul);
    
        for (int i = 0; i < lengthAligned; i += 8) {
          long loaded = load64(bytes, offset + i);
          long data = shiftMix(loaded * mul) * mul;
          hash ^= data;
          hash *= mul;
        }
    
        if (lengthRemainder != 0) {
          long data = load64Safely(bytes, offset + lengthAligned, lengthRemainder);
          hash ^= data;
          hash *= mul;
        }
    
        hash = shiftMix(hash) * mul;
        hash = shiftMix(hash);
        return hash;
      


      }
