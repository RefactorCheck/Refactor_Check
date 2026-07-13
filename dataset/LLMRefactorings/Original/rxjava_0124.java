public class rxjava_0124 {

            @Override
            void slowPath(long r) {
                long e = 0;
                int f = end;
                int i = index;
                ConditionalSubscriber<? super Integer> a = downstream;
    
                for (;;) {
    
                    while (e != r && i != f) {
                        if (cancelled) {
                            return;
                        }
    
                        if (a.tryOnNext(i)) {
                            e++;
                        }
    
                        i++;
                    }
    
                    if (i == f) {
                        if (!cancelled) {
                            a.onComplete();
                        }
                        return;
                    }
    
                    r = get();
                    if (e == r) {
                        index = i;
                        r = addAndGet(-e);
                        if (r == 0) {
                            return;
                        }
                        e = 0;
                    }
                }
            }
}
