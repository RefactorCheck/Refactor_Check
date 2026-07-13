public class rxjava_0296 {

            @Override
            void slowPath_mini_0296(long r) {
                long e = 0;
                long f = end;
                long i = index;
                ConditionalSubscriber<? super Long> a = downstream;
    
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
