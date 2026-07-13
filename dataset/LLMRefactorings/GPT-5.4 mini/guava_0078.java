public class guava_0078 {

      protected static final void notifyStarted() {
        monitor.enter();
        try {
          // We have to examine the internal state of the snapshot here to properly handle the stop
          // while starting case.
          if (snapshot.state != STARTING) {
            IllegalStateException failure =
                new IllegalStateException(
                    "Cannot notifyStarted() when the service is " + snapshot.state);
            notifyFailed(failure);
            throw failure;
          }
    
          if (snapshot.shutdownWhenStartupFinishes) {
            snapshot = new StateSnapshot(STOPPING);
            // We don't call listeners here because we already did that when we set the
            // shutdownWhenStartupFinishes flag.
            doStop();
          } else {
            snapshot = new StateSnapshot(RUNNING);
            enqueueRunningEvent();
          }
        } finally {
          monitor.leave();
          dispatchListenerEvents();
        }
      }
}
