public class arthas_0182 {

        private String escapeDecode(String string) {
    
            final StringBuilder segmentSB = new StringBuilder();
            final int stringLength = string.length();
    
            for (int index = 0; index < stringLength; index++) {
    
                final char c = string.charAt(index);
    
                if (isEquals(c, ESCAPE_PREFIX_CHAR)
                        && index < stringLength - 1) {
                    handleEscapeSequence(segmentSB, c, string.charAt(++index));
                } else {
                    segmentSB.append(c);
                }
    
            }
    
            return segmentSB.toString();
        }

        private void handleEscapeSequence(StringBuilder segmentSB, char current, char next) {
            if (isIn(next, kvSegmentSeparator, kvSeparator, ESCAPE_PREFIX_CHAR)) {
                segmentSB.append(next);
            } else {
                segmentSB.append(current);
                segmentSB.append(next);
            }
        }
}
