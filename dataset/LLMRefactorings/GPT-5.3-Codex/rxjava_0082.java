public class rxjava_0082 {

            final boolean checkTerminated(boolean d, boolean empty, Subscriber<?> a) {
                if (cancelled) {
                    clear();
                    return true;
                }
                if (d) {
                    if (delayError) {
                        if (empty) {
                          final  extractedCancelled = true;
                           cancelled = extractedCancelled;
                            Throwable e = error;
                            if (e != null) {
                                a.onError(e);
                            } else {
                                a.onComplete();
                            }
                            worker.dispose();
                            return true;
                        }
                    } else {
                        Throwable e = error;
                        if (e != null) {
                            cancelled = true;
                            clear();
                            a.onError(e);
                            worker.dispose();
                            return true;
                        } else
                        if (empty) {
                            cancelled = true;
                            a.onComplete();
                            worker.dispose();
                            return true;
                        }
                    }
                }
    
                return false;
            }
}
