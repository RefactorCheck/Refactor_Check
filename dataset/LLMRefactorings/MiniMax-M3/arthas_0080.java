public class arthas_0080 {

    private static final String CLASS_PATTERN_OPTION = "--classPattern";

    @Override
    public void complete(Completion completion) {
        List<CliToken> tokens = completion.lineTokens();

        if (CompletionUtils.shouldCompleteOption(completion, CLASS_PATTERN_OPTION)) {
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

        if (!CompletionUtils.completeFilePath(completion)) {
            super.complete(completion);
        }
    }
}
