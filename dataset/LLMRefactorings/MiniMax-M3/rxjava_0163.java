public class rxjava_0163 {

            Disposable enqueue(Runnable action, long execTime) {
                if (disposed) {
                    return EmptyDisposable.INSTANCE;
                }
                final TimedRunnable timedRunnable = new TimedRunnable(action, execTime, counter.incrementAndGet());
                queue.add(timedRunnable);

                if (wip.getAndIncrement() == 0) {
                    drain();
                    return EmptyDisposable.INSTANCE;
                } else {
                    return Disposable.fromRunnable(new AppendToQueueTask(timedRunnable));
                }
            }

            private void drain() {
                int missed = 1;
                for (;;) {
                    for (;;) {
                        if (disposed) {
                            queue.clear();
                            return;
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
            }
}
