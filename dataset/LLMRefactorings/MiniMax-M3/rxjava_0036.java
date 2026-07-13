public class rxjava_0036 {

            @Nullable
            @Override
            public T poll() throws Throwable {
                for (;;) {
                    T t = this.qs.poll();
                    if (t == null) {
                        return null;
                    }

                    if (filter.test(t)) {
                        return t;
                    }

                    if (sourceMode == ASYNC) {
                        this.qs.request(1);
                    }
                }
            }
}
