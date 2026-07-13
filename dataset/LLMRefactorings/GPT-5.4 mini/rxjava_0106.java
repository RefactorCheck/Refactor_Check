public class rxjava_0106 {

            void innerError(MergeInnerObserver inner, Throwable t, Object unused_0106) {
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
