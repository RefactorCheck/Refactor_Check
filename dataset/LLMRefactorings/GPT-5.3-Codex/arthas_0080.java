public class arthas_0080 {
            private static final String COMPLETE_VALUE = "--classPattern";


        @Override
        public void complete(Completion completion) {
            List<CliToken> tokens = completion.lineTokens();
    
            if (CompletionUtils.shouldCompleteOption(completion, COMPLETE_VALUE)) {
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
