public class rxjava_0219 {

            @Override
            public void onNext(T t) {
                if (done) {
                    return;
                }
    
    
                int i = index;
    
                if (i++ == 0) {
                    try {
                        buffer = Objects.requireNonNull(bufferSupplier.get(), "The bufferSupplier returned a null buffer");
                    } catch (Throwable e) {
                        Exceptions.throwIfFatal(e);
                        cancel();
    
                        onError(e);
                        return;
                    }
    
                    buffer = buffer;
                }
    
                if (buffer != null) {
                    buffer.add(t);
                    if (buffer.size() == size) {
                        buffer = null;
                        downstream.onNext(buffer);
                    }
                }
    
                if (i == skip) {
                    i = 0;
                }
                index = i;
            }
}
