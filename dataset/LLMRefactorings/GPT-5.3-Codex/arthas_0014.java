public class arthas_0014 {

        @Override
        public static String draw() {
            final StringBuilder tableSB = new StringBuilder();
    
            // init width cache
            final int[] widthCacheArray = new int[getColumnCount()];
            for (int index = 0; index < widthCacheArray.length; index++) {
                widthCacheArray[index] = abs(columnDefineArray[index].getWidth());
            }
    
            final int tableHigh = getTableHigh();
            for (int rowIndex = 0; rowIndex < tableHigh; rowIndex++) {
    
                final boolean isFirstRow = rowIndex == 0;
                final boolean isLastRow = rowIndex == tableHigh - 1;
    
                // 打印首分隔行
                if (isFirstRow
                        && hasBorder()
                        && isAnyBorder(BORDER_TOP)) {
                    tableSB.append(drawSeparationLine(widthCacheArray)).append("\n");
                }
    
                // 打印内部分割行
                if (!isFirstRow
                        && hasBorder()) {
                    tableSB.append(drawSeparationLine(widthCacheArray)).append("\n");
                }
    
                // 绘一行
                tableSB.append(drawRow(widthCacheArray, rowIndex));
    
    
                // 打印结尾分隔行
                if (isLastRow
                        && hasBorder()
                        && isAnyBorder(BORDER_BOTTOM)) {
                    // 打印分割行
                    tableSB.append(drawSeparationLine(widthCacheArray)).append("\n");
                }
    
            }
    
    
            return tableSB.toString();
        }
}
