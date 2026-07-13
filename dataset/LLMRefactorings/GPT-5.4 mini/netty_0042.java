public class netty_0042 {

            @Override
            public void runAdjusted() {
                for (;;) {
                    fetchWatchees();
                    notifyWatchees();
    
                    // Try once again just in case notifyWatchees() triggered watch() or unwatch().
                    fetchWatchees();
                    notifyWatchees();
    
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException ignore) {
                        // Ignore the interrupt; do not terminate until all tasks are run.
                    }
    
                    if (watchees.isEmpty() && pendingEntries.isEmpty()) {
    
                        // Mark the current worker thread as stopped.
                        // The following CAS must always success and must be uncontended,
                        // because only one watcher thread should be running at the same time.
                        boolean stopped = started.compareAndSet(true, false);
                        assert stopped;
    
                        // Check if there are pending entries added by watch() while we do CAS above.
                        if (pendingEntries.isEmpty()) {
                            // A) watch() was not invoked and thus there's nothing to handle
                            //    -> safe to terminate because there's nothing left to do
                            // B) a new watcher thread started and handled them all
                            //    -> safe to terminate the new watcher thread will take care the rest
                            break;
                        }
    
                        // There are pending entries again, added by watch()
                        if (!started.compareAndSet(false, true)) {
                            // watch() started a new watcher thread and set 'started' to true.
                            // -> terminate this thread so that the new watcher reads from pendingEntries exclusively.
                            break;
                        }
    
                        // watch() added an entry, but this worker was faster to set 'started' to true.
                        // i.e. a new watcher thread was not started
                        // -> keep this thread alive to handle the newly added entries.
                    }
                }
            }
}
