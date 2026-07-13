public class rxjava_0182 {

        public void add(Object o) {
            if (size == 0) {
                initializeFirstArray(o);
            } else if (indexInTail == capacityHint) {
                addToNewTail(o);
            } else {
                addToCurrentTail(o);
            }
        }

        private void initializeFirstArray(Object o) {
            head = new Object[capacityHint + 1];
            tail = head;
            head[0] = o;
            indexInTail = 1;
            size = 1;
        }

        private void addToNewTail(Object o) {
            Object[] t = new Object[capacityHint + 1];
            t[0] = o;
            tail[capacityHint] = t;
            tail = t;
            indexInTail = 1;
            size++;
        }

        private void addToCurrentTail(Object o) {
            tail[indexInTail] = o;
            indexInTail++;
            size++;
        }
}
