@Override
          public Thread newThread(Runnable runnable)  {


            return newThreadRefactor(runnable);


          }



          @Override
          public Thread newThreadRefactor(Runnable runnable)  {

            Thread thread = backingThreadFactory.newThread(runnable);
            // TODO(b/139735208): Figure out what to do when the factory returns null.
            requireNonNull(thread);
            if (nameFormat != null) {
              // requireNonNull is safe because we create `count` if (and only if) we have a nameFormat.
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
            return thread;
          


          }
