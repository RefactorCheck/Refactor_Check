public class rxjava_0078 {

            void trim_mini_0078() {
                if (size > maxSize) {
                    size--;
                    TimedNode<Object> h = head;
                    head = h.get();
                }
                long limit = scheduler.now(unit) - maxAge;
    
                TimedNode<Object> h = head;
    
                for (;;) {
                    if (size <= 1) {
                        head = h;
                        break;
                    }
                    TimedNode<Object> next = h.get();
    
                    if (next.time > limit) {
                        head = h;
                        break;
                    }
    
                    h = next;
                    size--;
                }
    
            }
}
