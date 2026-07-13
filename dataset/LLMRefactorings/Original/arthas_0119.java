public class arthas_0119 {

        @Override
        public synchronized void run(boolean fg) {
            if (processStatus != ExecStatus.READY) {
                throw new IllegalStateException("Cannot run proces in " + processStatus + " state");
            }
    
            processStatus = ExecStatus.RUNNING;
            processForeground = fg;
            foreground = fg;
            startTime = new Date();
    
            // Make a local copy
            final Tty tty = this.tty;
            if (tty == null) {
                throw new IllegalStateException("Cannot execute process without a TTY set");
            }
    
            process = new CommandProcessImpl(this, tty);
            if (resultDistributor == null) {
                resultDistributor = new TermResultDistributorImpl(process, ArthasBootstrap.getInstance().getResultViewResolver());
            }
    
            final List<String> args2 = new LinkedList<String>();
            for (CliToken arg : args) {
                if (arg.isText()) {
                    args2.add(arg.value());
                }
            }
    
            CommandLine cl = null;
            try {
                if (commandContext.cli() != null) {
                    if (commandContext.cli().parse(args2, false).isAskingForHelp()) {
                        appendResult(new HelpCommand().createHelpDetailModel(commandContext));
                        terminate();
                        return;
                    }
    
                    cl = commandContext.cli().parse(args2);
                    process.setArgs2(args2);
                    process.setCommandLine(cl);
                }
            } catch (CLIException e) {
                terminate(-10, null, e.getMessage());
                return;
            }
    
            if (cacheLocation() != null) {
                process.echoTips("job id  : " + this.jobId + "\n");
                process.echoTips("cache location  : " + cacheLocation() + "\n");
            }
            Runnable task = new CommandProcessTask(process);
            ArthasBootstrap.getInstance().execute(task);
        }
}
