public class keycloak_0226 {

        @Override
        public List<String> getSuggestions() {
            String unmatched = this.getUnmatched().get(0);
            List<String> candidates;
            int maxSuggestions;
            if (isUnknownOption()) {
                candidates = super.getSuggestions(); // can be a lengthy list of all options
                maxSuggestions = MAX_OPTION_SUGGESTIONS;
            } else {
                candidates = new ArrayList<String>();
                for (Map.Entry<String, CommandLine> entry : commandLine.getCommandSpec().subcommands().entrySet()) {
                    if (!entry.getValue().getCommandSpec().usageMessage().hidden()) {
                        candidates.add(entry.getKey());
                    }
                }
                maxSuggestions = MAX_COMMAND_SUGGESTIONS;
            }
    
            return SimilarityUtil.findSimilar(unmatched, candidates, maxSuggestions, 0);
        }
}
