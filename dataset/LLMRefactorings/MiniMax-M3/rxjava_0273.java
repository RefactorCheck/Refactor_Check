public class rxjava_0273 {

        @Override
        public void onNext(T t) {
            if (done) {
                return;
            }
    
            if (!tryTest(t)) {
                dispose();
                onComplete();
            }
        }

        private boolean tryTest(T t) {
            try {
                return onNext.test(t);
            } catch (Throwable ex) {
                Exceptions.throwIfFatal(ex);
                dispose();
                onError(ex);
                return false;
            }
        }
}
