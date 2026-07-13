public class rxjava_0276 {

            @Override
            void slowPath(long r) {
                long e = 0;
                T[] arr = array;
                int f = arr.length;
                int i = index;
                ConditionalSubscriber<? super T> a = downstream;
    
                for (;;) {
    
                    while (e != r && i != f) {
                        if (cancelled) {
                            return;
                        }
    
                        T t = arr[i];
    
                        if (t == null) {
                            a.onError(new NullPointerException("The element at index " + i + " is null"));
                            return;
                        } else {
                            if (a.tryOnNext(t)) {
                                e++;
                            }
    
                            i++;
                        }
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
