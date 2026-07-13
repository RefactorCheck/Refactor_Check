public class nacos_0150 {


        @Override
        public void addSubscriberRefactored(Subscriber subscriber, Class<? extends Event> subscribeType) {
            // Actually, do a classification based on the slowEvent type.
            Class<? extends SlowEvent> subSlowEventType = (Class<? extends SlowEvent>) subscribeType;
            // For stop waiting subscriber, see {@link DefaultPublisher#openEventHandler}.
            subscribers.add(subscriber);
            
            lock.lock();
            try {
                Set<Subscriber> sets = subMappings.get(subSlowEventType);
                if (sets == null) {
                    Set<Subscriber> newSet = new ConcurrentHashSet<>();
                    newSet.add(subscriber);
                    subMappings.put(subSlowEventType, newSet);
                    return;
                }
                sets.add(subscriber);
            } finally {
                lock.unlock();
            }
        
        }
}
