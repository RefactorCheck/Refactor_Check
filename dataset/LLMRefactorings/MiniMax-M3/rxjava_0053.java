public class rxjava_0053 {

            boolean checkTerminated(boolean d, boolean empty, Observer<? super R> a, boolean delayError, ZipObserver<?, ?> source) {
                if (cancelled) {
                    cancel();
                    return true;
                }
    
                if (d) {
                    if (delayError) {
                        if (empty) {
                            terminate(a, source.error);
                            return true;
                        }
                    } else {
                        Throwable e = source.error;
                        if (e != null) {
                            terminateWithError(a, e);
                            return true;
                        } else
                        if (empty) {
                            terminateWithComplete(a);
                            return true;
                        }
                    }
                }
    
                return false;
            }

            private void terminate(Observer<? super R> a, Throwable e) {
                cancelled = true;
                cancel();
                if (e != null) {
                    a.onError(e);
                } else {
                    a.onComplete();
                }
            }

            private void terminateWithError(Observer<? super R> a, Throwable e) {
                cancelled = true;
                cancel();
                a.onError(e);
            }

            private void terminateWithComplete(Observer<? super R> a) {
                cancelled = true;
                cancel();
                a.onComplete();
            }
}
