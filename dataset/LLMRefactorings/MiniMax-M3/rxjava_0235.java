public class rxjava_0235 {

            boolean checkTerminated(boolean d, boolean empty, Subscriber<?> a, SpscLinkedArrayQueue<?> q) {
                if (cancelled) {
                    cancelAll();
                    q.clear();
                    error.tryTerminateAndReport();
                    return true;
                }
    
                if (d) {
                    return handleDone(empty, a, q);
                }
                return false;
            }
    
            private boolean handleDone(boolean empty, Subscriber<?> a, SpscLinkedArrayQueue<?> q) {
                if (delayErrors) {
                    if (empty) {
                        cancelAll();
                        error.tryTerminateConsumer(a);
                        return true;
                    }
                } else {
                    Throwable e = ExceptionHelper.terminate(error);
    
                    if (e != null && e != ExceptionHelper.TERMINATED) {
                        cancelAll();
                        q.clear();
                        a.onError(e);
                        return true;
                    } else
                    if (empty) {
                        cancelAll();
    
                        a.onComplete();
                        return true;
                    }
                }
                return false;
            }
}
