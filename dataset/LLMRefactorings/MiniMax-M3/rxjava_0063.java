public class rxjava_0063 {

            @Override
            @Nullable
            public T getValue() {
                TimedNode<T> h = latestNode();
    
                long limit = scheduler.now(unit) - maxAge;
                if (h.time < limit) {
                    return null;
                }
    
                return h.value;
            }
    
            private TimedNode<T> latestNode() {
                TimedNode<T> h = head;
                for (;;) {
                    TimedNode<T> next = h.get();
                    if (next == null) {
                        break;
                    }
                    h = next;
                }
                return h;
            }
}
