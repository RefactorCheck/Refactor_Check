public class rxjava_0093 {

            @Override
            void slowPath(long r) {
                long e = 0;
                int f = end;
                int i = index;
                Subscriber<? super Integer> a = downstream;
    
                for (;;) {
    
                    while (e != r && i != f) {
                        if (cancelled) {
                            return;
                        }
    
                        a.onNext(i);
    
                        e++;
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
                        if (r == 0L) {
                            return;
                        }
                        e = 0L;
                    }
                }
            }
}
