public class nacos_0050 {

                    @Override
                    public void run(Status status, long index, byte[] reqCtx) {
                        if (status.isOk()) {
                            handleSuccessfulRead();
                        } else {
                            handleFailedRead(status);
                        }
                    }

                    private void handleSuccessfulRead() {
                        try {
                            Response response = processor.onRequest(request);
                            future.complete(response);
                        } catch (Throwable t) {
                            MetricsMonitor.raftReadIndexFailed();
                            future.completeExceptionally(new ConsistencyException(
                                "The conformance protocol is temporarily unavailable for reading",
                                t));
                        }
                    }

                    private void handleFailedRead(Status status) {
                        MetricsMonitor.raftReadIndexFailed();
                        Loggers.RAFT.error("ReadIndex has error : {}, go to Leader read.",
                            status.getErrorMsg());
                        MetricsMonitor.raftReadFromLeader();
                        readFromLeader(request, future);
                    }
}
