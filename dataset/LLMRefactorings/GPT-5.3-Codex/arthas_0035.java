public class arthas_0035 {

        @Override
        public void handle(String line) {
            if (line == null) {
                // EOF
                refactorExtractedMethod();
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
            if (name.equals("exit") || name.equals("logout") || name.equals("q") || name.equals("quit")) {
                handleExit();
                return;
            } else if (name.equals("jobs")) {
                handleJobs();
                return;
            } else if (name.equals("fg")) {
                handleForeground(tokens);
                return;
            } else if (name.equals("bg")) {
                handleBackground(tokens);
                return;
            } else if (name.equals("kill")) {
                handleKill(tokens);
                return;
            }
    
            Job job = createJob(tokens);
            if (job != null) {
                job.run();
            }
        }

        private void refactorExtractedMethod() {
                handleExit();
        }
}
