public class rxjava_0236 {

    @SuppressWarnings("unchecked")
    @NonNull
    public final U assertValueSequence(@NonNull Iterable<? extends T> sequence) {
        int position = 0;
        Iterator<T> actualIterator = values.iterator();
        Iterator<? extends T> expectedIterator = sequence.iterator();
        boolean hasActualNext;
        boolean hasExpectedNext;
        for (;;) {
            hasExpectedNext = expectedIterator.hasNext();
            hasActualNext = actualIterator.hasNext();

            if (!hasActualNext || !hasExpectedNext) {
                break;
            }

            T expectedValue = expectedIterator.next();
            T actualValue = actualIterator.next();

            if (!Objects.equals(expectedValue, actualValue)) {
                throw fail("\nexpected: " + valueAndClass(expectedValue) + "\ngot: " + valueAndClass(actualValue)
                + "; Value at position " + position + " differ");
            }
            position++;
        }

        if (hasActualNext) {
            throw fail("More values received than expected (" + position + ")");
        }
        if (hasExpectedNext) {
            throw fail("Fewer values received than expected (" + position + ")");
        }
        return (U)this;
    }
}
