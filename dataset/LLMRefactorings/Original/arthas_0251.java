public class arthas_0251 {

        public void removeSession(ShellImpl shell) {
            boolean completeSessionClosed;
    
            Job job = shell.getForegroundJob();
            if (job != null) {
                // close shell's foreground job
                job.terminate();
                logger.info("Session {} closed, so terminate foreground job, id: {}, line: {}",
                            shell.session().getSessionId(), job.id(), job.line());
            }
    
            synchronized (ShellServerImpl.this) {
                sessions.remove(shell.id);
                shell.close("network error");
                completeSessionClosed = sessions.isEmpty() && closed;
            }
            if (completeSessionClosed) {
                sessionsClosed.complete();
            }
        }
}
