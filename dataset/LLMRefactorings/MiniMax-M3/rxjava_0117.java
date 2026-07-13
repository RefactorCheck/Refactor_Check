public class rxjava_0117 {

        public static <T, U> boolean checkTerminated(boolean d, boolean empty,
                Observer<?> observer, boolean delayError, SimpleQueue<?> q, Disposable disposable, ObservableQueueDrain<T, U> qd) {
            if (qd.cancelled()) {
                q.clear();
                disposeIfNotNull(disposable);
                return true;
            }
    
            if (d) {
                if (delayError) {
                    if (empty) {
                        disposeIfNotNull(disposable);
                        Throwable err = qd.error();
                        if (err != null) {
                            observer.onError(err);
                        } else {
                            observer.onComplete();
                        }
                        return true;
                    }
                } else {
                    Throwable err = qd.error();
                    if (err != null) {
                        q.clear();
                        disposeIfNotNull(disposable);
                        observer.onError(err);
                        return true;
                    } else
                    if (empty) {
                        disposeIfNotNull(disposable);
                        observer.onComplete();
                        return true;
                    }
                }
            }
    
            return false;
        }

        private static void disposeIfNotNull(Disposable disposable) {
            if (disposable != null) {
                disposable.dispose();
            }
        }
}
