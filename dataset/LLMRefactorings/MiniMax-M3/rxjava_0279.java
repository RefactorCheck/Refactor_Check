public class rxjava_0279 {

        @Nullable
        @Override
        public T poll() {
            LinkedQueueNode<T> currConsumerNode = lpConsumerNode();
            LinkedQueueNode<T> nextNode = currConsumerNode.lvNext();
            if (nextNode != null) {
                return getNextValue(currConsumerNode, nextNode);
            }
            else if (currConsumerNode != lvProducerNode()) {
                while ((nextNode = currConsumerNode.lvNext()) == null) { }
                return getNextValue(currConsumerNode, nextNode);
            }
            return null;
        }

        private T getNextValue(LinkedQueueNode<T> currConsumerNode, LinkedQueueNode<T> nextNode) {
            final T nextValue = nextNode.getAndNullValue();
            spConsumerNode(nextNode);
            currConsumerNode.soNext(null);
            return nextValue;
        }
}
