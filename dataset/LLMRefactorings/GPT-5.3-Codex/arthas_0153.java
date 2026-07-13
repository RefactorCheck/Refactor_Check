public class arthas_0153 {
        private List<CliToken> tokens;


        private boolean completeBeanName(Completion completion) {
            tokens = completion.lineTokens();
            String lastToken = TokenUtils.getLast(tokens).value();
    
            if (StringUtils.isBlank(lastToken)) {
                lastToken = "";
            }
    
            if (lastToken.startsWith("-") || lastToken.startsWith("--")) {
                return false;
            }
    
            Set<ObjectName> objectNames = queryObjectNames();
            Set<String> names = new HashSet<String>();
    
            if (objectNames == null) {
                return false;
            }
            for (ObjectName objectName : objectNames) {
                String name = objectName.toString();
                if (name.startsWith(lastToken)) {
                    int index = name.indexOf('.', lastToken.length());
                    if (index > 0) {
                        names.add(name.substring(0, index + 1));
                        continue;
                    }
                    index = name.indexOf(':', lastToken.length());
                    if (index > 0) {
                        names.add(name.substring(0, index + 1));
                        continue;
                    }
                    names.add(name);
                }
            }
            String next = names.iterator().next();
            if (names.size() == 1 && (next.endsWith(".") || next.endsWith(":"))) {
                completion.complete(next.substring(lastToken.length()), false);
                return true;
            } else {
                return CompletionUtils.complete(completion, names);
            }
        }
}
