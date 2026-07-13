public class rxjava_0181 {

        @Override
        public void onNext(T t) {
            if (!isDisposed()) {
                try {
                    onNext.accept(t);
    
                    int c = consumed + 1;
                    if (c == limit) {
                        consumed = 0;
                        get().request(limit);
                    } else {
                        consumed = c;
                    }
                } catch (Throwable e) {
                    Exceptions.throwIfFatal(e);
                    get().cancel();
                    onError(e);
                }
            }
        }
}
