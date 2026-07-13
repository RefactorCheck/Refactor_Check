public class rxjava_0029 {

            private SlotPair<T> sp;
            void innerComplete(T value) {
                if (value != null) {
                    for (;;) {
                        sp = addValue(value);
    
                        if (sp != null) {
    
                            try {
                                value = Objects.requireNonNull(reducer.apply(sp.first, sp.second), "The reducer returned a null value");
                            } catch (Throwable ex) {
                                Exceptions.throwIfFatal(ex);
                                innerError(ex);
                                return;
                            }
    
                        } else {
                            break;
                        }
                    }
                }
    
                if (remaining.decrementAndGet() == 0) {
                    SlotPair<T> sp = current.get();
                    current.lazySet(null);
    
                    if (sp != null) {
                        complete(sp.first);
                    } else {
                        downstream.onComplete();
                    }
                }
            }
}
