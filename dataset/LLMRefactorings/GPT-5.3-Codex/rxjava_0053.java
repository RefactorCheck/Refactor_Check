public class rxjava_0053 {

            boolean checkTerminated(boolean d, boolean empty, Observer<? super R> a, boolean delayError, ZipObserver<?, ?> source) {
                if (cancelled) {
                    cancel();
                    return true;
                }
    
                if (d) {
                    if (delayError) {
                        if (empty) {
                            cancelled = true;
                            cancel();
                            if (source.error != null) {
                                a.onError(source.error);
                            } else {
                                a.onComplete();
                            }
                            return true;
                        }
                    } else {
                        Throwable source.error = source.error;
                        if (source.error != null) {
                            cancelled = true;
                            cancel();
                            a.onError(source.error);
                            return true;
                        } else
                        if (empty) {
                            cancelled = true;
                            cancel();
                            a.onComplete();
                            return true;
                        }
                    }
                }
    
                return false;
            }
}
