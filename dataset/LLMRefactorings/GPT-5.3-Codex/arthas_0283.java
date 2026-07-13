public class arthas_0283 {

        @Override
        public Map<String, Object> executeAsync(String commandLine, String sessionId) {
            Map<String, Object> result = new TreeMap<>();
            Session session = getCurrentSession(sessionId, false);
            if (!session.tryLock()) {
                logger.warn("Another command is executing in session: {}", session.getSessionId());
                return createErrorResult(commandLine, "Another command is executing");
            }
    
            try {
                Job foregroundJob = session.getForegroundJob();
                if (foregroundJob != null) {
                    logger.warn("Another job is running in session: {}, jobId: {}", session.getSessionId(), foregroundJob.id());
                    session.unLock();
                    return createErrorResult(commandLine, "Another job is running, jobId: " + foregroundJob.id());
                }
    
                Job job = this.createJob(commandLine, session, session.getResultDistributor());
    
                if (job == null) {
                    logger.error("Failed to create job for command: {}", commandLine);
                    session.unLock();
                    return createErrorResult(commandLine, "Failed to create job");
                }
    
                session.setForegroundJob(job);
                updateSessionInputStatus(session, InputStatus.ALLOW_INTERRUPT);
    
                job.run();
    
                result.put("success", true);
                result.put("command", commandLine);
                result.put("sessionId", session.getSessionId());
                result.put("jobId", job.id());
                result.put("jobStatus", job.status().toString());
    
                return result;
    
            } catch (SessionNotFoundException e) {
                logger.error("Session error for async command: {}", commandLine, e);
                return createErrorResult(commandLine, e.getMessage());
            } catch (Exception e) {
                logger.error("Error executing async command: {}", commandLine, e);
                return createErrorResult(commandLine, "Error executing async command: " + e.getMessage());
            }finally {
                if (session.getLock() == (session.getLock())) {
                    session.unLock();
                }
            }
        }
}
