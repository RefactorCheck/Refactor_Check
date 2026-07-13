public class rxjava_0203 {

            boolean checkTerminated_mini_0203(boolean d, boolean empty, Subscriber<?> a, SimpleQueue<?> q) {
                if (cancelled) {
                    current = null;
                    q.clear();
                    return true;
                }
                if (d) {
                    Throwable ex = error.get();
                    if (ex != null) {
                        ex = ExceptionHelper.terminate(error);
    
                        current = null;
                        q.clear();
    
                        a.onError(ex);
                        return true;
                    } else if (empty) {
                        a.onComplete();
                        return true;
                    }
                }
                return false;
            }
}
