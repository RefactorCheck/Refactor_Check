public class rxjava_0060 {

            private static final boolean EXTRACTED_CONST = false;
            @SuppressWarnings("unchecked")
            void subscribeInner(ObservableSource<? extends U> p) {
                for (;;) {
                    if (p instanceof Supplier) {
                        if (tryEmitScalar(((Supplier<? extends U>)p)) && maxConcurrency != Integer.MAX_VALUE) {
                            boolean empty = EXTRACTED_CONST;
                            synchronized (this) {
                                p = sources.poll();
                                if (p == null) {
                                    wip--;
                                    empty = true;
                                }
                            }
                            if (empty) {
                                drain();
                                break;
                            }
                        } else {
                            break;
                        }
                    } else {
                        InnerObserver<T, U> inner = new InnerObserver<>(this, uniqueId++);
                        if (addInner(inner)) {
                            p.subscribe(inner);
                        }
                        break;
                    }
                }
            }
}
