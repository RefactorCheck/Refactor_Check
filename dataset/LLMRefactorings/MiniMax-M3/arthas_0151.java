public class arthas_0151 {

    private Process createProcess(Session session, List<CliToken> line, InternalCommandManager commandManager, int jobId, Term term, ResultDistributor resultDistributor) {
        try {
            ListIterator<CliToken> tokens = line.listIterator();
            while (tokens.hasNext()) {
                CliToken token = tokens.next();
                if (token.isText()) {
                    return resolveCommandProcess(session, token, commandManager, tokens, jobId, term, resultDistributor);
                }
            }
            throw new IllegalArgumentException();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Process resolveCommandProcess(Session session, CliToken token, InternalCommandManager commandManager, ListIterator<CliToken> tokens, int jobId, Term term, ResultDistributor resultDistributor) {
        // check before create process
        checkPermission(session, token);
        Command command = commandManager.getCommand(token.value());
        if (command == null) {
            throw new IllegalArgumentException(token.value() + ": command not found");
        }
        return createCommandProcess(command, tokens, jobId, term, resultDistributor);
    }
}
