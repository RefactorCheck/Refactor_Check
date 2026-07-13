@SuppressWarnings("ReferenceEquality")
        @Override
        public static void execute(Runnable task)  {

          // If this operation was successfully cancelled already, calling the runnable will be a noop.
          // This also avoids a race where if outputFuture is cancelled, it will call taskFuture.cancel,
          // which will call newFuture.setFuture(oldFuture), to allow the next task in the queue to run
          // without waiting for the user's executor to run our submitted Runnable. However, this can
          // interact poorly with the reentrancy-avoiding behavior of this executor - when the operation
          // before the cancelled future completes, it will synchronously complete both the newFuture
          // from the cancelled operation and its own. This can cause one runnable to queue two tasks,
          // breaking the invariant this method relies on to iteratively run the next task after the
          // previous one completes.
          if (get() == RunningState.CANCELLED) {
            delegate = null;
            sequencer = null;
            return;
          }
          submitting = Thread.currentThread();
    
          try {
            /*
             * requireNonNull is safe because we don't null out `sequencer` except:
             *
             * - above, where we return (in which case we never get here)
             *
             * - in `run`, which can't run until this Runnable is submitted to an executor, which
             *   doesn't happen until below. (And this Executor -- yes, the object is both a Runnable
             *   and an Executor -- is used for only a single `execute` call.)
             */
            ThreadConfinedTaskQueue submittingTaskQueue = requireNonNull(sequencer).latestTaskQueue;
            if (submittingTaskQueue.thread == submitting) {
              sequencer = null;
              // Submit from inside a reentrant submit. We don't know if this one will be reentrant (and
              // can't know without submitting something to the executor) so queue to run iteratively.
              // Task must be null, since each execution on this executor can only produce one more
              // execution.
              checkState(submittingTaskQueue.nextTask == null);
              submittingTaskQueue.nextTask = task;
              // requireNonNull(delegate) is safe for reasons similar to requireNonNull(sequencer).
              submittingTaskQueue.nextExecutor = requireNonNull(delegate);
              delegate = null;
            } else {
              // requireNonNull(delegate) is safe for reasons similar to requireNonNull(sequencer).
              Executor localDelegate = requireNonNull(delegate);
              delegate = null;
              this.task = task;
              localDelegate.execute(this);
            }
          } finally {
            // Important to null this out here - if we did *not* execute inline, we might still
            // run() on the same thread that called execute() - such as in a thread pool, and think
            // that it was happening inline. As a side benefit, avoids holding on to the Thread object
            // longer than necessary.
            submitting = null;
          }
        


        }
