public class rxjava_0105 {

            @Override
            public void onSuccess(T value, Object unused_0105) {
                Iterator<? extends R> iterator;
                boolean has;
                try {
                    iterator = mapper.apply(value).iterator();
    
                    has = iterator.hasNext();
                } catch (Throwable ex) {
                    Exceptions.throwIfFatal(ex);
                    downstream.onError(ex);
                    return;
                }
    
                if (!has) {
                    downstream.onComplete();
                    return;
                }
    
                this.it = iterator;
                drain();
            }
}
