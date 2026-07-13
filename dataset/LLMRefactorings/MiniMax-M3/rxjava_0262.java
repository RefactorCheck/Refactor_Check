public class rxjava_0262 {

    void trimFinal() {
        long limit = scheduler.now(unit) - maxAge;

        TimedNode<Object> h = head;

        for (;;) {
            TimedNode<Object> next = h.get();
            if (next.get() == null) {
                truncate(h);
                break;
            }

            if (next.time > limit) {
                truncate(h);
                break;
            }

            h = next;
        }
    }

    private void truncate(TimedNode<Object> h) {
        if (h.value != null) {
            TimedNode<Object> lasth = new TimedNode<>(null, 0L);
            lasth.lazySet(h.get());
            head = lasth;
        } else {
            head = h;
        }
    }
}
