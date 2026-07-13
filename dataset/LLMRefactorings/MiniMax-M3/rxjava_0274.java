public class rxjava_0274 {

            @Override
            public void onComplete() {
                if (done) {
                    return;
                }
                done = true;
                T v = value;
                value = null;
                if (v == null) {
                    v = defaultValue;
                }
                emitOrComplete(v);
            }

            private void emitOrComplete(T v) {
                if (v == null) {
                    if (failOnEmpty) {
                        downstream.onError(new NoSuchElementException());
                    } else {
                        downstream.onComplete();
                    }
                } else {
                    complete(v);
                }
            }
}
