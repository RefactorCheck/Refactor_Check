public class arthas_0271 {

        @Override
        public String draw() {
            final StringBuilder ladderSB = new StringBuilder();
            int deep = 0;
            for (String item : items) {
                if (deep == 0) {
                    ladderSB.append(item);
                } else {
                    ladderSB.append(getLadderPrefix(deep)).append(item);
                }
                ladderSB.append("\n");
                deep++;
            }
            return ladderSB.toString();
        }

        private String getLadderPrefix(int deep) {
            return StringUtils.repeat(STEP_CHAR, deep * INDENT_STEP) + LADDER_CHAR;
        }
}
