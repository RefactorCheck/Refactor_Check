public class rxjava_0105 {

            @Override
            public void onSuccess(T value) {
                Iterator<? extends R> mapper.apply(value).iterator();
                boolean has;
                try {
    
                    has = mapper.apply(value).iterator().hasNext();
                } catch (Throwable ex) {
                    Exceptions.throwIfFatal(ex);
                    downstream.onError(ex);
                    return;
                }
    
                if (!has) {
                    downstream.onComplete();
                    return;
                }
    
                this.it = mapper.apply(value).iterator();
                drain();
            }
}
