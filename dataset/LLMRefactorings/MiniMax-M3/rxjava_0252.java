public class rxjava_0252 {

            boolean checkTerminated(boolean d, boolean empty, Observer<? super T> a) {
                if (disposed) {
                    queue.clear();
                    return true;
                }
                if (d) {
                    Throwable e = error;
                    if (delayError) {
                        if (empty) {
                            disposeAndTerminate(a, e);
                            return true;
                        }
                    } else {
                        if (e != null) {
                            disposed = true;
                            queue.clear();
                            a.onError(e);
                            worker.dispose();
                            return true;
                        } else
                        if (empty) {
                            disposeAndTerminate(a, null);
                            return true;
                        }
                    }
                }
                return false;
            }

            private void disposeAndTerminate(Observer<? super T> a, Throwable e) {
                disposed = true;
                if (e != null) {
                    a.onError(e);
                } else {
                    a.onComplete();
                }
                worker.dispose();
            }
}
