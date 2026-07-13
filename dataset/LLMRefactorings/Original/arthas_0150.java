public class arthas_0150 {

        @Override
        public void complete(List<String> candidates) {
            String lastToken = tokens.isEmpty() ? null : tokens.get(tokens.size() - 1).value();
            if(StringUtils.isBlank(lastToken)) {
                lastToken = "";
            }
            if (candidates.size() > 1) {
                // complete common prefix
                String commonPrefix = CompletionUtils.findLongestCommonPrefix(candidates);
                if (commonPrefix.length() > 0) {
                    if (!commonPrefix.equals(lastToken)) {
                        // only complete if the common prefix is longer than the last token
                        if (commonPrefix.length() > lastToken.length()) {
                            String strToComplete = commonPrefix.substring(lastToken.length());
                            completion.complete(io.termd.core.util.Helper.toCodePoints(strToComplete), false);
                            return;
                        }
                    }
                }
            }
            if (candidates.size() > 0) {
                List<int[]> suggestions = new LinkedList<int[]>();
                for (String candidate : candidates) {
                    suggestions.add(io.termd.core.util.Helper.toCodePoints(candidate));
                }
                completion.suggest(suggestions);
            } else {
                completion.end();
            }
        }
}
