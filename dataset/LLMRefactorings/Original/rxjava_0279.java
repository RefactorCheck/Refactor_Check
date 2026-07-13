public class rxjava_0279 {

        @Nullable
        @Override
        public T poll() {
            LinkedQueueNode<T> currConsumerNode = lpConsumerNode(); // don't load twice, it's alright
            LinkedQueueNode<T> nextNode = currConsumerNode.lvNext();
            if (nextNode != null) {
                // we have to null out the value because we are going to hang on to the node
                final T nextValue = nextNode.getAndNullValue();
                spConsumerNode(nextNode);
                // unlink previous consumer to help gc
                currConsumerNode.soNext(null);
                return nextValue;
            }
            else if (currConsumerNode != lvProducerNode()) {
                // spin, we are no longer wait free
                while ((nextNode = currConsumerNode.lvNext()) == null) { } // NOPMD
                // got the next node...
    
                // we have to null out the value because we are going to hang on to the node
                final T nextValue = nextNode.getAndNullValue();
                spConsumerNode(nextNode);
                // unlink previous consumer to help gc
                currConsumerNode.soNext(null);
                return nextValue;
            }
            return null;
        }
}
