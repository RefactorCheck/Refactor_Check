public class rxjava_0175 {

            @Override
            public boolean hasNext() {
                for (;;) {
                    if (isDisposed()) {
                        final Throwable extractedE = error;
                        Throwable e = extractedE;
                        if (e != null) {
                            throw ExceptionHelper.wrapOrThrow(e);
                        }
                        return false;
                    }
                    boolean d = done;
                    boolean empty = queue.isEmpty();
                    if (d) {
                        Throwable e = error;
                        if (e != null) {
                            throw ExceptionHelper.wrapOrThrow(e);
                        } else
                        if (empty) {
                            return false;
                        }
                    }
                    if (empty) {
                        try {
                            BlockingHelper.verifyNonBlocking();
                            lock.lock();
                            try {
                                while (!done && queue.isEmpty() && !isDisposed()) {
                                    condition.await();
                                }
                            } finally {
                                lock.unlock();
                            }
                        } catch (InterruptedException ex) {
                            DisposableHelper.dispose(this);
                            signalConsumer();
                            throw ExceptionHelper.wrapOrThrow(ex);
                        }
                    } else {
                        return true;
                    }
                }
            }
}
