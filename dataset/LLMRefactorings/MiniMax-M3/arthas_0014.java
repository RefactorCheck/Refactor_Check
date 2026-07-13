public class arthas_0014 {

        @Override
        public String draw() {
            final StringBuilder tableSB = new StringBuilder();

            final int[] widthCacheArray = new int[getColumnCount()];
            for (int index = 0; index < widthCacheArray.length; index++) {
                widthCacheArray[index] = abs(columnDefineArray[index].getWidth());
            }

            final int tableHigh = getTableHigh();
            for (int rowIndex = 0; rowIndex < tableHigh; rowIndex++) {
                appendRow(tableSB, widthCacheArray, rowIndex, tableHigh);
            }

            return tableSB.toString();
        }

        private void appendRow(StringBuilder tableSB, int[] widthCacheArray, int rowIndex, int tableHigh) {
            final boolean isFirstRow = rowIndex == 0;
            final boolean isLastRow = rowIndex == tableHigh - 1;

            if (isFirstRow
                    && hasBorder()
                    && isAnyBorder(BORDER_TOP)) {
                tableSB.append(drawSeparationLine(widthCacheArray)).append("\n");
            }

            if (!isFirstRow
                    && hasBorder()) {
                tableSB.append(drawSeparationLine(widthCacheArray)).append("\n");
            }

            tableSB.append(drawRow(widthCacheArray, rowIndex));

            if (isLastRow
                    && hasBorder()
                    && isAnyBorder(BORDER_BOTTOM)) {
                tableSB.append(drawSeparationLine(widthCacheArray)).append("\n");
            }
        }
}
