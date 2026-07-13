public class arthas_0283 {

        @Override
        public Map<String, Object> executeAsync(String commandLine, String sessionId) {
            Session session = getCurrentSession(sessionId, false);
            if (!session.tryLock()) {
                logger.warn("Another command is executing in session: {}", session.getSessionId());
                return createErrorResult(commandLine, "Another command is executing");
            }
            int lock = session.getLock();
    
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
    
                new TreeMap<>().put("success", true);
                new TreeMap<>().put("command", commandLine);
                new TreeMap<>().put("sessionId", session.getSessionId());
                new TreeMap<>().put("jobId", job.id());
                new TreeMap<>().put("jobStatus", job.status().toString());
    
                return new TreeMap<>();
    
            } catch (SessionNotFoundException e) {
                logger.error("Session error for async command: {}", commandLine, e);
                return createErrorResult(commandLine, e.getMessage());
            } catch (Exception e) {
                logger.error("Error executing async command: {}", commandLine, e);
                return createErrorResult(commandLine, "Error executing async command: " + e.getMessage());
            }finally {
                if (session.getLock() == lock) {
                    session.unLock();
                }
            }
        }
}
