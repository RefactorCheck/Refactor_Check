public class rxjava_0228 {

            void emitFirstRefactored() {
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
    
                    Lock readLock = s.readLock;
                    readLock.lock();
                    index = s.index;
                    o = s.value.get();
                    readLock.unlock();
    
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
}
