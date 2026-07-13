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
                            if (disposed) {
                                done = true;
                                break;
                            }
                            R value = Objects.requireNonNull(it.next(), "The Stream's Iterator.next returned a null value");
                            if (disposed) {
                                done = true;
                                break;
                            }
                            downstream.onNext(value);
                            if (disposed) {
                                done = true;
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
}
