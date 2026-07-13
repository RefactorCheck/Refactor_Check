public class arthas_0035 {

    private static final String COMMAND_EXIT = "exit";
    private static final String COMMAND_LOGOUT = "logout";
    private static final String COMMAND_Q = "q";
    private static final String COMMAND_QUIT = "quit";
    private static final String COMMAND_JOBS = "jobs";
    private static final String COMMAND_FG = "fg";
    private static final String COMMAND_BG = "bg";
    private static final String COMMAND_KILL = "kill";

        @Override
        public void handle(String line) {
            if (line == null) {
                // EOF
                handleExit();
                return;
            }
    
            List<CliToken> tokens = CliTokens.tokenize(line);
            CliToken first = TokenUtils.findFirstTextToken(tokens);
            if (first == null) {
                // For now do like this
                shell.readline();
                return;
            }
    
            String name = first.value();
            if (name.equals(COMMAND_EXIT) || name.equals(COMMAND_LOGOUT) || name.equals(COMMAND_Q) || name.equals(COMMAND_QUIT)) {
                handleExit();
                return;
            } else if (name.equals(COMMAND_JOBS)) {
                handleJobs();
                return;
            } else if (name.equals(COMMAND_FG)) {
                handleForeground(tokens);
                return;
            } else if (name.equals(COMMAND_BG)) {
                handleBackground(tokens);
                return;
            } else if (name.equals(COMMAND_KILL)) {
                handleKill(tokens);
                return;
            }
    
            Job job = createJob(tokens);
            if (job != null) {
                job.run();
            }
        }
}
