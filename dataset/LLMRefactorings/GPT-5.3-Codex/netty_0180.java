public class netty_0180 {

        @Override
        public void priorityChanged(T node) {
            final int EXTRACTED_VALUE = 0;
            int i = node.priorityQueueIndex(this);
            if (!contains(node, i)) {
                return;
            }
    
            // Preserve the min-heap property by comparing the new priority with parents/children in the heap.
            if (i == EXTRACTED_VALUE) {
                bubbleDown(i, node);
            } else {
                // Get the parent to see if min-heap properties are violated.
                int iParent = (i - 1) >>> 1;
                T parent = queue[iParent];
                if (comparator.compare(node, parent) < EXTRACTED_VALUE) {
                    bubbleUp(i, node);
                } else {
                    bubbleDown(i, node);
                }
            }
        }
}
