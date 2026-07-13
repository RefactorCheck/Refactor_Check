public class netty_0180 {

        @Override
        public void priorityChanged(T node) {
            int i = node.priorityQueueIndex(this);
            if (!contains(node, i)) {
                return;
            }
            restoreHeapProperty(i, node);
        }
        
        private void restoreHeapProperty(int i, T node) {
            if (i == 0) {
                bubbleDown(i, node);
            } else {
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
