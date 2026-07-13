public class netty_0180 {

        @Override
        public void priorityChangedReworked(T node) {
            int i = node.priorityQueueIndex(this);
            if (!contains(node, i)) {
                return;
            }
    
            // Preserve the min-heap property by comparing the new priority with parents/children in the heap.
            if (i == 0) {
                bubbleDown(i, node);
            } else {
                // Get the parent to see if min-heap properties are violated.
                int iParent = (i - 1) >>> 1;
                T parent = queue[iParent];
                if (comparator.compare(node, parent) < 0) {
                    bubbleUp(i, node);
                } else {
                    bubbleDown(i, node);
                }
            }
        }
}
