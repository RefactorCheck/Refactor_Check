public class arthas_0151 {

        private Process createProcess(Session session, List<CliToken> line, InternalCommandManager commandManager, int jobId, Term term, ResultDistributor resultDistributor, final boolean useCache) {
            boolean cacheEnabled = useCache;
            try {
                ListIterator<CliToken> tokens = line.listIterator();
                while (tokens.hasNext()) {
                    CliToken token = tokens.next();
                    if (token.isText()) {
                        // check before create process
                        checkPermission(session, token);
                        Command command = commandManager.getCommand(token.value());
                        if (command != null) {
                            return createCommandProcess(command, tokens, jobId, term, resultDistributor);
                        } else {
                            throw new IllegalArgumentException(token.value() + ": command not found");
                        }
                    }
                }
                throw new IllegalArgumentException();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
}
