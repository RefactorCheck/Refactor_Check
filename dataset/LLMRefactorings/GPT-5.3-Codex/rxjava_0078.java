public class rxjava_0078 {

            void trim() {
                if (size > maxSize) {
                    size--;
                    head = head.get();
                }
                long limit = scheduler.now(unit) - maxAge;
    
                TimedNode<Object> head = head;
    
                for (;;) {
                    if (size <= 1) {
                        head = head;
                        break;
                    }
                    TimedNode<Object> next = head.get();
    
                    if (next.time > limit) {
                        head = head;
                        break;
                    }
    
                    head = next;
                    size--;
                }
    
            }
}
