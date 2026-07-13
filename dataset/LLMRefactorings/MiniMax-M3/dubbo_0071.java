public class dubbo_0071 {

        @Override
        public String rendering() {
            final StringBuilder ladderSB = new StringBuilder();
            int deep = 0;
            for (String item : items) {
                appendIndentedItem(ladderSB, item, deep);
                deep++;
            }
            return ladderSB.toString();
        }

        private void appendIndentedItem(StringBuilder ladderSB, String item, int deep) {
            if (deep == 0) {
                ladderSB.append(item).append(System.lineSeparator());
            } else {
                ladderSB.append(repeat(STEP_CHAR, deep * INDENT_STEP))
                        .append(LADDER_CHAR)
                        .append(item)
                        .append(System.lineSeparator());
            }
        }
}
