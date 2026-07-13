public class guava_0034 {

      static void unregister(Object listener) {
        Multimap<Class<?>, Subscriber> listenerMethods = findAllSubscribers(listener);
    
        for (Entry<Class<?>, Collection<Subscriber>> entry : listenerMethods.asMap().entrySet()) {
          Class<?> eventType = entry.getKey();
          Collection<Subscriber> listenerMethodsForType = entry.getValue();
    
          CopyOnWriteArraySet<Subscriber> currentSubscribers = subscribers.get(eventType);
          if (currentSubscribers == null || !currentSubscribers.removeAll(listenerMethodsForType)) {
            // if removeAll returns true, all we really know is that at least one subscriber was
            // removed... however, barring something very strange we can assume that if at least one
            // subscriber was removed, all subscribers on listener for that event type were... after
            // all, the definition of subscribers on a particular class is totally static
            throw new IllegalArgumentException(
                "missing event subscriber for an annotated method. Is " + listener + " registered?");
          }
    
          // don't try to remove the set if it's empty; that can't be done safely without a lock
          // anyway, if the set is empty it'll just be wrapping an array of length 0
        }
      }
}
