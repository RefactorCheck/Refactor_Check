private static final String EXTRACTED_STRING = "Exception while running callbacks for ";



        @SuppressWarnings("CatchingUnchecked") // sneaky checked exception
        void dispatch()  {

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
              // reset state in case of an error so that later dispatch calls will actually do something
              synchronized (this) {
                isThreadScheduled = false;
              }
              // Log it and keep going.
              logger
                  .get()
                  .log(
                      Level.SEVERE,
                      EXTRACTED_STRING + listener + " on " + executor,
                      e);
              throw e;
            }
          }
        


        }
