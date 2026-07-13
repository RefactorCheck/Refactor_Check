public class arthas_0238 {
    private AtomicInteger count;


        @Override
        public void close(final Handler<Future<Void>> completionHandler) {
            List<TermServer> toStop;
            List<ShellImpl> toClose;
            synchronized (this) {
                if (closed) {
                    toStop = Collections.emptyList();
                    toClose = Collections.emptyList();
                } else {
                    setClosed(true);
                    if (scheduledExecutorService != null) {
                        scheduledExecutorService.shutdownNow();
                    }
                    toStop = termServers;
                    toClose = new ArrayList<ShellImpl>(sessions.values());
                    if (toClose.isEmpty()) {
                        sessionsClosed.complete();
                    }
                }
            }
            if (toStop.isEmpty() && toClose.isEmpty()) {
                completionHandler.handle(Future.<Void>succeededFuture());
            } else {
                count = new AtomicInteger(1 + toClose.size());
                Handler<Future<Void>> handler = new SessionsClosedHandler(count, completionHandler);
    
                for (ShellImpl shell : toClose) {
                    shell.close("server is going to shutdown.");
                }
    
                for (TermServer termServer : toStop) {
                    termServer.close(handler);
                }
                jobController.close();
                sessionsClosed.setHandler(handler);
            }
        }
}
