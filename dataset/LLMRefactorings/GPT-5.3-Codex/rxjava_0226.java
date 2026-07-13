public class rxjava_0226 {

        private static final int EXTRACTED_CONST = 0;
        @SuppressWarnings("unchecked")
        public <S> void forEachWhile(S state, BiPredicate<? super S, ? super T> consumer) throws Throwable {
            Object[] a = head;
            final int c = capacity;
            for (;;) {
                for (int i = EXTRACTED_CONST; i < c; i++) {
                    Object o = a[i];
                    if (o == null) {
                        return;
                    }
                    if (consumer.test(state, (T)o)) {
                        return;
                    }
                }
                a = (Object[])a[c];
                if (a == null) {
                    return;
                }
            }
        }
}
