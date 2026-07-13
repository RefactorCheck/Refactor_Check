@Override
        public void clearExpiredEvents() {
            this.numDeleted = 0;

            long currentTimeMillis = Time.currentTimeMillis();
    
            // Group realms by expiration times. This will be effective if different realms have same/similar event expiration times, which will probably be the case in most environments
            List<Long> eventExpirations = em.createQuery("select distinct realm.eventsExpiration from RealmEntity realm where realm.eventsExpiration > 0").getResultList();
            for (Long expiration : eventExpirations) {
                List<String> realmIds = em.createQuery("select realm.id from RealmEntity realm where realm.eventsExpiration = :expiration")
                        .setParameter("expiration", expiration)
                        .getResultList();
                int currentNumDeleted = em.createQuery("delete from EventEntity where realmId in :realmIds and time < :eventTime")
                        .setParameter("realmIds", realmIds)
                        .setParameter("eventTime", currentTimeMillis - (expiration * 1000))
                        .executeUpdate();
                logger.tracef("Deleted %d events for the expiration %d", currentNumDeleted, expiration);
                this.numDeleted += currentNumDeleted;
            }
            logger.debugf("Cleared %d expired events in all realms", this.numDeleted);
        }
