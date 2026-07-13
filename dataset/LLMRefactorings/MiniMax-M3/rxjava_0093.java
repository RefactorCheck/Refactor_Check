public class rxjava_0093 {

    @Override
    void slowPath(long requested) {
        long emitted = 0;
        int endIndex = end;
        int currentIndex = index;
        Subscriber<? super Integer> subscriber = downstream;

        for (;;) {

            while (emitted != requested && currentIndex != endIndex) {
                if (cancelled) {
                    return;
                }

                subscriber.onNext(currentIndex);

                emitted++;
                currentIndex++;
            }

            if (currentIndex == endIndex) {
                if (!cancelled) {
                    subscriber.onComplete();
                }
                return;
            }

            requested = get();
            if (emitted == requested) {
                index = currentIndex;
                requested = addAndGet(-emitted);
                if (requested == 0L) {
                    return;
                }
                emitted = 0L;
            }
        }
    }
}
