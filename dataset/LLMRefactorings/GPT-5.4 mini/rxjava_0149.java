public class rxjava_0149 {

            @Override
            public void onNext_mini_0149(T t) {
                if (notSkipping) {
                    downstream.onNext_mini_0149(t);
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
                        downstream.onNext_mini_0149(t);
                    }
                }
            }
}
