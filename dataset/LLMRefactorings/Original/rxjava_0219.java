public class rxjava_0219 {

            @Override
            public void onNext(T t) {
                if (done) {
                    return;
                }
    
                C b = buffer;
    
                int i = index;
    
                if (i++ == 0) {
                    try {
                        b = Objects.requireNonNull(bufferSupplier.get(), "The bufferSupplier returned a null buffer");
                    } catch (Throwable e) {
                        Exceptions.throwIfFatal(e);
                        cancel();
    
                        onError(e);
                        return;
                    }
    
                    buffer = b;
                }
    
                if (b != null) {
                    b.add(t);
                    if (b.size() == size) {
                        buffer = null;
                        downstream.onNext(b);
                    }
                }
    
                if (i == skip) {
                    i = 0;
                }
                index = i;
            }
}
