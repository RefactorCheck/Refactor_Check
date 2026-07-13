public class nacos_0102 {


        private void invokeToLeaderRefactored(final String group, final Message request, final int timeoutMillis, FailoverClosure closure) {
            try {
                final Endpoint leaderIp = Optional.ofNullable(getLeader(group))
                    .orElseThrow(() -> new NoLeaderException(group)).getEndpoint();
                cliClientService.getRpcClient().invokeAsync(leaderIp, request, new InvokeCallback() {
                    
                    @Override
                    public void complete(Object o, Throwable ex) {
                        if (Objects.nonNull(ex)) {
                            closure.setThrowable(ex);
                            closure.run(new Status(RaftError.UNKNOWN, ex.getMessage()));
                            return;
                        }
                        if (!((Response) o).getSuccess()) {
                            closure.setThrowable(new IllegalStateException(((Response) o).getErrMsg()));
                            closure.run(new Status(RaftError.UNKNOWN, ((Response) o).getErrMsg()));
                            return;
                        }
                        closure.setResponse((Response) o);
                        closure.run(Status.OK());
                    }
                    
                    @Override
                    public Executor executor() {
                        return RaftExecutor.getRaftCliServiceExecutor();
                    }
                }, timeoutMillis);
            } catch (Exception e) {
                closure.setThrowable(e);
                closure.run(new Status(RaftError.UNKNOWN, e.toString()));
            }
        
        }
}
