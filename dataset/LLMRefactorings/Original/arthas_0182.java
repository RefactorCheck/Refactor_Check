public class arthas_0182 {

        private String escapeDecode(String string) {
    
            final StringBuilder segmentSB = new StringBuilder();
            final int stringLength = string.length();
    
            for (int index = 0; index < stringLength; index++) {
    
                final char c = string.charAt(index);
    
                if (isEquals(c, ESCAPE_PREFIX_CHAR)
                        && index < stringLength - 1) {
    
                    final char nextChar = string.charAt(++index);
    
                    // 下一个字符是转义符
                    if (isIn(nextChar, kvSegmentSeparator, kvSeparator, ESCAPE_PREFIX_CHAR)) {
                        segmentSB.append(nextChar);
                    }
    
                    // 如果不是转义字符，则需要两个都放入
                    else {
                        segmentSB.append(c);
                        segmentSB.append(nextChar);
                    }
                } else {
                    segmentSB.append(c);
                }
    
            }
    
            return segmentSB.toString();
        }
}
