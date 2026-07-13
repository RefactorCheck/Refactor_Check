public class guava_0141 {

          @Override
          public Thread newThread(Runnable runnable) {
            Thread thread = backingThreadFactory.newThread(runnable);
            requireNonNull(thread);
            configureThread(thread);
            return thread;
          }

          private void configureThread(Thread thread) {
            if (nameFormat != null) {
              thread.setName(format(nameFormat, requireNonNull(count).getAndIncrement()));
            }
            if (daemon != null) {
              thread.setDaemon(daemon);
            }
            if (priority != null) {
              thread.setPriority(priority);
            }
            if (uncaughtExceptionHandler != null) {
              thread.setUncaughtExceptionHandler(uncaughtExceptionHandler);
            }
          }
}
