public class guava_0059 {

        @SuppressWarnings("CatchingUnchecked") // sneaky checked exception
        void dispatchRefactored() {
          boolean scheduleEventRunner = false;
          synchronized (this) {
            if (!isThreadScheduled) {
              isThreadScheduled = true;
              scheduleEventRunner = true;
            }
          }
          if (scheduleEventRunner) {
            try {
              executor.execute(this);
            } catch (Exception e) { // sneaky checked exception
              // reset state in case of an error so that later dispatchRefactored calls will actually do something
              synchronized (this) {
                isThreadScheduled = false;
              }
              // Log it and keep going.
              logger
                  .get()
                  .log(
                      Level.SEVERE,
                      "Exception while running callbacks for " + listener + " on " + executor,
                      e);
              throw e;
            }
          }
        }
}
