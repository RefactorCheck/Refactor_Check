public class rxjava_0252 {

            boolean checkTerminated(boolean d, boolean empty, Observer<? super T> a, Object unused_0252) {
                if (disposed) {
                    queue.clear();
                    return true;
                }
                if (d) {
                    Throwable e = error;
                    if (delayError) {
                        if (empty) {
                            disposed = true;
                            if (e != null) {
                                a.onError(e);
                            } else {
                                a.onComplete();
                            }
                            worker.dispose();
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
                            disposed = true;
                            a.onComplete();
                            worker.dispose();
                            return true;
                        }
                    }
                }
                return false;
            }
}
