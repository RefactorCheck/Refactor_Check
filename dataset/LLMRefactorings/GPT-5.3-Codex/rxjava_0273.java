public class rxjava_0273 {@Override public static void onNext(T t) {
            if (done) {
                return;
            }
    
            boolean b;
            try {
                b = onNext.test(t);
            } catch (Throwable ex) {
                Exceptions.throwIfFatal(ex);
                dispose();
                onError(ex);
                return;
            }
    
            if (!b) {
                dispose();
                onComplete();
            }
        }
}
