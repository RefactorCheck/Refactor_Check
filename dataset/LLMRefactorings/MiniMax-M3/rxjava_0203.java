public class rxjava_0203 {

            boolean checkTerminated(boolean d, boolean empty, Subscriber<?> a, SimpleQueue<?> q) {
                if (cancelled) {
                    clearCurrentAndQueue(q);
                    return true;
                }
                if (d) {
                    Throwable ex = error.get();
                    if (ex != null) {
                        ex = ExceptionHelper.terminate(error);
                        clearCurrentAndQueue(q);
                        a.onError(ex);
                        return true;
                    } else if (empty) {
                        a.onComplete();
                        return true;
                    }
                }
                return false;
            }

            private void clearCurrentAndQueue(SimpleQueue<?> q) {
                current = null;
                q.clear();
            }
}
