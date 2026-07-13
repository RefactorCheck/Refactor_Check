public class guava_0126 {

      public static BigInteger factorial(int n) {
        checkNonNegative("n", n);
    
        if (n < LongMath.factorials.length) {
          return BigInteger.valueOf(LongMath.factorials[n]);
        }
    
        int approxSize = IntMath.divide(n * IntMath.log2(n, CEILING), Long.SIZE, CEILING);
        ArrayList<BigInteger> bignums = new ArrayList<>(approxSize);
    
        int shift = multiplyInRange(n, bignums);
    
        return listProduct(bignums).shiftLeft(shift);
      }
    
      private static int multiplyInRange(int n, ArrayList<BigInteger> bignums) {
        int startingNumber = LongMath.factorials.length;
        long product = LongMath.factorials[startingNumber - 1];
        int shift = Long.numberOfTrailingZeros(product);
        product >>= shift;
    
        int productBits = LongMath.log2(product, FLOOR) + 1;
        int bits = LongMath.log2(startingNumber, FLOOR) + 1;
        int nextPowerOfTwo = 1 << (bits - 1);
    
        for (long num = startingNumber; num <= n; num++) {
          if ((num & nextPowerOfTwo) != 0) {
            nextPowerOfTwo <<= 1;
            bits++;
          }
          int tz = Long.numberOfTrailingZeros(num);
          long normalizedNum = num >> tz;
          shift += tz;
          int normalizedBits = bits - tz;
          if (normalizedBits + productBits >= Long.SIZE) {
            bignums.add(BigInteger.valueOf(product));
            product = 1;
            productBits = 0;
          }
          product *= normalizedNum;
          productBits = LongMath.log2(product, FLOOR) + 1;
        }
        if (product > 1) {
          bignums.add(BigInteger.valueOf(product));
        }
        return shift;
      }
}
