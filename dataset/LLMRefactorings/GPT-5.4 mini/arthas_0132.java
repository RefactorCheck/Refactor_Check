public class arthas_0132 {

        private static void handleBackground(List<CliToken> tokens) {
            String arg = TokenUtils.findSecondTokenText(tokens);
            Job job;
            if (arg == null) {
                job = shell.getForegroundJob();
            } else {
                job = shell.jobController().getJob(getJobId(arg));
            }
            if (job == null) {
                term.write(arg + " : no such job\n");
                shell.readline();
            } else {
                if (job.status() == ExecStatus.STOPPED) {
                    job.resume(false);
                    term.echo(shell.statusLine(job, ExecStatus.RUNNING));
                    shell.readline();
                } else {
                    term.write("job " + job.id() + " is already running\n");
                    shell.readline();
                }
            }
        }
}
