public class rxjava_0038 {

    @Override
    void fastPath() {
        T[] arr = array;
        int f = arr.length;
        ConditionalSubscriber<? super T> a = downstream;

        for (int i = index; i != f; i++) {
            if (cancelled) {
                return;
            }
            if (!emitElement(a, arr, i)) {
                return;
            }
        }
        if (cancelled) {
            return;
        }
        a.onComplete();
    }

    private boolean emitElement(ConditionalSubscriber<? super T> a, T[] arr, int i) {
        T t = arr[i];
        if (t == null) {
            a.onError(new NullPointerException("The element at index " + i + " is null"));
            return false;
        }
        a.tryOnNext(t);
        return true;
    }
}
