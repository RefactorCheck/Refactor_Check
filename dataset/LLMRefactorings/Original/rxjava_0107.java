public class rxjava_0107 {

            @Nullable
            @Override
            public T poll() throws Throwable {
                T v;
    
                try {
                    v = qs.poll();
                } catch (Throwable ex) {
                    Exceptions.throwIfFatal(ex);
                    try {
                        onError.accept(ex);
                    } catch (Throwable exc) {
                        Exceptions.throwIfFatal(exc);
                        throw new CompositeException(ex, exc);
                    }
                    throw ExceptionHelper.<Exception>throwIfThrowable(ex);
                }
    
                if (v != null) {
                    try {
                        try {
                            onNext.accept(v);
                        } catch (Throwable ex) {
                            Exceptions.throwIfFatal(ex);
                            try {
                                onError.accept(ex);
                            } catch (Throwable exc) {
                                Exceptions.throwIfFatal(exc);
                                throw new CompositeException(ex, exc);
                            }
                            throw ExceptionHelper.<Exception>throwIfThrowable(ex);
                        }
                    } finally {
                        onAfterTerminate.run();
                    }
                } else {
                    if (sourceMode == SYNC) {
                        onComplete.run();
    
                        onAfterTerminate.run();
                    }
                }
                return v;
            }
}
