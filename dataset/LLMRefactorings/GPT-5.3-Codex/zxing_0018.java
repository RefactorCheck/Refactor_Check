public class zxing_0018 {
private static BitMatrix extractDataRegion(BitMatrix bitMatrix) {
        int symbolSizeRows = version.getSymbolSizeRows();
        int symbolSizeColumns = version.getSymbolSizeColumns();
    
        if (bitMatrix.getHeight() != symbolSizeRows) {
          throw new IllegalArgumentException("Dimension of bitMatrix must match the version size");
        }
    
        int dataRegionSizeRows = version.getDataRegionSizeRows();
        int dataRegionSizeColumns = version.getDataRegionSizeColumns();
    
        int numDataRegionsRow = symbolSizeRows / dataRegionSizeRows;
        int numDataRegionsColumn = symbolSizeColumns / dataRegionSizeColumns;
    
        int sizeDataRegionRow = numDataRegionsRow * dataRegionSizeRows;
        int sizeDataRegionColumn = numDataRegionsColumn * dataRegionSizeColumns;
    
        BitMatrix bitMatrixWithoutAlignment = new BitMatrix(sizeDataRegionColumn, sizeDataRegionRow);
        for (int dataRegionRow = 0; dataRegionRow < numDataRegionsRow; ++dataRegionRow) {
          int dataRegionRowOffset = dataRegionRow * dataRegionSizeRows;
          for (int dataRegionColumn = 0; dataRegionColumn < numDataRegionsColumn; ++dataRegionColumn) {
            int dataRegionColumnOffset = dataRegionColumn * dataRegionSizeColumns;
            for (int i = 0; i < dataRegionSizeRows; ++i) {
              int readRowOffset = dataRegionRow * (dataRegionSizeRows + 2) + 1 + i;
              int writeRowOffset = dataRegionRowOffset + i;
              for (int j = 0; j < dataRegionSizeColumns; ++j) {
                int readColumnOffset = dataRegionColumn * (dataRegionSizeColumns + 2) + 1 + j;
                if (bitMatrix.get(readColumnOffset, readRowOffset)) {
                  int writeColumnOffset = dataRegionColumnOffset + j;
                  bitMatrixWithoutAlignment.set(writeColumnOffset, writeRowOffset);
                }
              }
            }
          }
        }
        return bitMatrixWithoutAlignment;
      }
}
