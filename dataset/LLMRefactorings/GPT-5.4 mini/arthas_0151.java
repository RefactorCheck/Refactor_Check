public class arthas_0151 {

        private Process createProcess(Session session, List<CliToken> line, InternalCommandManager commandManager, int jobId, Term term, ResultDistributor resultDistributor) {
            try {
                while (line.listIterator().hasNext()) {
                    CliToken token = line.listIterator().next();
                    if (token.isText()) {
                        // check before create process
                        checkPermission(session, token);
                        Command command = commandManager.getCommand(token.value());
                        if (command != null) {
                            return createCommandProcess(command, line.listIterator(), jobId, term, resultDistributor);
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
