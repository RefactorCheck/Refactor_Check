public class arthas_0254 {

        @Override
        public void complete(Completion completion) {
            List<CliToken> tokens = completion.lineTokens();
            String token = tokens.get(tokens.size() - 1).value();
    
            if (tokens.size() >= 2) {
                CliToken cliToken_1 = tokens.get(tokens.size() - 1);
                CliToken cliToken_2 = tokens.get(tokens.size() - 2);
                if (cliToken_1.isBlank()) {
                    String token_2 = cliToken_2.value();
                    if (token_2.equals("-e") || token_2.equals("--event")) {
                        CompletionUtils.complete(completion, events());
                        return;
                    } else if (token_2.equals("-o") || token_2.equals("--format")) {
                        CompletionUtils.complete(completion, Arrays.asList(
                                "flamegraph", "tree", "jfr",
                                "flat", "traces", "collapsed",
                                "md", "md=10"
                        ));
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
