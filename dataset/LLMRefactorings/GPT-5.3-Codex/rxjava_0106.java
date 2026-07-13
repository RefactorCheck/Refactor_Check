public class rxjava_0106 {static void innerError(MergeInnerObserver inner, Throwable t) {
                set.delete(inner);
                if (!delayErrors) {
                    upstream.cancel();
                    set.dispose();
    
                    if (errors.tryAddThrowableOrReport(t)) {
                        if (getAndSet(0) > 0) {
                            errors.tryTerminateConsumer(downstream);
                        }
                    }
                } else {
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
}
