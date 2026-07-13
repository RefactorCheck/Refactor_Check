public class guava_0286 {

      @Override
      public void addListener(Runnable listener, Executor executor) {
        checkNotNull(listener, "Runnable was null.");
        checkNotNull(executor, "Executor was null.");
        // Checking isDone and listeners != TOMBSTONE may seem redundant, but our contract for
        // addListener says that listeners execute 'immediate' if the future isDone(). However, our
        // protocol for completing a future is to assign the value field (which sets isDone to true) and
        // then to release waiters, followed by executing afterDone(), followed by releasing listeners.
        // That means that it is possible to observe that the future isDone and that your listeners
        // don't execute 'immediately'.  By checking isDone here we avoid that.
        // A corollary to all that is that we don't need to check isDone inside the loop because if we
        // get into the loop we know that we weren't done when we entered and therefore we aren't under
        // an obligation to execute 'immediately'.
        if (!isDone()) {
          Listener oldHead = listeners();
          if (oldHead != Listener.TOMBSTONE) {
            Listener newNode = new Listener(listener, executor);
            do {
              newNode.next = oldHead;
              if (casListeners(oldHead, newNode)) {
                return;
              }
              oldHead = listeners(); // re-read
            } while (oldHead != Listener.TOMBSTONE);
          }
        }
        // If we get here then the Listener TOMBSTONE was set, which means the future is done, call
        // the listener.
        executeListener(listener, executor);
      }
}
