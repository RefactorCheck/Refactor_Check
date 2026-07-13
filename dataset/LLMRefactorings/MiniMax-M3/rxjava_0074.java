public class rxjava_0074 {

            @Override
            public void onNext(@NonNull T t) {
                if (done) {
                    return;
                }
                try {
                    try (Stream<? extends R> stream = Objects.requireNonNull(mapper.apply(t), "The mapper returned a null Stream")) {
                        Iterator<? extends R> it = stream.iterator();
                        while (it.hasNext()) {
                            if (isDisposed()) {
                                break;
                            }
                            R value = Objects.requireNonNull(it.next(), "The Stream's Iterator.next returned a null value");
                            if (isDisposed()) {
                                break;
                            }
                            downstream.onNext(value);
                            if (isDisposed()) {
                                break;
                            }
                        }
                    }
                } catch (Throwable ex) {
                    Exceptions.throwIfFatal(ex);
                    upstream.dispose();
                    onError(ex);
                }
            }

            private boolean isDisposed() {
                if (disposed) {
                    done = true;
                    return true;
                }
                return false;
            }
}
