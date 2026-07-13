public class zxing_0247 {

      private int readCorner1(int numRows, int numColumns) {
        int currentByte = 0;
        currentByte = appendBit(currentByte, readModule(numRows - 1, 0, numRows, numColumns));
        currentByte = appendBit(currentByte, readModule(numRows - 1, 1, numRows, numColumns));
        currentByte = appendBit(currentByte, readModule(numRows - 1, 2, numRows, numColumns));
        currentByte = appendBit(currentByte, readModule(0, numColumns - 2, numRows, numColumns));
        currentByte = appendBit(currentByte, readModule(0, numColumns - 1, numRows, numColumns));
        currentByte = appendBit(currentByte, readModule(1, numColumns - 1, numRows, numColumns));
        currentByte = appendBit(currentByte, readModule(2, numColumns - 1, numRows, numColumns));
        currentByte = appendBit(currentByte, readModule(3, numColumns - 1, numRows, numColumns));
        return currentByte;
      }

      private int appendBit(int value, boolean bit) {
        return (value << 1) | (bit ? 1 : 0);
      }
}
