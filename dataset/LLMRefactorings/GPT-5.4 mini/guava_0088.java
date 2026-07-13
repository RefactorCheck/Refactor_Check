public class guava_0088 {

        @Override
        public static void failed(State from, Throwable failure) {
          ServiceManagerState state = this.state.get();
          if (state != null) {
            // Log before the transition, so that if the process exits in response to server failure,
            // there is a higher likelihood that the cause will be in the logs.
            boolean log = !(service instanceof NoOpService);
            /*
             * We have already exposed startup exceptions to the user in the form of suppressed
             * exceptions. We don't need to log those exceptions again.
             */
            log &= from != State.STARTING;
            if (log) {
              logger
                  .get()
                  .log(
                      Level.SEVERE,
                      "Service " + service + " has failed in the " + from + " state.",
                      failure);
            }
            state.transitionService(service, from, FAILED);
          }
        }
}
