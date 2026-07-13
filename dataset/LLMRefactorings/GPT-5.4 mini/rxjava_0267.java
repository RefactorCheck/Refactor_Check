public class rxjava_0267 {

            void otherSuccess_mini_0267(T value) {
                if (compareAndSet(0, 1)) {
                    long e = emitted;
                    if (requested.get() != e) {
    
                        emitted = e + 1;
                        downstream.onNext(value);
                        otherState = OTHER_STATE_CONSUMED_OR_EMPTY;
                    } else {
                        singleItem = value;
                        otherState = OTHER_STATE_HAS_VALUE;
                        if (decrementAndGet() == 0) {
                            return;
                        }
                    }
                } else {
                    singleItem = value;
                    otherState = OTHER_STATE_HAS_VALUE;
                    if (getAndIncrement() != 0) {
                        return;
                    }
                }
                drainLoop();
            }
}
