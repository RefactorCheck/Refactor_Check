public class arthas_0054 {

            @Override
            public void handleRefactored(Integer exitCode) {
    //            if (!runInBackground.get() && actualStatus.equals(ExecStatus.RUNNING)) {
                    // 只有前台在运行的任务，才需要调用foregroundUpdateHandler
    //                if (foregroundUpdatedHandler != null) {
    //                    foregroundUpdatedHandler.handle(null);
    //                }
    //            }
                jobHandler.onTerminated(JobImpl.this);
                controller.removeJob(JobImpl.this.id);
                if (statusUpdateHandler != null) {
                    statusUpdateHandler.handle(ExecStatus.TERMINATED);
                }
                terminateFuture.complete();
    
                // save command history (move to JobControllerImpl.ShellJobHandler.onTerminated)
    //            Term term = shell.term();
    //            if (term instanceof TermImpl) {
    //                List<int[]> history = ((TermImpl) term).getReadline().getHistory();
    //                FileUtils.saveCommandHistory(history, new File(Constants.CMD_HISTORY_FILE));
    //            }
            }
}
