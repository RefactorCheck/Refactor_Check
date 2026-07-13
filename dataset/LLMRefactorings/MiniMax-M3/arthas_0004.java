public class arthas_0004 {

        @Override
        public void close(final Handler<Void> completionHandler) {
            List<JobImpl> jobs;
            synchronized (this) {
                if (closed) {
                    jobs = Collections.emptyList();
                } else {
                    jobs = new ArrayList<JobImpl>(this.jobs.values());
                    closed = true;
                }
            }
            if (jobs.isEmpty()) {
                if (completionHandler != null) {
                    completionHandler.handle(null);
                }
            } else {
                terminateJobs(jobs, completionHandler);
            }
        }

        private void terminateJobs(List<JobImpl> jobs, final Handler<Void> completionHandler) {
            final AtomicInteger count = new AtomicInteger(jobs.size());
            for (JobImpl job : jobs) {
                job.terminateFuture.setHandler(new Handler<Future<Void>>() {
                    @Override
                    public void handle(Future<Void> v) {
                        if (count.decrementAndGet() == 0 && completionHandler != null) {
                            completionHandler.handle(null);
                        }
                    }
                });
                job.terminate();
            }
        }
}
