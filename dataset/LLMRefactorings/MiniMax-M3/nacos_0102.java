public class nacos_0102 {

        private void invokeToLeader(final String group, final Message request, final int timeoutMillis,
            FailoverClosure closure) {
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
                        Response response = (Response) o;
                        if (!response.getSuccess()) {
                            closure.setThrowable(new IllegalStateException(response.getErrMsg()));
                            closure.run(new Status(RaftError.UNKNOWN, response.getErrMsg()));
                            return;
                        }
                        closure.setResponse(response);
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
