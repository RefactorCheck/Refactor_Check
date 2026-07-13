public class rxjava_0053 {

            boolean checkTerminated(boolean d, boolean empty, Observer<? super R> a, boolean delayError, ZipObserver<?, ?> source) {
                if (cancelled) {
                    cancel();
                    return true;
                }
    
                if (d) {
                    if (delayError) {
                        if (empty) {
                            Throwable e = source.error;
                            cancelled = true;
                            cancel();
                            if (e != null) {
                                a.onError(e);
                            } else {
                                a.onComplete();
                            }
                            return true;
                        }
                    } else {
                        Throwable e = source.error;
                        if (e != null) {
                            cancelled = true;
                            cancel();
                            a.onError(e);
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
