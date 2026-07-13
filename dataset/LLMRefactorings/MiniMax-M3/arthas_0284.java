public class arthas_0284 {

        public static boolean completeClassName(Completion completion) {
            List<CliToken> tokens = completion.lineTokens();
            String lastToken = tokens.get(tokens.size() - 1).value();
    
            if (StringUtils.isBlank(lastToken)) {
                lastToken = "";
            }
    
            if (lastToken.startsWith("-")) {
                return false;
            }
    
            Instrumentation instrumentation = completion.session().getInstrumentation();
    
            Class<?>[] allLoadedClasses = instrumentation.getAllLoadedClasses();
    
            Set<String> result = new HashSet<String>();
            for(Class<?> clazz : allLoadedClasses) {
                addIfMatches(result, clazz.getName(), lastToken);
            }
    
            if(result.size() == 1 && result.iterator().next().endsWith(".")) {
                completion.complete(result.iterator().next().substring(lastToken.length()), false);
            }else {
                CompletionUtils.complete(completion, result);
            }
            return true;
        }

        private static void addIfMatches(Set<String> result, String name, String lastToken) {
            if (name.startsWith("[")) {
                return;
            }
            if(name.startsWith(lastToken)) {
                int index = name.indexOf('.', lastToken.length());
                if(index > 0) {
                    result.add(name.substring(0, index + 1));
                } else {
                    result.add(name);
                }
            }
        }
}
