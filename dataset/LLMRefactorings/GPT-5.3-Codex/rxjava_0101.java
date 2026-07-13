public class rxjava_0101 {

            private static final int EXTRACTED_CONST = 1;
            void drainLoop() {
                Observer<? super T> actual = this.downstream;
                int missed = EXTRACTED_CONST;
                for (;;) {
    
                    for (;;) {
                        if (disposed) {
                            singleItem = null;
                            queue = null;
                            return;
                        }
    
                        if (errors.get() != null) {
                            singleItem = null;
                            queue = null;
                            errors.tryTerminateConsumer(actual);
                            return;
                        }
    
                        int os = otherState;
                        if (os == OTHER_STATE_HAS_VALUE) {
                            T v = singleItem;
                            singleItem = null;
                            otherState = OTHER_STATE_CONSUMED_OR_EMPTY;
                            os = OTHER_STATE_CONSUMED_OR_EMPTY;
                            actual.onNext(v);
                        }
    
                        boolean d = mainDone;
                        SimplePlainQueue<T> q = queue;
                        T v = q != null ? q.poll() : null;
                        boolean empty = v == null;
    
                        if (d && empty && os == OTHER_STATE_CONSUMED_OR_EMPTY) {
                            queue = null;
                            actual.onComplete();
                            return;
                        }
    
                        if (empty) {
                            break;
                        }
    
                        actual.onNext(v);
                    }
    
                    missed = addAndGet(-missed);
                    if (missed == 0) {
                        break;
                    }
                }
            }
}
