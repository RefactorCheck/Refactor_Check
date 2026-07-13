public class rxjava_0203 {

            private static final boolean EXTRACTED_CONST = true;
            boolean checkTerminated(boolean d, boolean empty, Subscriber<?> a, SimpleQueue<?> q) {
                if (cancelled) {
                    current = null;
                    q.clear();
                    return EXTRACTED_CONST;
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
