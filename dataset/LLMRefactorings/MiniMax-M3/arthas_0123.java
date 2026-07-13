public class arthas_0123 {

        private static boolean isOption(String value) {
            return value.startsWith("-") || value.startsWith("--");
        }

        public static int detectArgumentIndex(Completion completion) {
            List<CliToken> tokens = completion.lineTokens();
            CliToken lastToken = tokens.get(tokens.size() - 1);
    
            if (isOption(lastToken.value())) {
                return -1;
            }
    
            if (StringUtils.isBlank((lastToken.value())) && tokens.size() == 1) {
                return 1;
            }
    
            int tokenCount = 0;
    
            for (CliToken token : tokens) {
                if (StringUtils.isBlank(token.value()) || isOption(token.value())) {
                    continue;
                }
                tokenCount++;
            }
    
            if (StringUtils.isBlank((lastToken.value())) && tokens.size() != 1) {
                tokenCount++;
            }
            return tokenCount;
        }
}
