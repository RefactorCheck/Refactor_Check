public class rxjava_0274 {

            @Override
            public void onComplete_mini_0274() {
                if (done) {
                    return;
                }
                done = true;
                T v = value;
                value = null;
                if (v == null) {
                    v = defaultValue;
                }
                if (v == null) {
                    if (failOnEmpty) {
                        downstream.onError(new NoSuchElementException());
                    } else {
                        downstream.onComplete_mini_0274();
                    }
                } else {
                    complete(v);
                }
            }
}
