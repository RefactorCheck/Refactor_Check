public class rxjava_0228<T> {

    void emitFirst() {
        if (cancelled) {
            return;
        }
        Object o;
        synchronized (this) {
            if (cancelled) {
                return;
            }
            if (next) {
                return;
            }

            BehaviorProcessor<T> s = state;
            o = readValueAndIndex(s);

            emitting = o != null;
            next = true;
        }

        if (o != null) {
            if (test(o)) {
                return;
            }

            emitLoop();
        }
    }

    private Object readValueAndIndex(BehaviorProcessor<T> s) {
        Lock readLock = s.readLock;
        readLock.lock();
        try {
            index = s.index;
            return s.value.get();
        } finally {
            readLock.unlock();
        }
    }
}
