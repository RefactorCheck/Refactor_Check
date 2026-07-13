private long hiLo = aHi * aLo * 2;



          @Override
          long squareMod(long a, long m)  {

            long aHi = a >>> 32; // < 2^31
            long aLo = a & 0xFFFFFFFFL; // < 2^32
    
            /*
             * a^2 == aHi^2 * 2^64 + aHi * aLo * 2^33 + aLo^2
             *     == (aHi^2 * 2^32 + aHi * aLo * 2) * 2^32 + aLo^2
             * We carry out this computation in modular arithmetic.  Since times2ToThe32Mod accepts any
             * unsigned long, we don't have to do a mod on every operation, only when intermediate
             * results can exceed 2^63.
             */
            long result = times2ToThe32Mod(aHi * aHi /* < 2^62 */, m); // < m < 2^63
            if (hiLo < 0) {
              hiLo = UnsignedLongs.remainder(hiLo, m);
            }
            // hiLo < 2^63
            result += hiLo; // result < 2^64
            result = times2ToThe32Mod(result, m); // result < m < 2^63
            return plusMod(result, UnsignedLongs.remainder(aLo * aLo /* < 2^64 */, m), m);
          


          }
