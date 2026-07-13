public class arthas_0200 {

        private void updateStatus(ExecStatus statusUpdate, Integer exitCodeUpdate, boolean foregroundUpdate,
                                  Handler<Void> handler, Handler<Integer> terminatedHandler,
                                  Handler<Void> completionHandler) {
            processStatus = statusUpdate;
            exitCode = exitCodeUpdate;
            if (!foregroundUpdate) {
                if (processForeground) {
                    processForeground = false;
                    if (stdinHandler != null) {
                        tty.stdinHandler(null);
                    }
                    if (resizeHandler != null) {
                        tty.resizehandler(null);
                    }
                }
            } else {
                if (!processForeground) {
                    processForeground = true;
                    if (stdinHandler != null) {
                        tty.stdinHandler(stdinHandler);
                    }
                    if (resizeHandler != null) {
                        tty.resizehandler(resizeHandler);
                    }
                }
            }
    
            foreground = foregroundUpdate;
            try {
                if (handler != null) {
                    handler.handle(null);
                }
            } finally {
                if (completionHandler != null) {
                    completionHandler.handle(null);
                }
                if (terminatedHandler != null && statusUpdate == ExecStatus.TERMINATED) {
                    terminatedHandler.handle(exitCodeUpdate);
                }
            }
        }
}
