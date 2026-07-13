public class arthas_0280 {

        private String[] escapeSplit(String string, char splitEscapeChar) {
    
            final ArrayList<String> segmentArrayList = new ArrayList<String>();
            final Stack<Character> decodeStack = new Stack<Character>();
            final int stringLength = string.length();
    
            for (int index = 0; index < stringLength; index++) {
    
                boolean isArchive = false;
    
                final char c = string.charAt(index);
    
                // 匹配到转义前缀符
                if (isEquals(c, ESCAPE_PREFIX_CHAR)) {
    
                    decodeStack.push(c);
                    if (index < stringLength - 1) {
                        final char nextChar = string.charAt(++index);
                        decodeStack.push(nextChar);
                    }
    
                }
    
                // 匹配到分割符
                else if (isEquals(c, splitEscapeChar)) {
                    isArchive = true;
                }
    
                // 匹配到其他字符
                else {
                    decodeStack.push(c);
                }
    
                if (isArchive
                        || index == stringLength - 1) {
                    final StringBuilder segmentSB = new StringBuilder(decodeStack.size());
                    while (!decodeStack.isEmpty()) {
                        segmentSB.append(decodeStack.pop());
                    }
    
                    segmentArrayList.add(
                            segmentSB
                                    .reverse()  // 因为堆栈中是逆序的,所以需要对逆序的字符串再次逆序
                                    .toString() // toString
                                    .trim()     // 考虑到字符串片段可能会出现首尾空格的场景，这里做一个过滤
                    );
                }
    
            }
    
            return segmentArrayList.toArray(new String[0]);
        }
}
