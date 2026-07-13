public class rxjava_0124 {

            @Override
            void slowPath(long requested) {
                long emitted = 0;
                int endIndex = end;
                int currentIndex = index;
                ConditionalSubscriber<? super Integer> subscriber = downstream;
    
                for (;;) {
    
                    while (emitted != requested && currentIndex != endIndex) {
                        if (cancelled) {
                            return;
                        }
    
                        if (subscriber.tryOnNext(currentIndex)) {
                            emitted++;
                        }
    
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
                        if (requested == 0) {
                            return;
                        }
                        emitted = 0;
                    }
                }
            }
}
