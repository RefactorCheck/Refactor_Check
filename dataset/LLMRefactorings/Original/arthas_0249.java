public class arthas_0249 {

        @Override
        public Session removeSession(String sessionId) {
            Session session = sessions.get(sessionId);
            if (session == null) {
                return null;
            }
    
            //interrupt foreground job
            Job job = session.getForegroundJob();
            if (job != null) {
                job.interrupt();
            }
    
            SharingResultDistributor resultDistributor = session.getResultDistributor();
            if (resultDistributor != null) {
                resultDistributor.close();
            }
    
            return sessions.remove(sessionId);
        }
}
