public class zxing_0065 {

      private int readCorner4(int numRows, int numColumns) {
        int currentByte = 0;
        currentByte = readCornerBit(numRows - 3, 0, numRows, numColumns, currentByte);
        currentByte = readCornerBit(numRows - 2, 0, numRows, numColumns, currentByte);
        currentByte = readCornerBit(numRows - 1, 0, numRows, numColumns, currentByte);
        currentByte = readCornerBit(0, numColumns - 2, numRows, numColumns, currentByte);
        currentByte = readCornerBit(0, numColumns - 1, numRows, numColumns, currentByte);
        currentByte = readCornerBit(1, numColumns - 1, numRows, numColumns, currentByte);
        currentByte = readCornerBit(2, numColumns - 1, numRows, numColumns, currentByte);
        currentByte = readCornerBit(3, numColumns - 1, numRows, numColumns, currentByte);
        return currentByte;
      }

      private int readCornerBit(int row, int col, int numRows, int numColumns, int currentByte) {
        currentByte <<= 1;
        if (readModule(row, col, numRows, numColumns)) {
          currentByte |= 1;
        }
        return currentByte;
      }
}
