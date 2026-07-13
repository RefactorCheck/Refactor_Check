public class rxjava_0149 {

            @Override
            public void onNext(T t) {
                if (notSkipping) {
                    downstream.onNext(t);
                } else if (tryEmit(t)) {
                    notSkipping = true;
                    downstream.onNext(t);
                }
            }

            private boolean tryEmit(T t) {
                try {
                    return !predicate.test(t);
                } catch (Throwable e) {
                    Exceptions.throwIfFatal(e);
                    upstream.dispose();
                    downstream.onError(e);
                    return false;
                }
            }
}
