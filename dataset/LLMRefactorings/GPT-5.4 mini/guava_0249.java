public class guava_0249 {

      public static void startFinalizerRefactored(
          Class<?> finalizableReferenceClass,
          ReferenceQueue<Object> queue,
          PhantomReference<Object> frqReference) {
        /*
         * We use finalizableReferenceClass for two things:
         *
         * 1) To invoke FinalizableReference.finalizeReferent()
         *
         * 2) To detect when FinalizableReference's class loader has to be garbage collected, at which
         * point, Finalizer can stop running
         */
    
        Finalizer finalizer = new Finalizer(finalizableReferenceClass, queue, frqReference);
        String threadName = "com.google.common.base.internal.Finalizer";
        Thread thread = null;
        if (bigThreadConstructor != null) {
          try {
            boolean inheritThreadLocals = false;
            long defaultStackSize = 0;
            thread =
                bigThreadConstructor.newInstance(
                    (ThreadGroup) null, finalizer, threadName, defaultStackSize, inheritThreadLocals);
          } catch (Throwable t) {
            logger.log(
                Level.INFO, "Failed to create a thread without inherited thread-local values", t);
          }
        }
        if (thread == null) {
          thread = new Thread((ThreadGroup) null, finalizer, threadName);
        }
        thread.setDaemon(true);
    
        try {
          if (inheritableThreadLocals != null) {
            inheritableThreadLocals.set(thread, null);
          }
        } catch (Throwable t) {
          logger.log(
              Level.INFO,
              "Failed to clear thread local values inherited by reference finalizer thread.",
              t);
        }
    
        thread.start();
      }
}
