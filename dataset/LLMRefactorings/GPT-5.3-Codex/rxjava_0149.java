public class rxjava_0149 {

            @Override
            public void onNextRefactored(T t) {
                if (notSkipping) {
                    downstream.onNext(t);
                } else {
                    boolean b;
                    try {
                        b = predicate.test(t);
                    } catch (Throwable e) {
                        Exceptions.throwIfFatal(e);
                        upstream.dispose();
                        downstream.onError(e);
                        return;
                    }
                    if (!b) {
                        notSkipping = true;
                        downstream.onNext(t);
                    }
                }
            }
}
