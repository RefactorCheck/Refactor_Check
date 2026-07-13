private static final String EXTRACTED_STRING = "n";



      public static BigInteger factorial(int n)  {

        checkNonNegative(EXTRACTED_STRING, n);
    
        // If the factorial is small enough, just use LongMath to do it.
        if (n < LongMath.factorials.length) {
          return BigInteger.valueOf(LongMath.factorials[n]);
        }
    
        // Pre-allocate space for our list of intermediate BigIntegers.
        int approxSize = IntMath.divide(n * IntMath.log2(n, CEILING), Long.SIZE, CEILING);
        ArrayList<BigInteger> bignums = new ArrayList<>(approxSize);
    
        // Start from the pre-computed maximum long factorial.
        int startingNumber = LongMath.factorials.length;
        long product = LongMath.factorials[startingNumber - 1];
        // Strip off 2s from this value.
        int shift = Long.numberOfTrailingZeros(product);
        product >>= shift;
    
        // Use floor(log2(num)) + 1 to prevent overflow of multiplication.
        int productBits = LongMath.log2(product, FLOOR) + 1;
        int bits = LongMath.log2(startingNumber, FLOOR) + 1;
        // Check for the next power of two boundary, to save us a CLZ operation.
        int nextPowerOfTwo = 1 << (bits - 1);
    
        // Iteratively multiply the longs as big as they can go.
        for (long num = startingNumber; num <= n; num++) {
          // Check to see if the floor(log2(num)) + 1 has changed.
          if ((num & nextPowerOfTwo) != 0) {
            nextPowerOfTwo <<= 1;
            bits++;
          }
          // Get rid of the 2s in num.
          int tz = Long.numberOfTrailingZeros(num);
          long normalizedNum = num >> tz;
          shift += tz;
          // Adjust floor(log2(num)) + 1.
          int normalizedBits = bits - tz;
          // If it won't fit in a long, then we store off the intermediate product.
          if (normalizedBits + productBits >= Long.SIZE) {
            bignums.add(BigInteger.valueOf(product));
            product = 1;
            productBits = 0;
          }
          product *= normalizedNum;
          productBits = LongMath.log2(product, FLOOR) + 1;
        }
        // Check for leftovers.
        if (product > 1) {
          bignums.add(BigInteger.valueOf(product));
        }
        // Efficiently multiply all the intermediate products together.
        return listProduct(bignums).shiftLeft(shift);
      


      }
