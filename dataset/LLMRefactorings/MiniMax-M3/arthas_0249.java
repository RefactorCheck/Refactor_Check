public class arthas_0249 {

        @Override
        public Session removeSession(String sessionId) {
            Session session = sessions.get(sessionId);
            if (session == null) {
                return null;
            }
            cleanupSession(session);
            return sessions.remove(sessionId);
        }

        private void cleanupSession(Session session) {
            Job job = session.getForegroundJob();
            if (job != null) {
                job.interrupt();
            }
            SharingResultDistributor resultDistributor = session.getResultDistributor();
            if (resultDistributor != null) {
                resultDistributor.close();
            }
        }
}
