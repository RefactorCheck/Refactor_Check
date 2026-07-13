public class rxjava_0227 {

            @Override
            @Nullable
            @SuppressWarnings("unchecked")
            public T getValue() {
                Node<Object> prev = null;
                Node<Object> h = head;
    
                for (;;) {
                    Node<Object> next = h.get();
                    if (next == null) {
                        break;
                    }
                    prev = h;
                    h = next;
                }
    
                return extractValue(h, prev);
            }

            @SuppressWarnings("unchecked")
            private T extractValue(Node<Object> node, Node<Object> prev) {
                Object v = node.value;
                if (v == null) {
                    return null;
                }
                if (NotificationLite.isComplete(v) || NotificationLite.isError(v)) {
                    return (T)prev.value;
                }

                return (T)v;
            }
}
