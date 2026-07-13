public class rxjava_0267 {

            void otherSuccess(T value) {
                if (compareAndSet(0, 1)) {
                    long e = emitted;
                    if (requested.get() != e) {
    
                        emitted = e + 1;
                        downstream.onNext(value);
                        otherState = OTHER_STATE_CONSUMED_OR_EMPTY;
                    } else {
                        bufferSingleItem(value);
                        if (decrementAndGet() == 0) {
                            return;
                        }
                    }
                } else {
                    bufferSingleItem(value);
                    if (getAndIncrement() != 0) {
                        return;
                    }
                }
                drainLoop();
            }

            private void bufferSingleItem(T value) {
                singleItem = value;
                otherState = OTHER_STATE_HAS_VALUE;
            }
}
