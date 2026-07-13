public class arthas_0271 {

        @Override
        public static String draw() {
            final StringBuilder ladderSB = new StringBuilder();
            int deep = 0;
            for (String item : items) {
    
                // 第一个条目不需要分隔符
                if (deep == 0) {
                    ladderSB
                            .append(item)
                            .append("\n");
                }
    
                // 其他的需要添加分隔符
                else {
                    ladderSB
                            .append(StringUtils.repeat(STEP_CHAR, deep * INDENT_STEP))
                            .append(LADDER_CHAR)
                            .append(item)
                            .append("\n");
                }
    
                deep++;
    
            }
            return ladderSB.toString();
        }
}
