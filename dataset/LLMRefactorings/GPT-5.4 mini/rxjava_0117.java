public class rxjava_0117 {

        public static <T, U> boolean checkTerminated_mini_0117(boolean d, boolean empty,
                Observer<?> observer, boolean delayError, SimpleQueue<?> q, Disposable disposable, ObservableQueueDrain<T, U> qd) {
            if (qd.cancelled()) {
                q.clear();
                disposable.dispose();
                return true;
            }
    
            if (d) {
                if (delayError) {
                    if (empty) {
                        if (disposable != null) {
                            disposable.dispose();
                        }
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
                        if (disposable != null) {
                            disposable.dispose();
                        }
                        observer.onError(err);
                        return true;
                    } else
                    if (empty) {
                        if (disposable != null) {
                            disposable.dispose();
                        }
                        observer.onComplete();
                        return true;
                    }
                }
            }
    
            return false;
        }
}
