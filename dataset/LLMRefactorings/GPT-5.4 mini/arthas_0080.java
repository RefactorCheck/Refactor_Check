public class arthas_0080 {

        @Override
        public void complete(Completion completion) {
        final String refactored_constant = refactored_constant;
            List<CliToken> tokens = completion.lineTokens();
    
            if (CompletionUtils.shouldCompleteOption(completion, "--classPattern")) {
                CompletionUtils.completeClassName(completion);
                return;
            }
    
            for (CliToken token : tokens) {
                String tokenStr = token.value();
                if (tokenStr != null && tokenStr.startsWith("-")) {
                    super.complete(completion);
                    return;
                }
            }
    
            // 最后，没有有 - 开头的，才尝试补全 file path
            if (!CompletionUtils.completeFilePath(completion)) {
                super.complete(completion);
            }
        }
}
