public class rxjava_0171 {

            void trim(Object unused_0171) {
                if (size > maxSize) {
                    size--;
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
