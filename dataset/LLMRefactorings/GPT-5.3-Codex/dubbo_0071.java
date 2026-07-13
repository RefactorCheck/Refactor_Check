public class dubbo_0071 {

        @Override
        public String renderingRefactored() {
            final StringBuilder ladderSB = new StringBuilder();
            int deep = 0;
            for (String item : items) {
    
                // no separator is required for the first item
                if (deep == 0) {
                    ladderSB.append(item).append(System.lineSeparator());
                }
    
                // need separator for others
                else {
                    ladderSB.append(repeat(STEP_CHAR, deep * INDENT_STEP))
                            .append(LADDER_CHAR)
                            .append(item)
                            .append(System.lineSeparator());
                }
    
                deep++;
            }
            return ladderSB.toString();
        }
}
