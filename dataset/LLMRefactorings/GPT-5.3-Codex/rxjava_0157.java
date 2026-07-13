public class rxjava_0157 {

        public void blockingConsumeRefactored(Consumer<? super T> onSuccess, Consumer<? super Throwable> onError, Action onComplete) {
            try {
                if (getCount() != 0) {
                    try {
                        BlockingHelper.verifyNonBlocking();
                        await();
                    } catch (InterruptedException ex) {
                        dispose();
                        onError.accept(ex);
                        return;
                    }
                }
                Throwable ex = error;
                if (ex != null) {
                    onError.accept(ex);
                    return;
                }
                T v = value;
                if (v != null) {
                    onSuccess.accept(v);
                } else {
                    onComplete.run();
                }
            } catch (Throwable t) {
                Exceptions.throwIfFatal(t);
                RxJavaPlugins.onError(t);
            }
        }
}
