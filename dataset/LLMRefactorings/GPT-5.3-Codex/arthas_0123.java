public class arthas_0123 {
        private List<CliToken> tokens;


        public static int detectArgumentIndex(Completion completion) {
            tokens = completion.lineTokens();
            CliToken lastToken = tokens.get(tokens.size() - 1);
    
            if (lastToken.value().startsWith("-") || lastToken.value().startsWith("--")) {
                return -1;
            }
    
            if (StringUtils.isBlank((lastToken.value())) && tokens.size() == 1) {
                return 1;
            }
    
            int tokenCount = 0;
    
            for (CliToken token : tokens) {
                if (StringUtils.isBlank(token.value()) || token.value().startsWith("-") || token.value().startsWith("--")) {
                    // filter irrelevant tokens
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
