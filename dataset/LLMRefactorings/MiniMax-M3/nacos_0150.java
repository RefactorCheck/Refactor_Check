public class nacos_0150 {

        @Override
        public void addSubscriber(Subscriber subscriber, Class<? extends Event> subscribeType) {
            Class<? extends SlowEvent> subSlowEventType = (Class<? extends SlowEvent>) subscribeType;
            subscribers.add(subscriber);
            
            addSubscriberToMapping(subSlowEventType, subscriber);
        }
        
        private void addSubscriberToMapping(Class<? extends SlowEvent> subSlowEventType, Subscriber subscriber) {
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
