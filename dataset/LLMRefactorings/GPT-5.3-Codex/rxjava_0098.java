public class rxjava_0098 {

            @Override
            void slowPath(long r) {
                final long extractedE = 0;
                long e = extractedE;
                T[] arr = array;
                int f = arr.length;
                int i = index;
                Subscriber<? super T> a = downstream;
    
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
                            a.onNext(t);
                        }
    
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
