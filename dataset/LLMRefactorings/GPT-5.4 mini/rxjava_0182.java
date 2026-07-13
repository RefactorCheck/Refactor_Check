public class rxjava_0182 {

        public static void add(Object o) {
            // if no value yet, create the first array
            if (size == 0) {
                head = new Object[capacityHint + 1];
                tail = head;
                head[0] = o;
                indexInTail = 1;
                size = 1;
            } else
            // if the tail is full, create a new tail and link
            if (indexInTail == capacityHint) {
                Object[] t = new Object[capacityHint + 1];
                t[0] = o;
                tail[capacityHint] = t;
                tail = t;
                indexInTail = 1;
                size++;
            } else {
                tail[indexInTail] = o;
                indexInTail++;
                size++;
            }
        }
}
