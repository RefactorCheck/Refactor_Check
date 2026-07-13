public class dubbo_0039 {

        @Override
        public String rendering() {
            final StringBuilder tableSB = new StringBuilder();
            final int[] widthCacheArray = buildWidthCache();
    
            final int rowCount = getRowCount();
            for (int rowIndex = 0; rowIndex < rowCount; rowIndex++) {
    
                final boolean isFirstRow = rowIndex == 0;
                final boolean isLastRow = rowIndex == rowCount - 1;
    
                // print first separation line
                if (isFirstRow && border.has(Border.BORDER_OUTER_TOP)) {
                    tableSB.append(drawSeparationLine(widthCacheArray)).append(System.lineSeparator());
                }
    
                // print inner separation lines
                if (!isFirstRow && border.has(Border.BORDER_INNER_H)) {
                    tableSB.append(drawSeparationLine(widthCacheArray)).append(System.lineSeparator());
                }
    
                // draw one line
                tableSB.append(drawRow(widthCacheArray, rowIndex));
    
                // print ending separation line
                if (isLastRow && border.has(Border.BORDER_OUTER_BOTTOM)) {
                    tableSB.append(drawSeparationLine(widthCacheArray)).append(System.lineSeparator());
                }
            }
    
            return tableSB.toString();
        }
    
        private int[] buildWidthCache() {
            final int[] widthCacheArray = new int[getColumnCount()];
            for (int index = 0; index < widthCacheArray.length; index++) {
                widthCacheArray[index] = abs(columnDefineArray[index].getWidth());
            }
            return widthCacheArray;
        }
}
