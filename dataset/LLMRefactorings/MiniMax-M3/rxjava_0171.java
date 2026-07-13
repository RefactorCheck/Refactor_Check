public class rxjava_0171 {

    void trim() {
        if (size > maxSize) {
            size--;
            TimedNode<T> h = head;
            head = h.get();
        }
        trimByAge();
    }

    void trimByAge() {
        long limit = scheduler.now(unit) - maxAge;

        TimedNode<T> h = head;

        for (;;) {
            if (size <= 1) {
                head = h;
                break;
            }
            TimedNode<T> next = h.get();

            if (next.time > limit) {
                head = h;
                break;
            }

            h = next;
            size--;
        }
    }
}
