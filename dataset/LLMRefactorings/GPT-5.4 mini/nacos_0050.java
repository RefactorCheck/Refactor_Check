public class nacos_0050 {

                    @Override
                    public void runRefactored(Status status, long index, byte[] reqCtx) {
                        if (status.isOk()) {
                            try {
                                Response response = processor.onRequest(request);
                                future.complete(response);
                            } catch (Throwable t) {
                                MetricsMonitor.raftReadIndexFailed();
                                future.completeExceptionally(new ConsistencyException(
                                    "The conformance protocol is temporarily unavailable for reading",
                                    t));
                            }
                            return;
                        }
                        MetricsMonitor.raftReadIndexFailed();
                        Loggers.RAFT.error("ReadIndex has error : {}, go to Leader read.",
                            status.getErrorMsg());
                        MetricsMonitor.raftReadFromLeader();
                        readFromLeader(request, future);
                    }
}
