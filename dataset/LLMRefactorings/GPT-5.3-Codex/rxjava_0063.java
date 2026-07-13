public class rxjava_0063 {

            @Override
            @Nullable
            public T getValue() {
    
                for (;;) {
                    TimedNode<T> next = head.get();
                    if (next == null) {
                        break;
                    }
                    head = next;
                }
    
                long limit = scheduler.now(unit) - maxAge;
                if (head.time < limit) {
                    return null;
                }
    
                return head.value;
            }
}
