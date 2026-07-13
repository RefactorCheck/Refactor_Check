public class arthas_0101 {

        @Override
        public Map<String, Object> interruptJob(String sessionId) {
            try {
                Session session = getCurrentSession(sessionId, false);
                Job job = session.getForegroundJob();
                if (job == null) {
                    return createErrorResult(null, "no foreground job is running");
                }
                job.interrupt();
                return buildInterruptResult(sessionId, job);

            } catch (SessionNotFoundException e) {
                return createErrorResult(null, e.getMessage());
            }
        }

        private Map<String, Object> buildInterruptResult(String sessionId, Job job) {
            Map<String, Object> result = new TreeMap<>();
            result.put("success", true);
            result.put("sessionId", sessionId);
            result.put("jobId", job.id());
            result.put("jobStatus", job.status().toString());
            return result;
        }
}
