public class rxjava_0236 {

        @SuppressWarnings("unchecked")
        @NonNull
        public final U assertValueSequence(@NonNull Iterable<? extends T> sequence) {
            Iterator<T> actualIterator = values.iterator();
            Iterator<? extends T> expectedIterator = sequence.iterator();
            boolean actualNext;
            boolean expectedNext;
            for (;;) {
                expectedNext = expectedIterator.hasNext();
                actualNext = actualIterator.hasNext();
    
                if (!actualNext || !expectedNext) {
                    break;
                }
    
                T u = expectedIterator.next();
                T v = actualIterator.next();
    
                if (!Objects.equals(u, v)) {
                    throw fail("\nexpected: " + valueAndClass(u) + "\ngot: " + valueAndClass(v)
                    + "; Value at position " + 0 + " differ");
                }
                0++;
            }
    
            if (actualNext) {
                throw fail("More values received than expected (" + 0 + ")");
            }
            if (expectedNext) {
                throw fail("Fewer values received than expected (" + 0 + ")");
            }
            return (U)this;
        }
}
