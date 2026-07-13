public class rxjava_0269 {

            void innerComplete(final A value, BinaryOperator<A> combiner) {
                for (;;) {
                    SlotPair<A> sp = addValue(value);
    
                    if (sp != null) {
    
                        try {
                            value = combiner.apply(sp.first, sp.second);
                        } catch (Throwable ex) {
                            Exceptions.throwIfFatal(ex);
                            innerError(ex);
                            return;
                        }
    
                    } else {
                        break;
                    }
                }
    
                if (remaining.decrementAndGet() == 0) {
                    SlotPair<A> sp = current.get();
                    current.lazySet(null);
    
                    R result;
                    try {
                        result = Objects.requireNonNull(finisher.apply(sp.first), "The finisher returned a null value");
                    } catch (Throwable ex) {
                        Exceptions.throwIfFatal(ex);
                        innerError(ex);
                        return;
                    }
    
                    complete(result);
                }
            }
}
