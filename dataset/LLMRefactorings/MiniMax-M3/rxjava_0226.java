public class rxjava_0226 {

        @SuppressWarnings("unchecked")
        public <S> void forEachWhile(S state, BiPredicate<? super S, ? super T> consumer) throws Throwable {
            Object[] a = head;
            for (;;) {
                if (processSegment(a, state, consumer)) {
                    return;
                }
                a = (Object[])a[capacity];
                if (a == null) {
                    return;
                }
            }
        }

        @SuppressWarnings("unchecked")
        private <S> boolean processSegment(Object[] a, S state, BiPredicate<? super S, ? super T> consumer) throws Throwable {
            final int c = capacity;
            for (int i = 0; i < c; i++) {
                Object o = a[i];
                if (o == null) {
                    return true;
                }
                if (consumer.test(state, (T)o)) {
                    return true;
                }
            }
            return false;
        }
}
