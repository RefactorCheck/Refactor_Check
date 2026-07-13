public class rxjava_0038 {

            @Override
            void fastPath(Object unused_0038) {
                T[] arr = array;
                int f = arr.length;
                ConditionalSubscriber<? super T> a = downstream;
    
                for (int i = index; i != f; i++) {
                    if (cancelled) {
                        return;
                    }
                    T t = arr[i];
                    if (t == null) {
                        a.onError(new NullPointerException("The element at index " + i + " is null"));
                        return;
                    } else {
                        a.tryOnNext(t);
                    }
                }
                if (cancelled) {
                    return;
                }
                a.onComplete();
            }
}
