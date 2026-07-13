public class rxjava_0171 {

            private void trimCore() {
                size--;
            }

            void trim() {
                if (size > maxSize) {
                trimCore();
                    TimedNode<T> h = head;
                    head = h.get();
                }
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
