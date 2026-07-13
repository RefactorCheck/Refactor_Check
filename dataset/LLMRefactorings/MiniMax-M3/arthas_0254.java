public class arthas_0254 {

    private static final String OPTION_EVENT_SHORT = "-e";
    private static final String OPTION_EVENT_LONG = "--event";
    private static final String OPTION_FORMAT_SHORT = "-o";
    private static final String OPTION_FORMAT_LONG = "--format";
    private static final List<String> FORMAT_VALUES = Arrays.asList(
            "flamegraph", "tree", "jfr",
            "flat", "traces", "collapsed",
            "md", "md=10"
    );

    @Override
    public void complete(Completion completion) {
        List<CliToken> tokens = completion.lineTokens();
        String token = tokens.get(tokens.size() - 1).value();

        if (tokens.size() >= 2) {
            CliToken cliToken_1 = tokens.get(tokens.size() - 1);
            CliToken cliToken_2 = tokens.get(tokens.size() - 2);
            if (cliToken_1.isBlank()) {
                String token_2 = cliToken_2.value();
                if (token_2.equals(OPTION_EVENT_SHORT) || token_2.equals(OPTION_EVENT_LONG)) {
                    CompletionUtils.complete(completion, events());
                    return;
                } else if (token_2.equals(OPTION_FORMAT_SHORT) || token_2.equals(OPTION_FORMAT_LONG)) {
                    CompletionUtils.complete(completion, FORMAT_VALUES);
                    return;
                }
            }
        }

        if (token.startsWith("-")) {
            super.complete(completion);
            return;
        }

        CompletionUtils.complete(completion, actions());
    }
}
