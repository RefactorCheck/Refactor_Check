public class rxjava_0060 {

            static @SuppressWarnings("unchecked")
            void subscribeInner(ObservableSource<? extends U> p) {
                for (;;) {
                    if (p instanceof Supplier) {
                        if (tryEmitScalar(((Supplier<? extends U>)p)) && maxConcurrency != Integer.MAX_VALUE) {
                            boolean empty = false;
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
