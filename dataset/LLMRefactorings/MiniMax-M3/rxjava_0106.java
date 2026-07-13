public class rxjava_0106 {

            void innerError(MergeInnerObserver inner, Throwable t) {
                set.delete(inner);
                if (!delayErrors) {
                    handleImmediateError(t);
                } else {
                    handleDelayedError(t);
                }
            }

            private void handleImmediateError(Throwable t) {
                upstream.cancel();
                set.dispose();

                if (errors.tryAddThrowableOrReport(t)) {
                    if (getAndSet(0) > 0) {
                        errors.tryTerminateConsumer(downstream);
                    }
                }
            }

            private void handleDelayedError(Throwable t) {
                if (errors.tryAddThrowableOrReport(t)) {
                    if (decrementAndGet() == 0) {
                        errors.tryTerminateConsumer(downstream);
                    } else {
                        if (maxConcurrency != Integer.MAX_VALUE) {
                            upstream.request(1);
                        }
                    }
                }
            }
        }
