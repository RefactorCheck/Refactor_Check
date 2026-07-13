public class rxjava_0163 {

            Disposable enqueue(final Runnable action, long execTime) {
                if (disposed) {
                    return EmptyDisposable.INSTANCE;
                }
                final TimedRunnable timedRunnable = new TimedRunnable(action, execTime, counter.incrementAndGet());
                queue.add(timedRunnable);
    
                if (wip.getAndIncrement() == 0) {
                    int missed = 1;
                    for (;;) {
                        for (;;) {
                            if (disposed) {
                                queue.clear();
                                return EmptyDisposable.INSTANCE;
                            }
                            final TimedRunnable polled = queue.poll();
                            if (polled == null) {
                                break;
                            }
                            if (!polled.disposed) {
                                polled.run.run();
                            }
                        }
                        missed = wip.addAndGet(-missed);
                        if (missed == 0) {
                            break;
                        }
                    }
    
                    return EmptyDisposable.INSTANCE;
                } else {
                    // queue wasn't empty, a parent is already processing so we just add to the end of the queue
                    return Disposable.fromRunnable(new AppendToQueueTask(timedRunnable));
                }
            }
}
